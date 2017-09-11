package com.heartgo.controller;

import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.*;

import java.util.Collection;

public class ClientBean
{
    private HFClient client;
    private Channel channel;
    private ChaincodeID chaincodeid;
    private SampleOrg sampleorgs;
    private RunChannel runchannel;
    public ClientBean(HFClient client,Channel channel,ChaincodeID chaincodeid,SampleOrg sampleorgs,RunChannel runchannel){
        this.chaincodeid=chaincodeid;
        this.channel=channel;
        this.client=client;
        this.runchannel=runchannel;
        this.sampleorgs=sampleorgs;
    }
    public void setClient(HFClient client){
        this.client=client;
    }
    public HFClient getClient(){
        return client;
    }
    public void setChannel(Channel channel){
        this.channel=channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChaincodeid(ChaincodeID chaincodeid){
        this.chaincodeid=chaincodeid;
    }
    public ChaincodeID getChaincodeid(){
        return chaincodeid;
    }
    public void setSampleorgs(SampleOrg sampleorgs){
        this.sampleorgs=sampleorgs;
    }
    public SampleOrg  getSampleorgs(){
        return sampleorgs;
    }
    public void setRunchannel(RunChannel runchannel){
        this.runchannel=runchannel;
    }
    public RunChannel getRunchannel() {
        return runchannel;
    }
}
