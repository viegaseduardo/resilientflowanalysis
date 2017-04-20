/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.ml.dto;

import java.util.ArrayList;

/**
 *
 * @author viegas
 */
public class InstanceStreamPool {

    private ArrayList<InstanceStreamDTO> instances;
    private int nAttack;
    private int nNormal;

    public InstanceStreamPool() {
        this.instances = new ArrayList<>();
        this.nAttack = 0;
        this.nNormal = 0;
    }
    

    public int getnAttack() {
        return nAttack;
    }

    public void setnAttack(int nAttack) {
        this.nAttack = nAttack;
    }

    public int getnNormal() {
        return nNormal;
    }

    public void setnNormal(int nNormal) {
        this.nNormal = nNormal;
    }

    public ArrayList<InstanceStreamDTO> getInstances() {
        return instances;
    }

    public void setInstances(ArrayList<InstanceStreamDTO> instances) {
        this.instances = instances;
    }

}
