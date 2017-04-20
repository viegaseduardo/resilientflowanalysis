/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.ml.learners;

import com.yahoo.labs.samoa.instances.Attribute;
import com.yahoo.labs.samoa.instances.InstanceImpl;
import com.yahoo.labs.samoa.instances.Instances;
import com.yahoo.labs.samoa.instances.InstancesHeader;
import fcul.viegas.ml.converters.NetworkPacketsDefinitions;
import fcul.viegas.ml.dto.InstanceStreamDTO;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import moa.core.FastVector;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.co.CoFlatMapFunction;
import org.apache.flink.streaming.api.functions.co.RichCoFlatMapFunction;
import org.apache.flink.util.Collector;
import weka.classifiers.misc.InputMappedClassifier;
import weka.classifiers.trees.HoeffdingTree;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

/**
 *
 * @author viegas
 */
public class NetworkStreamLearningClassifierMapFunction extends RichCoFlatMapFunction<InstanceStreamDTO, weka.classifiers.misc.InputMappedClassifier, InstanceStreamDTO> {

    private InstancesHeader streamLearningInstanceHeader;
    private float percentOfSubsetFeatures;
    private String mappingFeatures;
    private weka.classifiers.misc.InputMappedClassifier classifier;
    private weka.core.Instances coreInstances;

    public NetworkStreamLearningClassifierMapFunction(float percent) {
        this.percentOfSubsetFeatures = percent;
    }

    public static String printFeatures(ArrayList<Integer> listaFeatures, int nTotalFeatures) {
        String ret = "[";
        int j = 0;
        for (int i = 1; i <= nTotalFeatures; i++) {
            if (j != listaFeatures.size()) {
                Integer value = listaFeatures.get(j);
                if (value == i) {
                    ret = ret + "1";
                    j++;
                } else {
                    ret = ret + "0";
                }

            } else {
                ret = ret + "0";
            }
            if (i == nTotalFeatures) {
                ret = ret + "]";
            } else {
                ret = ret + ",";
            }
        }
        return ret;
    }

    public void open(Configuration parameters) throws Exception {

        try {
            BufferedReader reader
                    = new BufferedReader(new FileReader(NetworkPacketsDefinitions.arffPath));
            ArffLoader.ArffReader arff = new ArffLoader.ArffReader(reader);
            weka.core.Instances dataTrain = arff.getData();
            dataTrain.setClassIndex(dataTrain.numAttributes() - 1);

            Random rand = new Random();
            rand.setSeed(this.hashCode() ^ 83484856847L);
            ArrayList<Integer> listaFeaturesChoosen = new ArrayList<>();
            for (int j = 1; j < dataTrain.numAttributes(); j++) {
                listaFeaturesChoosen.add(j);
            }

            //chose features to remove
            int numberOfFeaturesToBeRemoved = (int) (listaFeaturesChoosen.size() * (1.0f - percentOfSubsetFeatures));
            for (int j = 0; j < numberOfFeaturesToBeRemoved; j++) {
                int numberOfFeature = rand.nextInt(listaFeaturesChoosen.size());
                listaFeaturesChoosen.remove(numberOfFeature);
            }
            System.out.println("LEARNING: featureSubset: " + NetworkStreamLearningClassifierMapFunction.printFeatures(listaFeaturesChoosen, dataTrain.numAttributes() - 1));
            mappingFeatures = "featureSubset: " + NetworkStreamLearningClassifierMapFunction.printFeatures(listaFeaturesChoosen, dataTrain.numAttributes() - 1);
            FastVector attributes = new FastVector();
            int indexLista = 0;
            for (int i = 0; i < NetworkPacketsDefinitions.numberOfFeatures; i++) {
                if (indexLista < listaFeaturesChoosen.size()
                        && listaFeaturesChoosen.get(indexLista) == (i + 1)) {
                    indexLista++;
                    attributes.addElement(new Attribute("att" + (i + 1)));
                }
            }
            FastVector classLabels = new FastVector();
            for (int i = 0; i < NetworkPacketsDefinitions.numberOfClasses; i++) {
                classLabels.addElement("class" + (i + 1));
            }
            attributes.addElement(new Attribute("class", classLabels));

            this.streamLearningInstanceHeader = new InstancesHeader(new Instances(
                    "FlinkPhd", attributes, 0));

            this.streamLearningInstanceHeader.setClassIndex(attributes.size());

            listaFeaturesChoosen.add(dataTrain.numAttributes());

            String[] options = new String[2];
            options[0] = "-R";

            String optRemove = "";
            for (int j = 0; j < listaFeaturesChoosen.size() - 1; j++) {
                optRemove = optRemove + listaFeaturesChoosen.get(j) + ",";
            }
            optRemove = optRemove + listaFeaturesChoosen.get(listaFeaturesChoosen.size() - 1);
            options[1] = optRemove;

            Remove remove = new Remove();
            remove.setOptions(options);
            remove.setInvertSelection(true);
            remove.setInputFormat(dataTrain);

            weka.core.Instances newdataFeat = Filter.useFilter(dataTrain, remove);

            HoeffdingTree tree = new HoeffdingTree();

            weka.classifiers.misc.InputMappedClassifier classifier = new weka.classifiers.misc.InputMappedClassifier();
            classifier.setModelHeader(newdataFeat);
            classifier.setClassifier(tree);
            classifier.buildClassifier(newdataFeat);
            this.classifier = classifier;
            this.coreInstances = dataTrain;
            this.coreInstances.clear();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public InstanceStreamDTO map(InstanceStreamDTO instance) throws Exception {

        weka.core.Instance inst = instance.getInstance();
        inst.setDataset(this.coreInstances);
        inst.setClassValue(inst.classValue());
        inst = classifier.constructMappedInstance(inst);

        HoeffdingTree tree = (HoeffdingTree) classifier.getClassifier();
        double[] classe = tree.distributionForInstance(inst);
        instance.setInstance(null);
        //System.out.println("\t classe[0]: " + classe[0] + " classe[1]: " + classe[1]);
        if (classe[0] > classe[1]) {
            instance.setAssignedClassValueFromLearner(0.0d);
        } else {
            instance.setAssignedClassValueFromLearner(1.0d);
        }
        return instance;
    }

    //test
    @Override
    public void flatMap1(InstanceStreamDTO inst, Collector<InstanceStreamDTO> out) throws Exception {
        out.collect(this.map(inst));
    }

    @Override
    public void flatMap2(InputMappedClassifier in2, Collector<InstanceStreamDTO> clctr) throws Exception {
    }

}
