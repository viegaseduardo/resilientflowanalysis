/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.ml.dto;

import com.yahoo.labs.samoa.instances.DenseInstance;
import com.yahoo.labs.samoa.instances.Instance;
import com.yahoo.labs.samoa.instances.InstancesHeader;
import com.yahoo.labs.samoa.instances.SparseInstance;
import fcul.viegas.dto.NetworkPacketFeaturesDTO;
import java.util.Locale;

/**
 *
 * @author viegas
 */
public class InstanceStreamDTO {

    private Object originalObject;
    private double[] instanceValues;
    private weka.core.Instance instance;
    private Double classValue;
    private InstancesHeader instanceHeader;
    private Double assignedClassValueFromLearner;
    private Double assignedClassValueFromPool;
    private Long uniqueNumber;

    public void prepareInstanceImpl() {
        weka.core.Instance inst = new weka.core.DenseInstance(1.0, this.instanceValues);

        this.instance = inst;
    }

    public Long getUniqueNumber() {
        return uniqueNumber;
    }

    public void setUniqueNumber(Long uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }

    public Double getAssignedClassValueFromLearner() {
        return assignedClassValueFromLearner;
    }

    public void setAssignedClassValueFromLearner(Double assignedClassValueFromLearner) {
        this.assignedClassValueFromLearner = assignedClassValueFromLearner;
    }

    public Double getAssignedClassValueFromPool() {
        return assignedClassValueFromPool;
    }

    public void setAssignedClassValueFromPool(Double assignedClassValueFromPool) {
        this.assignedClassValueFromPool = assignedClassValueFromPool;
    }

    public InstancesHeader getInstanceHeader() {
        return instanceHeader;
    }

    public void setInstanceHeader(InstancesHeader instanceHeader) {
        this.instanceHeader = instanceHeader;
    }

    public Double getClassValue() {
        return classValue;
    }

    public void setClassValue(Double classValue) {
        this.classValue = classValue;
    }

    public weka.core.Instance getInstance() {
        return instance;
    }

    public void setInstance(weka.core.Instance instance) {
        this.instance = instance;
    }

    public Object getOriginalObject() {
        return originalObject;
    }

    public void setOriginalObject(Object originalObject) {
        this.originalObject = originalObject;
    }

    public double[] getInstanceValues() {
        return instanceValues;
    }

    public void setInstanceValues(double[] instanceValues) {
        this.instanceValues = instanceValues;
    }

    public String toString() {
        String ret = "";

        if (this.classValue != 0.0d) {
            System.out.println("t");
        }

        for (int i = 0; i < this.instanceValues.length - 1; i++) {
            if (i == 0) {
                ret = String.format(Locale.US, "%.4f", instanceValues[i]);
            } else {
                ret = ret + "," + String.format(Locale.US, "%.4f", instanceValues[i]);
            }
        }

        if (this.classValue == 0.0d) {
            ret = ret + ",normal";
        } else {
            ret = ret + ",attack";
        }

        return ret;
    }

}
