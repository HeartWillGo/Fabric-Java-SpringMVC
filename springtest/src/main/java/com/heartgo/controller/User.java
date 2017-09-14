package com.heartgo.controller;

import java.util.HashMap;

public class User {
    public String ID;                    //用户ID
    public String Name;               //用户名字
    public int IdentificationType; // 证件类型
    public String Identification;               //证件号码
    public int  Sex;           //性别
    public String Birthday;      //生日
    public String BankCard;          //银行卡号
    public String PhoneNumber;        //手机号

    public HashMap<String ,Product> ProductMap; //产品

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getIdentificationType() {
        return IdentificationType;
    }

    public void setIdentificationType(int identificationType) {
        IdentificationType = identificationType;
    }

    public String getIdentification() {
        return Identification;
    }

    public void setIdentification(String identification) {
        Identification = identification;
    }

    public int getSex() {
        return Sex;
    }

    public void setSex(int sex) {
        Sex = sex;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getBankCard() {
        return BankCard;
    }

    public void setBankCard(String bankCard) {
        BankCard = bankCard;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public HashMap<String, Product> getProductMap() {
        return ProductMap;
    }

    public void setProductMap(HashMap<String, Product> productMap) {
        ProductMap = productMap;
    }


}
