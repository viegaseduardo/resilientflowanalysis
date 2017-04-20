/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.window;

import fcul.viegas.dto.NetworkHostBasedFeaturesDTO;
import fcul.viegas.dto.NetworkServiceBasedFeaturesDTO;
import fcul.viegas.dto.NetworkPacketDTO;
import fcul.viegas.dto.NetworkPacketFeaturesCtoCDTO;
import org.apache.flink.api.common.functions.FoldFunction;
import org.apache.flink.configuration.Configuration;

/**
 *
 * @author viegas
 */
public class NetworkHostBasedFoldFunction implements FoldFunction<NetworkPacketDTO, NetworkHostBasedFeaturesDTO> {

    @Override
    public NetworkHostBasedFeaturesDTO fold(NetworkHostBasedFeaturesDTO hostBased, NetworkPacketDTO networkPacket) throws Exception {
        if (hostBased.getFirstTime()) {

            if (networkPacket.getSourceIP().equals("192.168.0.200")) {
                hostBased.setSourceIP(networkPacket.getDestinationIP());
                hostBased.setDestinationIP(networkPacket.getSourceIP());
            } else {
                hostBased.setSourceIP(networkPacket.getSourceIP());
                hostBased.setDestinationIP(networkPacket.getDestinationIP());
            }

            hostBased.setFirstTime(false);
        }

        NetworkPacketFeaturesCtoCDTO networkPacketFeatures;

        if (networkPacket.getSourceIP().equals(hostBased.getSourceIP())) {
            networkPacketFeatures = hostBased.getNetworkPacketFeaturesSrctoDst();
        } else {
            networkPacketFeatures = hostBased.getNetworkPacketFeaturesDsttoSrc();
        }

        networkPacketFeatures.setCount(networkPacketFeatures.getCount() + 1);
        networkPacketFeatures.setNumBytes(networkPacketFeatures.getNumBytes() + networkPacket.getPacket_size());

        if (networkPacket.getTcp_ack()) {
            networkPacketFeatures.setNumAck(networkPacketFeatures.getNumAck() + 1);
        }

        if (networkPacket.getTcp_fin()) {
            networkPacketFeatures.setNumFin(networkPacketFeatures.getNumFin() + 1);
        }

        if (networkPacket.getTcp_psh()) {
            networkPacketFeatures.setNumPushed(networkPacketFeatures.getNumPushed() + 1);
        }

        if (networkPacket.getTcp_rst()) {
            networkPacketFeatures.setNumRst(networkPacketFeatures.getNumRst() + 1);
        }

        if (networkPacket.getTcp_syn()) {
            networkPacketFeatures.setNumSyn(networkPacketFeatures.getNumSyn() + 1);

            if (networkPacket.getTcp_fin()) {
                networkPacketFeatures.setNumSynFin(networkPacketFeatures.getNumSynFin() + 1);
            }
        }

        return hostBased;
    }

}
