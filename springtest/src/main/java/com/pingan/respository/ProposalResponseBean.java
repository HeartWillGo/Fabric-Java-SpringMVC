package com.pingan.respository;

import org.hyperledger.fabric.sdk.ProposalResponse;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Vector;

public class ProposalResponseBean {
    public  static Collection<ProposalResponse> responses = new LinkedList<>();;
    public  static  Collection<ProposalResponse> successful = new LinkedList<>();
    public  static  Collection<ProposalResponse> failed = new LinkedList<>();
    public static Vector<ChaincodeEventCapture> chaincodeEvents = new Vector<>();
}
