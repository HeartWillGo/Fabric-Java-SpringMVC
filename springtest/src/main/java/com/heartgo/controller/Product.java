package com.heartgo.controller;

public class Product {
    public String ProductID;            //产品id
    public String ProductName;      //产品名称
    public int ProductType;           //产品类型
    public String OrganizationID;      //产品所属机构id
    public int Portion;      //产品份额
    public int Price;        //单价

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getProductType() {
        return ProductType;
    }

    public void setProductType(int productType) {
        ProductType = productType;
    }

    public String getOrganizationID() {
        return OrganizationID;
    }

    public void setOrganizationID(String organizationID) {
        OrganizationID = organizationID;
    }

    public int getPortion() {
        return Portion;
    }

    public void setPortion(int portion) {
        Portion = portion;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }
}
