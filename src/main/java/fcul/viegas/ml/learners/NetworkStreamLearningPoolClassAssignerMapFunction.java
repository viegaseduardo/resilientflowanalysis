/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.ml.learners;

import fcul.viegas.ml.dto.InstanceStreamDTO;
import fcul.viegas.ml.dto.InstanceStreamPool;
import java.util.HashMap;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

/**
 *
 * @author viegas
 */
public class NetworkStreamLearningPoolClassAssignerMapFunction extends RichFlatMapFunction<InstanceStreamDTO, InstanceStreamDTO> {

    private HashMap<Long, InstanceStreamPool> hashInstances;
    private int nClassifiersPool;

    public NetworkStreamLearningPoolClassAssignerMapFunction(int nClassifiers) {
        this.nClassifiersPool = nClassifiers;
    }

    public void open(Configuration parameters) throws Exception {
        this.hashInstances = new HashMap<>();
    }

    @Override
    public void flatMap(InstanceStreamDTO inst, Collector<InstanceStreamDTO> out) throws Exception {
        InstanceStreamPool instancePool = this.hashInstances.get(inst.getUniqueNumber());
        if (instancePool == null) {
            instancePool = new InstanceStreamPool();
            this.hashInstances.put(inst.getUniqueNumber(), instancePool);
        }

        instancePool.getInstances().add(inst);
        if (inst.getAssignedClassValueFromLearner().equals(0.0d)) {
            instancePool.setnNormal(instancePool.getnNormal() + 1);
        } else {
            instancePool.setnAttack(instancePool.getnAttack() + 1);
        }

        if (instancePool.getInstances().size() >= this.nClassifiersPool) {
            if (instancePool.getnNormal() > instancePool.getnAttack()) {
                inst.setAssignedClassValueFromPool(0.0d);
            } else {
                inst.setAssignedClassValueFromPool(1.0d);
            }
            this.hashInstances.remove(inst.getUniqueNumber());
            out.collect(inst);
        }
    }

}
