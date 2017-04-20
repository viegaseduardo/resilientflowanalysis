/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.window;

import fcul.viegas.dto.NetworkServiceBasedFeaturesDTO;
import fcul.viegas.dto.NetworkPacketDTO;
import fcul.viegas.dto.NetworkHostBasedFeaturesDTO;
import org.apache.flink.api.common.functions.FoldFunction;

/**
 *
 * @author viegas
 */
public class NetworkServiceBasedFoldFunction implements FoldFunction<NetworkPacketDTO, NetworkServiceBasedFeaturesDTO> {

    @Override
    public NetworkServiceBasedFeaturesDTO fold(NetworkServiceBasedFeaturesDTO serviceBased, NetworkPacketDTO networkPacket) throws Exception {

        if (serviceBased.getFirstTime()) {

            if (networkPacket.getSourceIP().equals("192.168.0.200")) {
                serviceBased.setSourceIP(networkPacket.getDestinationIP());
                serviceBased.setDestinationIP(networkPacket.getSourceIP());
                serviceBased.setSourcePort(networkPacket.getDestinationPort());
                serviceBased.setDestinationPort(networkPacket.getSourcePort());

            } else {
                serviceBased.setSourceIP(networkPacket.getSourceIP());
                serviceBased.setDestinationIP(networkPacket.getDestinationIP());
                serviceBased.setSourcePort(networkPacket.getSourcePort());
                serviceBased.setDestinationPort(networkPacket.getDestinationPort());
            }

            serviceBased.setFirstTime(false);
        }

        if (networkPacket.getSourceIP().equals(serviceBased.getSourceIP()) && networkPacket.getSourcePort().equals(serviceBased.getSourcePort())) {
            serviceBased.setCountSrctoDst(serviceBased.getCountSrctoDst() + 1);
            serviceBased.setNumBytesSrctoDst(serviceBased.getNumBytesSrctoDst() + networkPacket.getPacket_size());
        } else {
            serviceBased.setCountDsttoSrc(serviceBased.getCountDsttoSrc() + 1);
            serviceBased.setNumBytesDsttoSrc(serviceBased.getNumBytesDsttoSrc() + networkPacket.getPacket_size());
        }
        //System.out.println(serviceBased.getCountSrctoDst());

        return serviceBased;
    }

}
