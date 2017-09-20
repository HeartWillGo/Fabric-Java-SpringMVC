package main

import (
	"encoding/json"
	"fmt"
	"strconv"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)
//createProduct 创建产品
func (t *SimpleChaincode) CreateProduct(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("ex02 CreateProduct")

	var ProductID string      //产品id
	var ProductName int       //产品名称
	var ProductType int       //产品类型
	var OrganizationID string //产品所属机构id
	var Portion int           //产品份额
	var Price int             //价格
	var product Product

	if len(args) != 7 {
		return shim.Error("Incorrect number of arguments. Expecting 6")
	}

	ProductID = args[1]
	var err error
	ProductName, err = strconv.Atoi(args[2])
	if err != nil {
		return shim.Error("Expecting integer value for asset holding：Number ")
	}
	ProductType, err = strconv.Atoi(args[3])
	if err != nil {
		return shim.Error("Expecting integer value for asset holding：Number ")
	}
	OrganizationID = args[4]
	Portion, err = strconv.Atoi(args[5])
	if err != nil {
		return shim.Error("Expecting integer value for asset holding：Number ")
	}
	Price, err = strconv.Atoi(args[6])
	if err != nil {
		return shim.Error("Expecting integer value for asset holding：Number ")
	}
	product.ProductID = ProductID
	product.ProductName = ProductName
	product.ProductType = ProductType
	product.OrganizationID = OrganizationID
	product.Portion = Portion
	product.Price = Price

	jsons_product, err := json.Marshal(product) //转换成JSON返回的是byte[]
	if err != nil {
		return shim.Error(err.Error())
	}

	productinfo, err := stub.GetState(ProductID)
	if err != nil {
		return shim.Error("failed to get productID")
	} else if productinfo != nil {
		return shim.Error(string(productinfo) + "\t already exists")
	}

	// Write the state to the ledger
	err = stub.PutState(args[1], jsons_product)
	if err != nil {
		return shim.Error(err.Error())
	}


	return shim.Success(nil)
}
//getProduct 获取产品信息
func (t *SimpleChaincode) getProduct(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("ex02 getProduct")

	var Product_ID string //产品ID
	var product Product
	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}
	Product_ID = args[1]

	ProductInfo, err := stub.GetState(Product_ID)
	if err != nil {
		return shim.Error(err.Error())
	}
	//将byte的结果转换成struct
	err = json.Unmarshal(ProductInfo, &product)
	if err != nil {
		return shim.Error(err.Error())
	}
	fmt.Printf("  ProductInfo  = %d  \n", ProductInfo)
	return shim.Success(ProductInfo)
}
//WriteProduct 修改产品
func (t *SimpleChaincode) WriteProduct(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("ex02 WriteProduct")

	var ProductID string      //产品id
	var ProductName int       //产品名称
	var ProductType int       //产品类型
	var OrganizationID string //产品所属机构id
	var Portion int           //产品份额
	var product Product

	if len(args) != 6 {
		return shim.Error("Incorrect number of arguments. Expecting 5")
	}
	var err error
	ProductID = args[1]
	ProductName, err = strconv.Atoi(args[2])
	if err != nil {
		return shim.Error("Expecting integer value for asset holding：Number ")
	}
	ProductType, err = strconv.Atoi(args[3])
	if err != nil {
		return shim.Error("Expecting integer value for asset holding：Number ")
	}
	OrganizationID = args[4]
	Portion, err = strconv.Atoi(args[5])
	if err != nil {
		return shim.Error("Expecting integer value for asset holding：Number ")
	}
	productinfo , err := stub.GetState(ProductID)
	if err != nil {
		return shim.Error("failed to get productinfo")
	} else if productinfo == nil {
		return shim.Error("product not exists")
	}
	err = json.Unmarshal(productinfo, &product)

	product.ProductID = ProductID
	product.ProductName = ProductName
	product.ProductType = ProductType
	product.OrganizationID = OrganizationID
	product.Portion = Portion

	jsons_product, err := json.Marshal(product) //转换成JSON返回的是byte[]
	if err != nil {
		return shim.Error(err.Error())
	}
	// Write the state to the ledger
	err = stub.PutState(args[1], jsons_product)
	if err != nil {
		return shim.Error(err.Error())
	}

	fmt.Printf(" CreateProduct success \n")
	return shim.Success(nil)
}
