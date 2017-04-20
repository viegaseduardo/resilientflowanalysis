/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.ml.learners;

import com.yahoo.labs.samoa.instances.Attribute;
import com.yahoo.labs.samoa.instances.Instances;
import com.yahoo.labs.samoa.instances.InstancesHeader;
import fcul.viegas.ml.converters.NetworkPacketsDefinitions;
import fcul.viegas.ml.dto.InstanceStreamDTO;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import moa.core.FastVector;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;
import weka.classifiers.misc.InputMappedClassifier;
import weka.classifiers.trees.HoeffdingTree;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

/**
 *
 * @author viegas
 */
public class NetworkStreamLearningClassifierUpdateMapFunction extends RichFlatMapFunction<InstanceStreamDTO, weka.classifiers.misc.InputMappedClassifier> {

    private InstancesHeader streamLearningInstanceHeader;
    private float percentOfSubsetFeatures;
    private String mappingFeatures;
    private weka.classifiers.misc.InputMappedClassifier classifier;
    private weka.core.Instances coreInstances;
    private int nInstances;
    private int nInstancesBeforeUpdate;
    
    public NetworkStreamLearningClassifierUpdateMapFunction(int nInstances, float percentOfFeatures){
        this.nInstances = nInstances;
        this.nInstancesBeforeUpdate = 0;
        this.percentOfSubsetFeatures = percentOfFeatures;
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
            System.out.println("UPDATES featureSubset: " + NetworkStreamLearningClassifierMapFunction.printFeatures(listaFeaturesChoosen, dataTrain.numAttributes() - 1));
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

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void flatMap(InstanceStreamDTO in, Collector<InputMappedClassifier> out) throws Exception {
        /*weka.core.Instance inst = in.getInstance();
        inst.setDataset(this.coreInstances);
        inst.setClassValue(inst.classValue());
        inst = classifier.constructMappedInstance(inst);

        HoeffdingTree tree = (HoeffdingTree) classifier.getClassifier();
        tree.updateClassifier(inst);
        
        this.nInstancesBeforeUpdate++;
        if(this.nInstancesBeforeUpdate >= this.nInstances){
            this.nInstancesBeforeUpdate = 0;
            out.collect(classifier);
        }*/
    }

}
