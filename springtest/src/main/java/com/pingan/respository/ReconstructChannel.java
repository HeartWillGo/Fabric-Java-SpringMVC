package com.pingan.respository;

import com.pingan.myutil.ClientHelper;
import com.pingan.myutil.Config;
import com.pingan.myutil.SampleOrg;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.fabric.protos.common.Configtx;
import org.hyperledger.fabric.protos.peer.Query;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import java.util.*;

import static java.lang.String.format;

public class ReconstructChannel {
    private static final ClientHelper clientHelper = new ClientHelper();

    private Config testConfig  = Config.getConfig();
    private  String TEST_FIXTURES_PATH = testConfig.TEST_FIXTURES_PATH;
    private  String FOO_CHANNEL_NAME = testConfig.FOO_CHANNEL_NAME;

    private static final Log logger = LogFactory.getLog(ConstructChannel.class);

    private static boolean checkInstalledChaincode(HFClient client, Peer peer, String ccName, String ccPath, String ccVersion) throws InvalidArgumentException, ProposalException {

        out("Checking installed chaincode: %s, at version: %s, on peer: %s", ccName, ccVersion, peer.getName());
        List<Query.ChaincodeInfo> ccinfoList = client.queryInstalledChaincodes(peer);

        boolean found = false;

        for (Query.ChaincodeInfo ccifo : ccinfoList) {

            found = ccName.equals(ccifo.getName()) && ccPath.equals(ccifo.getPath()) && ccVersion.equals(ccifo.getVersion());
            if (found) {
                break;
            }

        }

        return found;
    }
    private static boolean checkInstantiatedChaincode(Channel channel, Peer peer, String ccName, String ccPath, String ccVersion) throws InvalidArgumentException, ProposalException {
        out("Checking instantiated chaincode: %s, at version: %s, on peer: %s", ccName, ccVersion, peer.getName());
        List<Query.ChaincodeInfo> ccinfoList = channel.queryInstantiatedChaincodes(peer);

        boolean found = false;

        for (Query.ChaincodeInfo ccifo : ccinfoList) {
            found = ccName.equals(ccifo.getName()) && ccPath.equals(ccifo.getPath()) && ccVersion.equals(ccifo.getVersion());
            if (found) {
                break;
            }

        }

        return found;
    }

    public  void reconstructchannel() throws Exception {

        // Get Org1
        SampleOrg sampleOrg = clientHelper.getSamleOrg();

        // Create instance of client.
        HFClient client = clientHelper.getHFClient();

        // Only peer Admin org
        client.setUserContext(sampleOrg.getPeerAdmin());
        Channel newChannel = client.newChannel(FOO_CHANNEL_NAME);

        for (String orderName : sampleOrg.getOrdererNames()) {
            newChannel.addOrderer(client.newOrderer(orderName, sampleOrg.getOrdererLocation(orderName),
                    testConfig.getOrdererProperties(orderName)));
        }

        for (String peerName : sampleOrg.getPeerNames()) {
            String peerLocation = sampleOrg.getPeerLocation(peerName);
            Peer peer = client.newPeer(peerName, peerLocation, testConfig.getPeerProperties(peerName));

            //Query the actual peer for which channels it belongs to and check it belongs to this channel
            Set<String> channels = client.queryChannels(peer);
            if (!channels.contains(FOO_CHANNEL_NAME)) {
                throw new AssertionError(format("Peer %s does not appear to belong to channel %s", peerName, FOO_CHANNEL_NAME));
            }

            newChannel.addPeer(peer);
            sampleOrg.addPeer(peer);
        }

        for (String eventHubName : sampleOrg.getEventHubNames()) {
            EventHub eventHub = client.newEventHub(eventHubName, sampleOrg.getEventHubLocation(eventHubName),
                    testConfig.getEventHubProperties(eventHubName));
            newChannel.addEventHub(eventHub);
        }

        newChannel.initialize();

        //Just see if we can get channelConfiguration. Not required for the rest of scenario but should work.
        final byte[] channelConfigurationBytes = newChannel.getChannelConfigurationBytes();
        Configtx.Config channelConfig = Configtx.Config.parseFrom(channelConfigurationBytes);
        //  assertNotNull(channelConfig);
        Configtx.ConfigGroup channelGroup = channelConfig.getChannelGroup();
        // assertNotNull(channelGroup);
        Map<String, Configtx.ConfigGroup> groupsMap = channelGroup.getGroupsMap();
//        assertNotNull(groupsMap.get("Orderer"));
//        assertNotNull(groupsMap.get("Application"));

        //Before return lets see if we have the chaincode on the peers that we expect from End2endIT
        //And if they were instantiated too.

        for (Peer peer : newChannel.getPeers()) {

            if (!checkInstalledChaincode(client, peer, testConfig.CHAIN_CODE_NAME, testConfig.CHAIN_CODE_PATH, testConfig.CHAIN_CODE_VERSION)) {
                System.out.println(format("Peer %s is missing chaincode name: %s, path:%s, version: %s",
                        peer.getName(), testConfig.CHAIN_CODE_NAME, testConfig.CHAIN_CODE_PATH,testConfig. CHAIN_CODE_PATH));
            }

            if (!checkInstantiatedChaincode(newChannel, peer, testConfig.CHAIN_CODE_NAME,testConfig. CHAIN_CODE_PATH, testConfig.CHAIN_CODE_VERSION)) {

                System.out.println(format("Peer %s is missing instantiated chaincode name: %s, path:%s, version: %s",
                        peer.getName(), testConfig.CHAIN_CODE_NAME, testConfig.CHAIN_CODE_PATH,testConfig. CHAIN_CODE_PATH));
            }

        }




    }
    static void out(String format, Object... args) {

        System.err.flush();
        System.out.flush();

        System.out.println(format(format, args));
        System.err.flush();
        System.out.flush();

    }

}
