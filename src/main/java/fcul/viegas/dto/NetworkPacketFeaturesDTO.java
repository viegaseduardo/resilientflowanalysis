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
public class NetworkPacketFeaturesDTO {

    private NetworkServiceBasedFeaturesDTO serviceBasedFeatures;
    private NetworkHostBasedFeaturesDTO hostBasedFeatures;

    public Double getFlowClass() {

        if (this.hostBasedFeatures.getSourceIP().equals("192.168.0.112")) {
            return 1.0d;
        } else if (this.hostBasedFeatures.getSourceIP().equals("192.168.0.113")) {
            return 1.0d;
        } else if (this.hostBasedFeatures.getSourceIP().equals("192.168.0.114")) {
            return 1.0d;
        } else if (this.hostBasedFeatures.getSourceIP().equals("192.168.0.115")) {
            return 1.0d;
        }

        return 0.0d;
    }

    public NetworkServiceBasedFeaturesDTO getServiceBasedFeatures() {
        return serviceBasedFeatures;
    }

    public void setServiceBasedFeatures(NetworkServiceBasedFeaturesDTO serviceBasedFeatures) {
        this.serviceBasedFeatures = new NetworkServiceBasedFeaturesDTO();
        this.serviceBasedFeatures.setCountDsttoSrc(serviceBasedFeatures.getCountDsttoSrc());
        this.serviceBasedFeatures.setCountSrctoDst(serviceBasedFeatures.getCountSrctoDst());
        this.serviceBasedFeatures.setDestinationIP(serviceBasedFeatures.getDestinationIP());
        this.serviceBasedFeatures.setDestinationPort(serviceBasedFeatures.getDestinationPort());
        this.serviceBasedFeatures.setFirstTime(serviceBasedFeatures.getFirstTime());
        this.serviceBasedFeatures.setNumBytesDsttoSrc(serviceBasedFeatures.getNumBytesDsttoSrc());
        this.serviceBasedFeatures.setNumBytesSrctoDst(serviceBasedFeatures.getNumBytesSrctoDst());
        this.serviceBasedFeatures.setSourceIP(serviceBasedFeatures.getSourceIP());
        this.serviceBasedFeatures.setSourcePort(serviceBasedFeatures.getSourcePort());
    }

    public NetworkHostBasedFeaturesDTO getHostBasedFeatures() {
        return hostBasedFeatures;
    }

    public void setHostBasedFeatures(NetworkHostBasedFeaturesDTO hostBasedFeatures) {
        this.hostBasedFeatures = new NetworkHostBasedFeaturesDTO();
        this.hostBasedFeatures.setDestinationIP(hostBasedFeatures.getDestinationIP());
        this.hostBasedFeatures.setFirstTime(hostBasedFeatures.getFirstTime());
        this.hostBasedFeatures.setSourceIP(hostBasedFeatures.getSourceIP());
    }

    public String toString() {
        String str = "";

        str = str + this.getServiceBasedFeatures().toString();
        str = str + this.getHostBasedFeatures().toString();

        if (this.hostBasedFeatures.getSourceIP().equals("192.168.0.112")) {
            str = str + "," + "attack";
            
        } else if (this.hostBasedFeatures.getSourceIP().equals("192.168.0.113")) {
            str = str + "," + "attack";
        } else if (this.hostBasedFeatures.getSourceIP().equals("192.168.0.114")) {
            str = str + "," + "attack";
        } else if (this.hostBasedFeatures.getSourceIP().equals("192.168.0.115")) {
            str = str + "," + "attack";
        } else {
            str = str + "," + "normal";
        }

        return str;
    }
}
