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
public class NetworkHostBasedFeaturesDTO {

    private String sourceIP;
    private String destinationIP;
    private Boolean firstTime;

    private NetworkPacketFeaturesCtoCDTO networkPacketFeaturesSrctoDst;
    private NetworkPacketFeaturesCtoCDTO networkPacketFeaturesDsttoSrc;

    public NetworkHostBasedFeaturesDTO() {
        this.sourceIP = this.destinationIP = "";

        this.networkPacketFeaturesSrctoDst = new NetworkPacketFeaturesCtoCDTO();
        this.networkPacketFeaturesDsttoSrc = new NetworkPacketFeaturesCtoCDTO();
        this.firstTime = true;
    }

    public Boolean getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Boolean firstTime) {
        this.firstTime = firstTime;
    }

    public String getSourceIP() {
        return sourceIP;
    }

    public void setSourceIP(String sourceIP) {
        this.sourceIP = sourceIP;
    }

    public String getDestinationIP() {
        return destinationIP;
    }

    public void setDestinationIP(String destinationIP) {
        this.destinationIP = destinationIP;
    }

    public NetworkPacketFeaturesCtoCDTO getNetworkPacketFeaturesSrctoDst() {
        return networkPacketFeaturesSrctoDst;
    }

    public void setNetworkPacketFeaturesSrctoDst(NetworkPacketFeaturesCtoCDTO networkPacketFeaturesSrctoDst) {
        this.networkPacketFeaturesSrctoDst = new NetworkPacketFeaturesCtoCDTO();
        this.networkPacketFeaturesSrctoDst.setCount(networkPacketFeaturesSrctoDst.getCount());
        this.networkPacketFeaturesSrctoDst.setNumAck(networkPacketFeaturesSrctoDst.getNumAck());
        this.networkPacketFeaturesSrctoDst.setNumBytes(networkPacketFeaturesSrctoDst.getNumBytes());
        this.networkPacketFeaturesSrctoDst.setNumFin(networkPacketFeaturesSrctoDst.getNumFin());
        this.networkPacketFeaturesSrctoDst.setNumPushed(networkPacketFeaturesSrctoDst.getNumPushed());
        this.networkPacketFeaturesSrctoDst.setNumRst(networkPacketFeaturesSrctoDst.getNumRst());
        this.networkPacketFeaturesSrctoDst.setNumSyn(networkPacketFeaturesSrctoDst.getNumSyn());
        this.networkPacketFeaturesSrctoDst.setNumSynFin(networkPacketFeaturesSrctoDst.getNumSynFin());
    }

    public NetworkPacketFeaturesCtoCDTO getNetworkPacketFeaturesDsttoSrc() {
        return networkPacketFeaturesDsttoSrc;
    }

    public void setNetworkPacketFeaturesDsttoSrc(NetworkPacketFeaturesCtoCDTO networkPacketFeaturesDsttoSrc) {
        this.networkPacketFeaturesDsttoSrc = new NetworkPacketFeaturesCtoCDTO();
        this.networkPacketFeaturesDsttoSrc.setCount(networkPacketFeaturesDsttoSrc.getCount());
        this.networkPacketFeaturesDsttoSrc.setNumAck(networkPacketFeaturesDsttoSrc.getNumAck());
        this.networkPacketFeaturesDsttoSrc.setNumBytes(networkPacketFeaturesDsttoSrc.getNumBytes());
        this.networkPacketFeaturesDsttoSrc.setNumFin(networkPacketFeaturesDsttoSrc.getNumFin());
        this.networkPacketFeaturesDsttoSrc.setNumPushed(networkPacketFeaturesDsttoSrc.getNumPushed());
        this.networkPacketFeaturesDsttoSrc.setNumRst(networkPacketFeaturesDsttoSrc.getNumRst());
        this.networkPacketFeaturesDsttoSrc.setNumSyn(networkPacketFeaturesDsttoSrc.getNumSyn());
        this.networkPacketFeaturesDsttoSrc.setNumSynFin(networkPacketFeaturesDsttoSrc.getNumSynFin());
    }

    public String toString() {
        String str = "";

        str = str + "," + this.networkPacketFeaturesSrctoDst.toString();
        str = str + "," + this.networkPacketFeaturesDsttoSrc.toString();

        return str;
    }
}
