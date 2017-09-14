package com.heartgo.controller;

public class Oraganization {
    public String OrganizationID;   //string `json:"organizationid"`   //机构id
    public String  OrganizationName;  // string `json:"organizationname"` //机构名称
    public int OrganizationType;      //int    `json:"organizationtype"` //机构类型

    public String getOrganizationID() {
        return OrganizationID;
    }

    public void setOrganizationID(String organizationID) {
        OrganizationID = organizationID;
    }

    public String getOrganizationName() {
        return OrganizationName;
    }

    public void setOrganizationName(String organizationName) {
        OrganizationName = organizationName;
    }

    public int getOrganizationType() {
        return OrganizationType;
    }

    public void setOrganizationType(int organizationType) {
        OrganizationType = organizationType;
    }
}
