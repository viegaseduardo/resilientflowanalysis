/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.dto;

/**
 *
 * @author viegas
 */
public class NetworkPacketFeaturesCtoCDTO {
    
    private Integer numSyn;
    private Integer numSynFin;
    private Integer numRst;
    private Integer numPushed;
    private Integer numFin;
    private Integer numBytes;
    private Integer numAck;
    private Integer count;
    
    public NetworkPacketFeaturesCtoCDTO(){
        this.numSyn = 0;
        this.numSynFin = 0;
        this.numRst = 0;
        this.numPushed = 0;
        this.numFin = 0;
        this.numBytes = 0;
        this.numAck = 0;
        this.count = 0;
    }

    public Integer getNumSyn() {
        return numSyn;
    }

    public void setNumSyn(Integer numSyn) {
        this.numSyn = numSyn;
    }

    public Integer getNumSynFin() {
        return numSynFin;
    }

    public void setNumSynFin(Integer numSynFin) {
        this.numSynFin = numSynFin;
    }

    public Integer getNumRst() {
        return numRst;
    }

    public void setNumRst(Integer numRst) {
        this.numRst = numRst;
    }

    public Integer getNumPushed() {
        return numPushed;
    }

    public void setNumPushed(Integer numPushed) {
        this.numPushed = numPushed;
    }

    public Integer getNumFin() {
        return numFin;
    }

    public void setNumFin(Integer numFin) {
        this.numFin = numFin;
    }

    public Integer getNumBytes() {
        return numBytes;
    }

    public void setNumBytes(Integer numBytes) {
        this.numBytes = numBytes;
    }

    public Integer getNumAck() {
        return numAck;
    }

    public void setNumAck(Integer numAck) {
        this.numAck = numAck;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
    
    public String toString(){
        String str = "";
        
        str = str + this.count;
        str = str + "," + this.numBytes;
        str = str + "," + this.numAck;
        str = str + "," + this.numFin;
        str = str + "," + this.numPushed;
        str = str + "," + this.numRst;
        str = str + "," + this.numSyn;
        str = str + "," + this.numSynFin;
        
        return str;
    }
    
}
