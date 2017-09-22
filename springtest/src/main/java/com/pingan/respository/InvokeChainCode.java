package com.pingan.respository;


import com.google.protobuf.InvalidProtocolBufferException;
import com.pingan.myutil.ClientHelper;
import com.pingan.myutil.Config;
import com.pingan.myutil.SampleOrg;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class InvokeChainCode {


	private static final ClientHelper clientHelper = new ClientHelper();
	private static final Config clientConfig = Config.getConfig();
	private static final Log logger = LogFactory.getLog(InvokeChainCode.class);
	private String[] args;
	private HFClient client;
	private Channel channel;
	private ChaincodeID chaincodeID;

	public InvokeChainCode(String[] args) throws CryptoException, InvalidArgumentException, NoSuchAlgorithmException,
			NoSuchProviderException, InvalidKeySpecException, TransactionException, IOException {
		this.args = args;
		this.client = clientHelper.getHFClient();
		this.channel = clientHelper.getChannel();
		this.chaincodeID = clientHelper.getChaincodeID();
		SampleOrg sampleOrg = clientHelper.getSamleOrg();
		//this.client.setUserContext(sampleOrg.getUser(TESTUSER_1_NAME));
		this.client.setUserContext(sampleOrg.getPeerAdmin()); // Maybe a bug of 1.0.0beta, only peer admin can call chaincode?
	}

	public String invoke() throws InvalidArgumentException, ProposalException, InvalidProtocolBufferException,
			UnsupportedEncodingException, InterruptedException, ExecutionException, TimeoutException {

		Collection<ProposalResponse> successful = new LinkedList<>();
		Collection<ProposalResponse> failed = new LinkedList<>();

		// Create instance of client.
		// HFClient client = clientHelper.getHFClient();

		// Create instance of channel.
		// Channel channel = clientHelper.getChannelWithPeerAdmin();

		// Create instance of ChaincodeID
		// ChaincodeID chaincodeID = clientHelper.getChaincodeID();

		///////////////
		/// Send transaction proposal to all peers
		TransactionProposalRequest transactionProposalRequest = client.newTransactionProposalRequest();
		transactionProposalRequest.setChaincodeID(chaincodeID);
		transactionProposalRequest.setFcn("invoke");
		transactionProposalRequest.setArgs(args);

		Map<String, byte[]> tm2 = new HashMap<>();
		tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
		tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
		tm2.put("result", ":)".getBytes(UTF_8)); /// This should be returned see
													/// chaincode.
		transactionProposalRequest.setTransientMap(tm2);

		System.out.println(
				"sending transactionProposal to all peers with arguments inspect(IMLC-000001,Chinasystem,China Ocean Shipping Company)");

		Collection<ProposalResponse> transactionPropResp = channel.sendTransactionProposal(transactionProposalRequest,
				channel.getPeers());
		for (ProposalResponse response : transactionPropResp) {
			if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
				// out("Successful transaction proposal response Txid: %s from
				// peer %s", response.getTransactionID(),
				// response.getPeer().getName());
				successful.add(response);
			} else {
				failed.add(response);
			}
		}

		// Check that all the proposals are consistent with each other. We
		// should have only one set
		// where all the proposals above are consistent.
		Collection<Set<ProposalResponse>> proposalConsistencySets = SDKUtils
				.getProposalConsistencySets(transactionPropResp);
		if (proposalConsistencySets.size() != 1) {
			System.out.println(
					"Expected only one set of consistent proposal responses but got " + proposalConsistencySets.size());
		}

		// out("Received %d transaction proposal responses. Successful+verified:
		// %d . Failed: %d",
		// transactionPropResp.size(), successful.size(), failed.size());
		if (failed.size() > 0) {
			ProposalResponse firstTransactionProposalResponse = failed.iterator().next();
			System.out.println("Not enough endorsers for inspect(IMLC-000001,Chinasystem,China Ocean Shipping Company):"
					+ failed.size() + " endorser error: " + firstTransactionProposalResponse.getMessage()
					+ ". Was verified: " + firstTransactionProposalResponse.isVerified());
		} else {
			System.out.println("Successfully received transaction proposal responses.");
		}
		ProposalResponse resp = transactionPropResp.iterator().next();
		byte[] x = resp.getChaincodeActionResponsePayload();
		String resultAsString = null;
		if (x != null) {
			resultAsString = new String(x, "UTF-8");
		}
		System.out.println(resultAsString);

		////////////////////////////
		// Send Transaction Transaction to orderer
		System.out.println(
				"Sending chain code transaction(inspect(IMLC-000001,Chinasystem,China Ocean Shipping Company)) to orderer.");
		System.out.println("sending transaction proposal to orderer");
		channel.sendTransaction(successful).thenApply(transactionEvent -> {
			if (transactionEvent.isValid()) {
				System.out.println("Successfully send transaction proposal to orderer. Transaction ID: "
						+ transactionEvent.getTransactionID());
			} else {
				System.out.println("Failed to send transaction proposal to orderer");
			}
			// chain.shutdown(true);
			return transactionEvent.getTransactionID();
		}).get(clientConfig.getTransactionWaitTime(), TimeUnit.SECONDS);
		return resultAsString;
	}

	public String  query() throws InvalidArgumentException, ProposalException {

		// Create instance of client.
		// HFClient client = clientHelper.getHFClient();

		// Create instance of channel.
		// Channel channel = clientHelper.getChannelWithPeerAdmin();

		// Create instance of ChaincodeID
		// ChaincodeID chaincodeID = clientHelper.getChaincodeID();

		QueryByChaincodeRequest queryByChaincodeRequest = client.newQueryProposalRequest();
		queryByChaincodeRequest.setArgs(args);
		queryByChaincodeRequest.setFcn("invoke");
		queryByChaincodeRequest.setChaincodeID(chaincodeID);

		Map<String, byte[]> tm2 = new HashMap<>();
		tm2.put("HyperLedgerFabric", "QueryByChaincodeRequest:JavaSDK".getBytes(UTF_8));
		tm2.put("method", "QueryByChaincodeRequest".getBytes(UTF_8));
		queryByChaincodeRequest.setTransientMap(tm2);
		String payload=null;
		Collection<ProposalResponse> queryProposals = channel.queryByChaincode(queryByChaincodeRequest,
				channel.getPeers());
		for (ProposalResponse proposalResponse : queryProposals) {
			if (!proposalResponse.isVerified() || proposalResponse.getStatus() != ProposalResponse.Status.SUCCESS) {
				System.out.println("Failed query proposal from peer " + proposalResponse.getPeer().getName() + " status: "
						+ proposalResponse.getStatus() + ". Messages: " + proposalResponse.getMessage()
						+ ". Was verified : " + proposalResponse.isVerified());
			} else {
				payload = proposalResponse.getProposalResponse().getResponse().getPayload().toStringUtf8();
				System.out.println("Query payload of IMLC-0001 from peer: " + proposalResponse.getPeer().getName());
				System.out.println("" + payload);
			}
		}
		return payload;

	}

}
