package com.heartgo.controller;


import org.apache.commons.codec.binary.Hex;
import com.heartgo.myutil.ConfigHelper;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionEventException;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

public class RunChannel {
    private   Config testConfig  = Config.getConfig();
    public    Collection<ProposalResponse> responses;
    public   Collection<ProposalResponse> successful = new LinkedList<>();
    public   Collection<ProposalResponse> failed = new LinkedList<>();
    Vector<ChaincodeEventCapture> chaincodeEvents = new Vector<>();

    boolean isFooChain;

    private   final ConfigHelper configHelper = new ConfigHelper();

    ////////////////////////////
    // Install Proposal Request
    //
    public void Inatall(HFClient client,  Channel channel,SampleOrg sampleOrg,ChaincodeID  chaincodeID  ){
         // Test list to capture chaincode events.

        final String channelName = channel.getName();
        System.out.println("channel.getName():"+channel.getName());

        isFooChain = testConfig.FOO_CHANNEL_NAME.equals(channelName);
        System.out.println("isFooChain:"+isFooChain);

        out("Running channel %s", channelName);
        channel.setTransactionWaitTime(testConfig.getTransactionWaitTime());
        channel.setDeployWaitTime(testConfig.getDeployWaitTime());
        System.out.println(" Transaction wait time :"+channel.getDeployWaitTime()+" DeployWaitTime :"+channel.getDeployWaitTime());

        Collection<Orderer> orderers = channel.getOrderers();
        System.out.println("orderers size:"+orderers.size());

        // Register a chaincode event listener that will trigger for any chaincode id and only for EXPECTED_EVENT_NAME event.
        try {
            String chaincodeEventListenerHandle = channel.registerChaincodeEventListener(Pattern.compile(".*"),
                    Pattern.compile(Pattern.quote(testConfig.EXPECTED_EVENT_NAME)),
                    (handle, blockEvent, chaincodeEvent) -> {

                        chaincodeEvents.add(new ChaincodeEventCapture(handle, blockEvent, chaincodeEvent));

                        out("RECEIVED Chaincode event with handle: %s, chhaincode Id: %s, chaincode event name: %s, "
                                        + "transaction id: %s, event payload: \"%s\", from eventhub: %s",
                                handle, chaincodeEvent.getChaincodeId(),
                                chaincodeEvent.getEventName(), chaincodeEvent.getTxId(),
                                new String(chaincodeEvent.getPayload()), blockEvent.getEventHub().toString());

                    });


        //For non foo channel unregister event listener to test events are not called.
            if (!isFooChain) {
                channel.unRegisterChaincodeEventListener(chaincodeEventListenerHandle);
                chaincodeEventListenerHandle = null;

            }
        }catch (InvalidArgumentException e){
            e.printStackTrace();
        }



        try {
            System.out.println("we are installChaincode");
            client.setUserContext(sampleOrg.getPeerAdmin());
            System.out.println("client getUserContext:" + client.getUserContext());
            out("Creating install proposal");

            InstallProposalRequest installProposalRequest = client.newInstallProposalRequest();
            installProposalRequest.setChaincodeID(chaincodeID);
            System.out.println("installProposalRequest:" + installProposalRequest);

            if (isFooChain) {
                // on foo chain install from directory.
                System.out.println("isFooChain");

                ////For GO language and serving just a single user, chaincodeSource is mostly likely the users GOPATH
                installProposalRequest.setChaincodeSourceLocation(new File(testConfig.TEST_FIXTURES_PATH + "/sdkintegration/gocc/sample1"));
            } else {
                // On bar chain install from an input stream.
                System.out.println("not isFooChain");
                String str = testConfig.TEST_FIXTURES_PATH + "/sdkintegration/gocc/sample1/" + "src/" + testConfig.CHAIN_CODE_PATH;
                System.out.println("run channel str" + str);
                File file = new File(str);
                installProposalRequest.setChaincodeInputStream(Util.generateTarGzInputStream(
                        file,
                        Paths.get("src", testConfig.CHAIN_CODE_PATH).toString()));
//                    System.out.println("path:"+Paths.get(TEST_FIXTURES_PATH, "/sdkintegration/gocc/sample1", "src", CHAIN_CODE_PATH).toFile()+
//                            Paths.get("src", CHAIN_CODE_PATH).toString());
            }

            installProposalRequest.setChaincodeVersion(testConfig.CHAIN_CODE_VERSION);

            out("Sending install proposal");

            ////////////////////////////
            // only a client from the same org as the peer can issue an install request
            int numInstallProposal = 0;
            //    Set<String> orgs = orgPeers.keySet();
            //   for (SampleOrg org : testSampleOrgs) {

            Set<Peer> peersFromOrg = sampleOrg.getPeers();
            numInstallProposal = numInstallProposal + peersFromOrg.size();
            responses = client.sendInstallProposal(installProposalRequest, peersFromOrg);

            for (ProposalResponse response : responses) {
                System.out.println("response.getStatus():" + response.getStatus());
                System.out.println("response :" + response.toString());
                if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
                    out("Successful install proposal response Txid: %s from peer %s", response.getTransactionID(), response.getPeer().getName());
                    successful.add(response);
                } else {
                    failed.add(response);
                }
            }

            //   }
            out("Received %d install proposal responses. Successful+verified: %d . Failed: %d", numInstallProposal, successful.size(), failed.size());

            if (failed.size() > 0) {
                System.out.println("failed size:" + failed.size());
                ProposalResponse first = failed.iterator().next();
                //fail("Not enough endorsers for install :" + successful.size() + ".  " + first.getMessage());
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    //// Instantiate chaincode.
    public   void Instantiate(HFClient client, ChaincodeID chaincodeID, Channel channel ){
        System.out.println("we are Instantiate chaincode now");
        try {
            InstantiateProposalRequest instantiateProposalRequest = client.newInstantiationProposalRequest();


            instantiateProposalRequest.setProposalWaitTime(testConfig.getProposalWaitTime());
            instantiateProposalRequest.setChaincodeID(chaincodeID);
            instantiateProposalRequest.setFcn("init");
            instantiateProposalRequest.setArgs(new String[]{"a", "600", "b","600"});
            Map<String, byte[]> tm = new HashMap<>();
            tm.put("HyperLedgerFabric", "InstantiateProposalRequest:JavaSDK".getBytes(UTF_8));
            tm.put("method", "InstantiateProposalRequest".getBytes(UTF_8));
            instantiateProposalRequest.setTransientMap(tm);

            /*
              policy OR(Org1MSP.member, Org2MSP.member) meaning 1 signature from someone in either Org1 or Org2
              See README.md Chaincode endorsement policies section for more details.
            */
            ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();

            chaincodeEndorsementPolicy.fromYamlFile(new File(testConfig.TEST_FIXTURES_PATH + "/sdkintegration/chaincodeendorsementpolicy.yaml"));
             instantiateProposalRequest.setChaincodeEndorsementPolicy(chaincodeEndorsementPolicy);
             successful.clear();
            failed.clear();

            if (isFooChain) {  //Send responses both ways with specifying peers and by using those on the channel.
                responses = channel.sendInstantiationProposal(instantiateProposalRequest, channel.getPeers());
            } else {
                responses = channel.sendInstantiationProposal(instantiateProposalRequest);

            }
            for (ProposalResponse response : responses) {
                if (response.isVerified() && response.getStatus() == ProposalResponse.Status.SUCCESS) {
                    successful.add(response);
                    out("Succesful instantiate proposal response Txid: %s from peer %s", response.getTransactionID(), response.getPeer().getName());
                } else {
                    failed.add(response);
                }
            }
            out("Received %d instantiate proposal responses. Successful+verified: %d . Failed: %d", responses.size(), successful.size(), failed.size());
            if (failed.size() > 0) {
                ProposalResponse first = failed.iterator().next();
                //fail("Not enough endorsers for instantiate :" + successful.size() + "endorser failed with " + first.getMessage() + ". Was verified:" + first.isVerified());
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    /// Send transaction proposal to all peers
    public  void SendtTansactionToPeers(HFClient client, Channel channel,ChaincodeID chaincodeID,SampleOrg sampleOrg,String[] transaction) {
        try {

            Collection<Orderer> orderers = channel.getOrderers();
            channel.sendTransaction(successful, orderers).thenApply(transactionEvent -> {
                System.out.println("is Valid():" + transactionEvent.isValid());
                System.out.println("Finished instantiate transaction with transaction id %s" + transactionEvent.getTransactionID());
                try {
                    successful.clear();
                    failed.clear();
                    client.setUserContext(sampleOrg.getUser(Config.TESTUSER_1_NAME));
                    TransactionProposalRequest transactionProposalRequest = client.newTransactionProposalRequest();
                    transactionProposalRequest.setChaincodeID(chaincodeID);
                    transactionProposalRequest.setFcn("invoke");
                    transactionProposalRequest.setProposalWaitTime(testConfig.getProposalWaitTime());
                    transactionProposalRequest.setArgs(transaction);

                    Map<String, byte[]> tm2 = new HashMap<>();
                    tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8)); //Just some extra junk in transient map
                    tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8)); // ditto
                    tm2.put("result", ":)".getBytes(UTF_8));  // This should be returned see chaincode why.
                    tm2.put(testConfig.EXPECTED_EVENT_NAME, testConfig.EXPECTED_EVENT_DATA);  //This should trigger an event see chaincode why.

                    transactionProposalRequest.setTransientMap(tm2);

                    out("sending transactionProposal to all peers with arguments: move(a,b,100)");
                    try {
                        Collection<ProposalResponse> transactionPropResp = channel.sendTransactionProposal(transactionProposalRequest, channel.getPeers());

                        for (ProposalResponse response : transactionPropResp) {
                            System.out.println("response.getStatus():" + response.getStatus());
                            if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
                                out("Successful transaction proposal response Txid: %s from peer %s", response.getTransactionID(), response.getPeer().getName());
                                successful.add(response);
                            } else {
                                failed.add(response);
                            }
                        }


                        // Check that all the proposals are consistent with each other. We should have only one set
                        // where all the proposals above are consistent. Note the when sending to Orderer this is done automatically.
                        //  Shown here as an example that applications can invoke and select.
                        // See org.hyperledger.fabric.sdk.proposal.consistency_validation config property.
                        Collection<Set<ProposalResponse>> proposalConsistencySets = SDKUtils.getProposalConsistencySets(transactionPropResp);
                        if (proposalConsistencySets.size() != 1) {
                            //fail(format("Expected only one set of consistent proposal responses but got %d", proposalConsistencySets.size()));
                        }

                        out("Received %d transaction proposal responses. Successful+verified: %d . Failed: %d",
                                transactionPropResp.size(), successful.size(), failed.size());
                        if (failed.size() > 0) {
                            ProposalResponse firstTransactionProposalResponse = failed.iterator().next();
                            //                        fail("Not enough endorsers for invoke(move a,b,100):" + failed.size() + " endorser error: " +
                            //                                firstTransactionProposalResponse.getMessage() +
                            //                                ". Was verified: " + firstTransactionProposalResponse.isVerified());
                        }
                        out("Successfully received transaction proposal responses.");

                        ProposalResponse resp = transactionPropResp.iterator().next();
                        byte[] x = resp.getChaincodeActionResponsePayload(); // This is the data returned by the chaincode.
                        String resultAsString = null;
                        try {
                            if (x != null) {
                                resultAsString = new String(x, "UTF-8");
                            }

                            System.out.println("resultAsString:" + resultAsString);
                        }catch (UnsupportedEncodingException e){
                            e.printStackTrace();
                        }

                        //
                        System.out.println("response status:" + resp.getChaincodeActionResponseStatus()); //Chaincode's status.

                        TxReadWriteSetInfo readWriteSetInfo = resp.getChaincodeActionResponseReadWriteSetInfo();
                        System.out.println("readWriteSetInfo:" + readWriteSetInfo);
                        //See blockwalker below how to transverse this
                        //                    assertNotNull(readWriteSetInfo);
                        //                    assertTrue(readWriteSetInfo.getNsRwsetCount() > 0);

                        ChaincodeID cid = resp.getChaincodeID();
                        //                    assertNotNull(cid);
                        //                    assertEquals(CHAIN_CODE_PATH, cid.getPath());
                        //                    assertEquals(CHAIN_CODE_NAME, cid.getName());
                        //                    assertEquals(CHAIN_CODE_VERSION, cid.getVersion());
                    } catch (ProposalException e) {
                        e.printStackTrace();
                    }
                } catch (InvalidArgumentException e) {
                    e.printStackTrace();
                }
                return null;
            }).exceptionally(e -> {
                if (e instanceof TransactionEventException) {
                    BlockEvent.TransactionEvent te = ((TransactionEventException) e).getTransactionEvent();
                    if (te != null) {
                        System.out.println(format("Transaction with txid %s failed. %s", te.getTransactionID(), e.getMessage()));
                    }
                }
                System.out.println(format("Test failed with %s exception %s", e.getClass().getName(), e.getMessage()));

                return null;
            }).get(testConfig.getTransactionWaitTime(), TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    ////////////////////////////
    // Send Query Proposal to all peers
    //
    public static void SendQuryToPeers(HFClient client, Channel channel,ChaincodeID chaincodeID,String[] query){
        try {

            QueryByChaincodeRequest queryByChaincodeRequest = client.newQueryProposalRequest();
            queryByChaincodeRequest.setArgs(query);
            queryByChaincodeRequest.setFcn("invoke");
            queryByChaincodeRequest.setChaincodeID(chaincodeID);

            Map<String, byte[]> tm2 = new HashMap<>();
            tm2.put("HyperLedgerFabric", "QueryByChaincodeRequest:JavaSDK".getBytes(UTF_8));
            tm2.put("method", "QueryByChaincodeRequest".getBytes(UTF_8));
            queryByChaincodeRequest.setTransientMap(tm2);

            Collection<ProposalResponse> queryProposals = channel.queryByChaincode(queryByChaincodeRequest, channel.getPeers());
            for (ProposalResponse proposalResponse : queryProposals) {
                if (!proposalResponse.isVerified() || proposalResponse.getStatus() != ProposalResponse.Status.SUCCESS) {
//                            fail("Failed query proposal from peer " + proposalResponse.getPeer().getName() + " status: " + proposalResponse.getStatus() +
//                                    ". Messages: " + proposalResponse.getMessage()
//                                    + ". Was verified : " + proposalResponse.isVerified());
                } else {
                    String payload = proposalResponse.getProposalResponse().getResponse().getPayload().toStringUtf8();
                    out("Query payload of b from peer %s returned %s", proposalResponse.getPeer().getName(), payload);
                    //  assertEquals(payload, expect);
                }
            }
        }catch (Exception e){
            e.printStackTrace();

        }

    }

    // Channel queries

    public   void ChannelQueries(Channel channel, SampleOrg sampleOrg){
        // We can only send channel queries to peers that are in the same org as the SDK user context
        // Get the peers from the current org being used and pick one randomly to send the queries to.
        Set<Peer> peerSet = sampleOrg.getPeers();
        //  Peer queryPeer = peerSet.iterator().next();
        //   out("Using peer %s for channel queries", queryPeer.getName());
        try {

            BlockchainInfo channelInfo = channel.queryBlockchainInfo();
            out("Channel info for : " + channel);
            out("Channel height: " + channelInfo.getHeight());
            String chainCurrentHash = Hex.encodeHexString(channelInfo.getCurrentBlockHash());
            String chainPreviousHash = Hex.encodeHexString(channelInfo.getPreviousBlockHash());
            out("Chain current block hash: " + chainCurrentHash);
            out("Chainl previous block hash: " + chainPreviousHash);

            // Query by block number. Should return latest block, i.e. block number 2
            BlockInfo returnedBlock = channel.queryBlockByNumber(channelInfo.getHeight() - 1);
            String previousHash = Hex.encodeHexString(returnedBlock.getPreviousHash());
            out("queryBlockByNumber returned correct block with blockNumber " + returnedBlock.getBlockNumber()
                    + " \n previous_hash " + previousHash);
//            assertEquals(channelInfo.getHeight() - 1, returnedBlock.getBlockNumber());
//            assertEquals(chainPreviousHash, previousHash);

            // Query by block hash. Using latest block's previous hash so should return block number 1
            byte[] hashQuery = returnedBlock.getPreviousHash();
            returnedBlock = channel.queryBlockByHash(hashQuery);
            out("queryBlockByHash returned block with blockNumber " + returnedBlock.getBlockNumber());
            //  assertEquals(channelInfo.getHeight() - 2, returnedBlock.getBlockNumber());

            // Query block by TxID. Since it's the last TxID, should be block 2
            returnedBlock = channel.queryBlockByTransactionID(testConfig.testTxID);
            out("queryBlockByTxID returned block with blockNumber " + returnedBlock.getBlockNumber());
//            assertEquals(channelInfo.getHeight() - 1, returnedBlock.getBlockNumber());
        }catch (Exception e){
            e.printStackTrace();
        }



    }
    // query transaction by ID
    public  TransactionInfo QueryTransaction(Channel channel){
        TransactionInfo txInfo=null;
        try {
            txInfo= channel.queryTransactionByID(testConfig.testTxID);
            out("QueryTransactionByID returned TransactionInfo: txID " + txInfo.getTransactionID()
                    + "\n     validation code " + txInfo.getValidationCode().getNumber());
        }catch (Exception e){
            e.printStackTrace();
        }
        return txInfo;



    }
    //CHECKSTYLE.OFF: Method length is 320 lines (max allowed is 150).
    public  void runChannel(HFClient client, Channel channel, boolean installChaincode, SampleOrg sampleOrg, int delta) {



        try {


            //   client.setUserContext(sampleOrg.getUser(TEST_ADMIN_NAME));
            //  final ChaincodeID chaincodeID = firstInstallProposalResponse.getChaincodeID();
            // Note installing chaincode does not require transaction no need to
            // send to Orderers

            ///////////////

            //// Instantiate chaincode.



         //   Instantiate(client,chaincodeID,channel);

            ///////////////
            /// Send instantiate transaction to orderer



//            out("Sending instantiateTransaction to orderer with a and b set to 100 and %s respectively", "" + (200 + delta));
//            channel.sendTransaction(successful, orderers).thenApply(transactionEvent -> {
//
//                //waitOnFabric(0);
//
//                // assertTrue(transactionEvent.isValid()); // must be valid to be here.
//                out("Finished instantiate transaction with transaction id %s", transactionEvent.getTransactionID());

            try {
                successful.clear();
                failed.clear();

                client.setUserContext(sampleOrg.getUser(testConfig.TESTUSER_1_NAME));

                ///////////////
                /// Send transaction proposal to all peers

               // SendtTansactionToPeers(client,channel,chaincodeID);
                ////////////////////////////
                // Send Transaction Transaction to orderer
                out("Sending chaincode transaction(move a,b,100) to orderer.");
               // return channel.sendTransaction(successful).get(testConfig.getTransactionWaitTime(), TimeUnit.SECONDS);

            } catch (Exception e) {
                out("Caught an exception while invoking chaincode");
                e.printStackTrace();
                //     fail("Failed invoking chaincode with error : " + e.getMessage());
            }

              //  return null;

        //    }).thenApply(transactionEvent -> {
            try {

              //  waitOnFabric(0);

                //   assertTrue(transactionEvent.isValid()); // must be valid to be here.
//                    out("Finished transaction with transaction id %s", transactionEvent.getTransactionID());
//                    testConfig.testTxID = transactionEvent.getTransactionID(); // used in the channel queries later

                ////////////////////////////
                // Send Query Proposal to all peers
                //
             //   SendQuryToPeers(client,channel,chaincodeID,delta);

               // return null;
            } catch (Exception e) {
                out("Caught exception while running query");
                e.printStackTrace();
                //   fail("Failed during chaincode query with error : " + e.getMessage());
            }

            //    return null;
//            }).exceptionally(e -> {
//                if (e instanceof TransactionEventException) {
//                    BlockEvent.TransactionEvent te = ((TransactionEventException) e).getTransactionEvent();
//                    if (te != null) {
//                        //  fail(format("Transaction with txid %s failed. %s", te.getTransactionID(), e.getMessage()));
//                    }
//                }
//                //fail(format("Test failed with %s exception %s", e.getClass().getName(), e.getMessage()));
//
//                return null;
//            }).get(testConfig.getTransactionWaitTime(), TimeUnit.SECONDS);

            // Channel queries
            ChannelQueries(channel,sampleOrg);



            // query transaction by ID
            QueryTransaction(channel);
//            if (chaincodeEventListenerHandle != null) {
//
//                channel.unRegisterChaincodeEventListener(chaincodeEventListenerHandle);
//                //Should be two. One event in chaincode and two notification for each of the two event hubs
//
//                final int numberEventHubs = channel.getEventHubs().size();
//                //just make sure we get the notifications.
//                for (int i = 15; i > 0; --i) {
//                    if (chaincodeEvents.size() == numberEventHubs) {
//                        break;
//                    } else {
//                        Thread.sleep(90); // wait for the events.
//                    }
//
//                }
//                //   assertEquals(numberEventHubs, chaincodeEvents.size());
//
//                for (ChaincodeEventCapture chaincodeEventCapture : chaincodeEvents) {
////                    assertEquals(chaincodeEventListenerHandle, chaincodeEventCapture.handle);
////                    assertEquals(testTxID, chaincodeEventCapture.chaincodeEvent.getTxId());
////                    assertEquals(EXPECTED_EVENT_NAME, chaincodeEventCapture.chaincodeEvent.getEventName());
////                    assertTrue(Arrays.equals(EXPECTED_EVENT_DATA, chaincodeEventCapture.chaincodeEvent.getPayload()));
////                    assertEquals(CHAIN_CODE_NAME, chaincodeEventCapture.chaincodeEvent.getChaincodeId());
//
//                    BlockEvent blockEvent = chaincodeEventCapture.blockEvent;
////                    assertEquals(channelName, blockEvent.getChannelId());
////                    assertTrue(channel.getEventHubs().contains(blockEvent.getEventHub()));
//
//                }
//
//            } else {
//                //   assertTrue(chaincodeEvents.isEmpty());
//            }

          //  out("Running for Channel %s done", channelName);

        } catch (Exception e) {
            out("Caught an exception running channel %s", channel.getName());
            e.printStackTrace();
            //    fail("Test failed with error : " + e.getMessage());
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
