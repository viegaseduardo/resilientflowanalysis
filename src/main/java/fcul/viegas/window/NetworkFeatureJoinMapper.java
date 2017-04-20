/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.window;

import fcul.viegas.dto.NetworkServiceBasedFeaturesDTO;
import fcul.viegas.dto.NetworkPacketFeaturesDTO;
import fcul.viegas.dto.NetworkHostBasedFeaturesDTO;
import org.apache.flink.api.common.functions.FlatJoinFunction;
import org.apache.flink.util.Collector;

/**
 *
 * @author viegas
 */
public class NetworkFeatureJoinMapper implements FlatJoinFunction<NetworkServiceBasedFeaturesDTO, NetworkHostBasedFeaturesDTO, NetworkPacketFeaturesDTO> {

    @Override
    public void join(NetworkServiceBasedFeaturesDTO serviceBased, NetworkHostBasedFeaturesDTO hostBased, Collector<NetworkPacketFeaturesDTO> out) throws Exception {

        NetworkPacketFeaturesDTO networkPacketFeaturesSrctoDest = new NetworkPacketFeaturesDTO();

        networkPacketFeaturesSrctoDest.setHostBasedFeatures(new NetworkHostBasedFeaturesDTO());
        networkPacketFeaturesSrctoDest.setServiceBasedFeatures(new NetworkServiceBasedFeaturesDTO());

        networkPacketFeaturesSrctoDest.getHostBasedFeatures().setSourceIP(serviceBased.getSourceIP());
        networkPacketFeaturesSrctoDest.getHostBasedFeatures().setDestinationIP(serviceBased.getDestinationIP());

        networkPacketFeaturesSrctoDest.getServiceBasedFeatures().setSourceIP(serviceBased.getSourceIP());
        networkPacketFeaturesSrctoDest.getServiceBasedFeatures().setDestinationIP(serviceBased.getDestinationIP());
        networkPacketFeaturesSrctoDest.getServiceBasedFeatures().setSourcePort(serviceBased.getSourcePort());
        networkPacketFeaturesSrctoDest.getServiceBasedFeatures().setDestinationPort(serviceBased.getDestinationPort());

        networkPacketFeaturesSrctoDest.getServiceBasedFeatures().setCountSrctoDst(serviceBased.getCountSrctoDst());
        networkPacketFeaturesSrctoDest.getServiceBasedFeatures().setCountDsttoSrc(serviceBased.getCountDsttoSrc());
        networkPacketFeaturesSrctoDest.getServiceBasedFeatures().setNumBytesSrctoDst(serviceBased.getNumBytesSrctoDst());
        networkPacketFeaturesSrctoDest.getServiceBasedFeatures().setNumBytesDsttoSrc(serviceBased.getNumBytesDsttoSrc());

        networkPacketFeaturesSrctoDest.getHostBasedFeatures().setNetworkPacketFeaturesSrctoDst(hostBased.getNetworkPacketFeaturesSrctoDst());
        networkPacketFeaturesSrctoDest.getHostBasedFeatures().setNetworkPacketFeaturesDsttoSrc(hostBased.getNetworkPacketFeaturesDsttoSrc());

        if (!networkPacketFeaturesSrctoDest.getHostBasedFeatures().getSourceIP().equals("192.168.0.112")
                && !networkPacketFeaturesSrctoDest.getHostBasedFeatures().getSourceIP().equals("192.168.0.113")
                && !networkPacketFeaturesSrctoDest.getHostBasedFeatures().getSourceIP().equals("192.168.0.114")
                && !networkPacketFeaturesSrctoDest.getHostBasedFeatures().getSourceIP().equals("192.168.0.115")
                && !networkPacketFeaturesSrctoDest.getHostBasedFeatures().getSourceIP().equals("192.168.0.116")
                && !networkPacketFeaturesSrctoDest.getHostBasedFeatures().getSourceIP().equals("192.168.0.117")) {
            out.collect(networkPacketFeaturesSrctoDest);
        }

    }
}
