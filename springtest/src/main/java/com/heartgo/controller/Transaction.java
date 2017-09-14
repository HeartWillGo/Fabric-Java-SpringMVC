package com.heartgo.controller;

public class Transaction {
    public String TransId;       //string `json:"id"`         //交易id
    public int  TransType;       //int    `json:"transtype"`  //交易类型 0 表示申购，1，表示赎回， 2，表示入金
    public int FromType;         //int    `json:"fromtype"`   //发送方角色
    public String  FromID ;      //string `json:"fromid"`     //发送方 ID
    public int  ToType ;         //int    `json:"totype"`     //接收方角色
    public String ToID;          //string `json:"toid"`       //接收方 ID
    public String  Time;         // string `json:"time"`       //交易时间
    public String  ProductID;    //string `json:"productid"`  //交易产品id
    public int Account;          //int    `json:"account"`    //交易 份额
    public int Price;            //int    `json:"price"`      //交易价格
    public String  ParentOrderNo;            //string `json:parentorder"` //父订单号

    public String getTransId() {
        return TransId;
    }

    public void setTransId(String transId) {
        TransId = transId;
    }

    public int getTransType() {
        return TransType;
    }

    public void setTransType(int transType) {
        TransType = transType;
    }

    public int getFromType() {
        return FromType;
    }

    public void setFromType(int fromType) {
        FromType = fromType;
    }

    public String getFromID() {
        return FromID;
    }

    public void setFromID(String fromID) {
        FromID = fromID;
    }

    public int getToType() {
        return ToType;
    }

    public void setToType(int toType) {
        ToType = toType;
    }

    public String getToID() {
        return ToID;
    }

    public void setToID(String toID) {
        ToID = toID;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public int getAccount() {
        return Account;
    }

    public void setAccount(int account) {
        Account = account;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getParentOrderNo() {
        return ParentOrderNo;
    }

    public void setParentOrderNo(String parentOrderNo) {
        ParentOrderNo = parentOrderNo;
    }


}
