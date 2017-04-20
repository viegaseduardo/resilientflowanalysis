/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.main;

import fcul.viegas.dto.NetworkServiceBasedFeaturesDTO;
import fcul.viegas.dto.NetworkPacketDTO;
import fcul.viegas.dto.NetworkPacketFeaturesDTO;
import fcul.viegas.dto.NetworkHostBasedFeaturesDTO;
import fcul.viegas.map.NetworkPacketParserFlatMap;
import fcul.viegas.ml.converters.KafkaInstanceToStringStreamMapFunction;
import fcul.viegas.ml.converters.KafkaStringToInstanceStreamMapFunction;
import fcul.viegas.ml.dto.InstanceStreamDTO;
import fcul.viegas.ml.converters.NetworkPacketToInstanceStreamMapFunction;
import fcul.viegas.ml.learners.NetworkStreamLearningClassifierMapFunction;
import fcul.viegas.ml.learners.NetworkStreamLearningClassifierUpdateMapFunction;
import fcul.viegas.ml.learners.NetworkStreamLearningPoolClassAssignerMapFunction;
import fcul.viegas.timestamp.NetworkPacketTimestampAssigner;
import fcul.viegas.window.NetworkFeatureJoinMapper;
import fcul.viegas.window.NetworkHostBasedFoldFunction;
import fcul.viegas.window.NetworkServiceBasedFoldFunction;
import java.util.Properties;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.JoinedStreams;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer09;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

/**
 *
 * @author viegas
 */
public class Main {

    public static int nClassificadoresPool = 10;
    public static int nBeforeUpdate = 1000;


    public static void main(String args[]) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //env.setParallelism(5);

        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        Properties props = new Properties();
        props.setProperty("zookeeper.connect", "10.32.1.208:2181");
        props.setProperty("bootstrap.servers", "10.32.1.208:9092"); 
        props.setProperty("group.id", "classifiedinstances");                 
        props.setProperty("auto.offset.reset", "earliest");       
        
        
        DataStreamSource<String> dataStreamSource = env.addSource(
                new FlinkKafkaConsumer09<>("cenario", new SimpleStringSchema(), props));

        DataStreamSource<String> kafkaReaderClassifiedInstances = env.addSource(
                new FlinkKafkaConsumer09<>("classifiedinstances", new SimpleStringSchema(), props));

        FlinkKafkaProducer09<String> producerOfClassifiedInstances = new FlinkKafkaProducer09<String>(
                "10.32.1.208:9092", 
                "classifiedinstances", 
                new SimpleStringSchema());   
        
        
        //read file
        //DataStreamSource<String> dataStreamSource = env.readTextFile("/home/viegas/Desktop/saida/cenario1.txt");
        //dataStreamSource.setParallelism(1);

        //parse data and correct order
        SingleOutputStreamOperator<NetworkPacketDTO> singleOutput = dataStreamSource.map(new NetworkPacketParserFlatMap())
                .assignTimestampsAndWatermarks(new NetworkPacketTimestampAssigner());
        

        //group by Source IP and Destination IP
        KeyedStream<NetworkPacketDTO, Integer> keyHostBased = singleOutput.keyBy(new KeySelector<NetworkPacketDTO, Integer>() {
            @Override
            public Integer getKey(NetworkPacketDTO in) throws Exception {
                Integer srcDstHash = (in.getSourceIP() + in.getDestinationIP()).hashCode();
                Integer dstSrcHash = (in.getDestinationIP() + in.getSourceIP()).hashCode();
                Integer hash = srcDstHash ^ dstSrcHash;
                return hash;
            }
        });

        KeyedStream<NetworkPacketDTO, Integer> keyServiceBased = singleOutput.keyBy(new KeySelector<NetworkPacketDTO, Integer>() {
            @Override
            public Integer getKey(NetworkPacketDTO in) throws Exception {
                Integer srcDstHash = (in.getSourceIP() + in.getSourcePort() + in.getDestinationIP() + in.getDestinationPort()).hashCode();
                Integer dstSrcHash = (in.getDestinationIP() + in.getDestinationPort() + in.getSourceIP() + in.getSourcePort()).hashCode();
                Integer hash = srcDstHash ^ dstSrcHash;
                return hash;
            }
        });

        //extract service-based features
        SingleOutputStreamOperator<NetworkServiceBasedFeaturesDTO> networkServiceBasedStream
                = //keyServiceBased.timeWindow(Time.milliseconds(2000l), Time.milliseconds(500l))
                keyServiceBased.window(TumblingEventTimeWindows.of(Time.milliseconds(2000l)))
                        .fold(new NetworkServiceBasedFeaturesDTO(), new NetworkServiceBasedFoldFunction());

        //extract host-based features 
        SingleOutputStreamOperator<NetworkHostBasedFeaturesDTO> networkHostBasedStream
                = keyHostBased.window(TumblingEventTimeWindows.of(Time.milliseconds(2000l)))
                        .fold(new NetworkHostBasedFeaturesDTO(), new NetworkHostBasedFoldFunction());

        //join both streams back together
        DataStream<NetworkPacketFeaturesDTO> networkFeatures = networkServiceBasedStream.join(networkHostBasedStream)
                .where(new KeySelector<NetworkServiceBasedFeaturesDTO, Integer>() {
                    @Override
                    public Integer getKey(NetworkServiceBasedFeaturesDTO in) throws Exception {
                        Integer srcDstHash = (in.getSourceIP() + in.getDestinationIP()).hashCode();
                        Integer dstSrcHash = (in.getDestinationIP() + in.getSourceIP()).hashCode();
                        Integer hash = srcDstHash ^ dstSrcHash;
                        return hash;
                    }
                }).equalTo(new KeySelector<NetworkHostBasedFeaturesDTO, Integer>() {
            @Override
            public Integer getKey(NetworkHostBasedFeaturesDTO in) throws Exception {
                Integer srcDstHash = (in.getSourceIP() + in.getDestinationIP()).hashCode();
                Integer dstSrcHash = (in.getDestinationIP() + in.getSourceIP()).hashCode();
                Integer hash = srcDstHash ^ dstSrcHash;
                return hash;
            }
        }).window(TumblingEventTimeWindows.of(Time.milliseconds(2000l)))
                .apply(new NetworkFeatureJoinMapper());

        //convert flow into parseable format for ML MOA
        SingleOutputStreamOperator<InstanceStreamDTO> instanceStream = 
                networkFeatures.map(new NetworkPacketToInstanceStreamMapFunction());
        
        //creates stream from kafka and map to instanceStream back again
        DataStream<InstanceStreamDTO> kafkaStreamSource = 
                kafkaReaderClassifiedInstances.map(new KafkaStringToInstanceStreamMapFunction()).broadcast();
        
        //creates stream of new classifiers
        DataStream<weka.classifiers.misc.InputMappedClassifier> updatedClassifierStream = 
                kafkaStreamSource.flatMap(new NetworkStreamLearningClassifierUpdateMapFunction(nBeforeUpdate, 0.5f))
                        .setParallelism(nClassificadoresPool)
                        .rebalance();
        
        //learners pool
        DataStream<InstanceStreamDTO> instanceStreamLearned = instanceStream.broadcast().connect(updatedClassifierStream)
                .flatMap(new NetworkStreamLearningClassifierMapFunction(0.5f))
                .setParallelism(nClassificadoresPool);
        

        DataStream<InstanceStreamDTO> poolClassAssigner = instanceStreamLearned.shuffle()
                .flatMap(new NetworkStreamLearningPoolClassAssignerMapFunction(nClassificadoresPool));

        
        poolClassAssigner.map(new KafkaInstanceToStringStreamMapFunction());//.writeAsText("testeee.txt");
                //producerOfClassifiedInstances);
        
        //instanceStream.writeAsText("/home/viegas/Desktop/saida/features.arff");
        //networkFeatures.writeAsText("/home/viegas/Desktop/saida/features.arff");
        //singleOutput.print();
        //networkFeatures.print();
        long startTime = System.currentTimeMillis();

        env.execute("meu job");

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Tempo de execucao: " + elapsedTime);
    }
}
