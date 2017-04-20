/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.ml.converters;

import fcul.viegas.ml.dto.InstanceStreamDTO;
import org.apache.flink.api.common.functions.RichMapFunction;

/**
 *
 * @author viegas
 */
public class KafkaStringToInstanceStreamMapFunction extends RichMapFunction<String, InstanceStreamDTO> {

    @Override
    public InstanceStreamDTO map(String in) throws Exception {
        InstanceStreamDTO inst = new InstanceStreamDTO();
        String[] split = in.split(";");
        double[] featVec = new double[split.length];

        for (int i = 0; i < featVec.length; i++) {
            featVec[i] = Double.valueOf(split[i]);
        }
        
        inst.setInstanceValues(featVec);

        return inst;
    }

}
