package com.pingan.respository;

import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.ChaincodeEvent;

public class ChaincodeEventCapture {
    final String handle;
    final BlockEvent blockEvent;
    final ChaincodeEvent chaincodeEvent;

    ChaincodeEventCapture(String handle, BlockEvent blockEvent, ChaincodeEvent chaincodeEvent) {
        this.handle = handle;
        this.blockEvent = blockEvent;
        this.chaincodeEvent = chaincodeEvent;
    }
}
