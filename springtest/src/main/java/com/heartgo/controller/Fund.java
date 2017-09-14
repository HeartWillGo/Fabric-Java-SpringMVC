package com.heartgo.controller;

public class Fund {
    public String CardID ;    //string `json:"cardid"` //银行卡id
    public int Amount;              //int    `json:"amount"` //卡上剩余金额

    public String getCardID() {
        return CardID;
    }

    public void setCardID(String cardID) {
        CardID = cardID;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }
}
