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
public class NetworkServiceBasedFeaturesDTO {

    private Integer numBytesSrctoDst;
    private Integer countSrctoDst;
    private Integer numBytesDsttoSrc;
    private Integer countDsttoSrc;

    private String sourceIP;
    private String destinationIP;
    private Integer sourcePort;
    private Integer destinationPort;

    private Boolean firstTime;

    public NetworkServiceBasedFeaturesDTO() {
        this.numBytesSrctoDst = 0;
        this.countSrctoDst = 0;
        this.numBytesDsttoSrc = 0;
        this.countDsttoSrc = 0;
        this.sourcePort = this.destinationPort = 0;
        this.sourceIP = this.destinationIP = "";
        this.firstTime = true;
    }

    public Boolean getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Boolean firstTime) {
        this.firstTime = firstTime;
    }

    public Integer getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(Integer sourcePort) {
        this.sourcePort = sourcePort;
    }

    public Integer getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(Integer destinationPort) {
        this.destinationPort = destinationPort;
    }

    public Integer getNumBytesSrctoDst() {
        return numBytesSrctoDst;
    }

    public void setNumBytesSrctoDst(Integer numBytesSrctoDst) {
        this.numBytesSrctoDst = numBytesSrctoDst;
    }

    public Integer getCountSrctoDst() {
        return countSrctoDst;
    }

    public void setCountSrctoDst(Integer countSrctoDst) {
        this.countSrctoDst = countSrctoDst;
    }

    public Integer getNumBytesDsttoSrc() {
        return numBytesDsttoSrc;
    }

    public void setNumBytesDsttoSrc(Integer numBytesDsttoSrc) {
        this.numBytesDsttoSrc = numBytesDsttoSrc;
    }

    public Integer getCountDsttoSrc() {
        return countDsttoSrc;
    }

    public void setCountDsttoSrc(Integer countDsttoSrc) {
        this.countDsttoSrc = countDsttoSrc;
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

    public String toString() {
        String str = "";

//        str = str + this.sourceIP;
//        str = str + "," + this.sourcePort;
//        str = str + "," + this.destinationIP;
//        str = str + "," + this.destinationPort;
        str = str + this.countSrctoDst;
        str = str + "," + this.countDsttoSrc;
        str = str + "," + this.numBytesSrctoDst;
        str = str + "," + this.numBytesDsttoSrc;

        return str;
    }

}
