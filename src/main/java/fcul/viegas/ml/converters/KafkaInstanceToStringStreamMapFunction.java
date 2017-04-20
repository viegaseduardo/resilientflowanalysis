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
public class KafkaInstanceToStringStreamMapFunction extends RichMapFunction<InstanceStreamDTO, String>{

    @Override
    public String map(InstanceStreamDTO inst) throws Exception {
        String ret = "";
        
        for(int i = 0; i < inst.getInstanceValues().length - 1; i++){
            ret = ret + inst.getInstanceValues()[i] + ";";
        }
        ret = ret + inst.getAssignedClassValueFromPool();
        
        return ret;
    }
    
}
