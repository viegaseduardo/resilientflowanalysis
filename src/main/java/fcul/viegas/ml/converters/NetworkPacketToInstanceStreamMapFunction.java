/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.ml.converters;

import com.yahoo.labs.samoa.instances.Attribute;
import com.yahoo.labs.samoa.instances.Instances;
import com.yahoo.labs.samoa.instances.InstancesHeader;
import fcul.viegas.dto.NetworkHostBasedFeaturesDTO;
import fcul.viegas.dto.NetworkPacketDTO;
import fcul.viegas.dto.NetworkPacketFeaturesDTO;
import fcul.viegas.ml.dto.InstanceStreamDTO;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;
import moa.core.FastVector;
import moa.streams.InstanceStream;
import org.apache.flink.api.common.functions.FoldFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import weka.core.converters.ArffLoader;

/**
 *
 * @author viegas
 */
public class NetworkPacketToInstanceStreamMapFunction extends RichMapFunction<NetworkPacketFeaturesDTO, InstanceStreamDTO> {

    //private InstancesHeader instanceHeader;
    private Random rand;

    public NetworkPacketToInstanceStreamMapFunction() {

    }

    public void open(Configuration parameters) throws Exception {
        rand = new Random();
        rand.setSeed(this.hashCode() ^ 83484856847L);
    }

    @Override
    public InstanceStreamDTO map(NetworkPacketFeaturesDTO networkPacketFeatures) throws Exception {
        InstanceStreamDTO instance = new InstanceStreamDTO();

        double[] features = new double[NetworkPacketsDefinitions.numberOfFeatures];

        features[0] = Double.valueOf(networkPacketFeatures.getServiceBasedFeatures().getCountSrctoDst());
        features[1] = Double.valueOf(networkPacketFeatures.getServiceBasedFeatures().getCountDsttoSrc());
        features[2] = Double.valueOf(networkPacketFeatures.getServiceBasedFeatures().getNumBytesSrctoDst());
        features[3] = Double.valueOf(networkPacketFeatures.getServiceBasedFeatures().getNumBytesDsttoSrc());
        features[4] = Double.valueOf(networkPacketFeatures.getHostBasedFeatures().getNetworkPacketFeaturesSrctoDst().getCount());
        features[5] = Double.valueOf(networkPacketFeatures.getHostBasedFeatures().getNetworkPacketFeaturesSrctoDst().getNumBytes());
        features[6] = Double.valueOf(networkPacketFeatures.getHostBasedFeatures().getNetworkPacketFeaturesSrctoDst().getNumAck());
        features[7] = Double.valueOf(networkPacketFeatures.getHostBasedFeatures().getNetworkPacketFeaturesSrctoDst().getNumFin());
        features[8] = Double.valueOf(networkPacketFeatures.getHostBasedFeatures().getNetworkPacketFeaturesSrctoDst().getNumPushed());
        features[9] = Double.valueOf(networkPacketFeatures.getHostBasedFeatures().getNetworkPacketFeaturesSrctoDst().getNumRst());
        features[10] = Double.valueOf(networkPacketFeatures.getHostBasedFeatures().getNetworkPacketFeaturesSrctoDst().getNumSyn());
        features[11] = Double.valueOf(networkPacketFeatures.getHostBasedFeatures().getNetworkPacketFeaturesSrctoDst().getNumSynFin());
        features[12] = Double.valueOf(networkPacketFeatures.getHostBasedFeatures().getNetworkPacketFeaturesDsttoSrc().getCount());
        features[13] = Double.valueOf(networkPacketFeatures.getHostBasedFeatures().getNetworkPacketFeaturesDsttoSrc().getNumBytes());
        features[14] = Double.valueOf(networkPacketFeatures.getHostBasedFeatures().getNetworkPacketFeaturesDsttoSrc().getNumAck());
        features[15] = Double.valueOf(networkPacketFeatures.getHostBasedFeatures().getNetworkPacketFeaturesDsttoSrc().getNumFin());
        features[16] = Double.valueOf(networkPacketFeatures.getHostBasedFeatures().getNetworkPacketFeaturesDsttoSrc().getNumPushed());
        features[17] = Double.valueOf(networkPacketFeatures.getHostBasedFeatures().getNetworkPacketFeaturesDsttoSrc().getNumRst());
        features[18] = Double.valueOf(networkPacketFeatures.getHostBasedFeatures().getNetworkPacketFeaturesDsttoSrc().getNumSyn());
        features[19] = Double.valueOf(networkPacketFeatures.getHostBasedFeatures().getNetworkPacketFeaturesDsttoSrc().getNumSynFin());

        instance.setClassValue(networkPacketFeatures.getFlowClass());

        features[20] = instance.getClassValue();

        instance.setInstanceValues(features);
        instance.setOriginalObject(networkPacketFeatures);
        instance.prepareInstanceImpl();
        instance.setUniqueNumber(this.rand.nextLong());

        return instance;
    }

}
