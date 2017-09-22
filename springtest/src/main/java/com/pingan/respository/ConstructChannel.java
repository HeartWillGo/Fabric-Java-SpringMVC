package com.pingan.respository;


import com.pingan.myutil.ClientHelper;
import com.pingan.myutil.Config;
import com.pingan.myutil.SampleOrg;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.fabric.sdk.*;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Properties;

public class ConstructChannel {

	private static final ClientHelper clientHelper = new ClientHelper();

	private Config testConfig  = Config.getConfig();
	private  String TEST_FIXTURES_PATH = testConfig.TEST_FIXTURES_PATH;
	private  String FOO_CHANNEL_NAME = testConfig.FOO_CHANNEL_NAME;

	private static final Log logger = LogFactory.getLog(ConstructChannel.class);

	public  void constructchannel() throws Exception {

		// Get Org1
		SampleOrg sampleOrg = clientHelper.getSamleOrg();

		// Create instance of client.
		HFClient client = clientHelper.getHFClient();

		// Only peer Admin org
		client.setUserContext(sampleOrg.getPeerAdmin());
		// Begin construction
		System.out.println("Constructing channel " + FOO_CHANNEL_NAME);

		Collection<Orderer> orderers = new LinkedList<>();

		for (String orderName : sampleOrg.getOrdererNames()) {
			orderers.add(client.newOrderer(orderName, sampleOrg.getOrdererLocation(orderName),
					testConfig.getOrdererProperties(orderName)));
		}

		// Just pick the first orderer in the list to create the channel.
		Orderer anOrderer = orderers.iterator().next();
		orderers.remove(anOrderer);

		ChannelConfiguration chainConfiguration = new ChannelConfiguration(
				new File(TEST_FIXTURES_PATH + "/sdkintegration/e2e-2Orgs/channel/" + FOO_CHANNEL_NAME + ".tx"));


		// Create channel that has only one signer that is this orgs peer admin.
		// If channel creation policy needed more signature they would need to
		// be
		// added too.
		Channel newChannel = client.newChannel(FOO_CHANNEL_NAME, anOrderer, chainConfiguration,
				client.getChannelConfigurationSignature(chainConfiguration, sampleOrg.getPeerAdmin()));

		System.out.println("Created chain " + FOO_CHANNEL_NAME);

		for (String peerName : sampleOrg.getPeerNames()) {
			String peerLocation = sampleOrg.getPeerLocation(peerName);

			Properties peerProperties = testConfig.getPeerProperties(peerName);// test
																					// properties
																					// for
																					// peer..
																					// if
																					// any.
			if (peerProperties == null) {
				peerProperties = new Properties();
			}
			// Example of setting specific options on grpc's
			// ManagedChannelBuilder
			peerProperties.put("grpc.ManagedChannelBuilderOption.maxInboundMessageSize", 9000000);

			Peer peer = client.newPeer(peerName, peerLocation, peerProperties);
			newChannel.joinPeer(peer);
			System.out.println("Peer " + peerName + "joined channel " + FOO_CHANNEL_NAME);
			sampleOrg.addPeer(peer);
		}

		for (Orderer orderer : orderers) { // add remaining orderers if any.
			newChannel.addOrderer(orderer);
		}

		for (String eventHubName : sampleOrg.getEventHubNames()) {
			EventHub eventHub = client.newEventHub(eventHubName, sampleOrg.getEventHubLocation(eventHubName),
					testConfig.getEventHubProperties(eventHubName));
			newChannel.addEventHub(eventHub);
		}

		newChannel.initialize();

		System.out.println("Finished initialization channel " + FOO_CHANNEL_NAME);



	}
}
