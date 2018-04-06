package main

import (
	"encoding/json"
	"fmt"

	"bytes"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

//产品
type Product struct {
	Productid      string  `json:"productid"`
	Productname    string  `json:"productname"`
	Producttype    int     `json:"producttype"`
	Organizationid string  `json:"organizationid"`
	Amount         float64 `json:"amount"`
	Price          float64 `json:"price"`
}

//createProduct 创建产品
func (t *SimpleChaincode) CreateProduct(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("ex11 CreateProduct")

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}

	var product Product
	err := json.Unmarshal([]byte(args[1]), &product)
	if err != nil {
		return shim.Error(err.Error())
	}
	productInfo, err := json.Marshal(product)
	if err != nil {
		return shim.Error(err.Error())
	}
	productByte, err := stub.GetState(product.Productid)
	if err != nil {
		return shim.Error("failed to get productID")
	} else if productByte != nil {
		return shim.Error(string(product.Productid) + "\t already exists")
	}

	// Write the state to the ledger
	err = stub.PutState(product.Productid, productInfo)
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
	return shim.Success(ProductInfo)
}

//TODO WriteProduct 修改产品
func (t *SimpleChaincode) WriteProduct(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	return shim.Success(nil)
}

//根据productid得到该产品的所有交易信息
func (t *SimpleChaincode) getProductTransactionByProductID(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("0x07 getProductTransactionByProductID")

	if len(args) != 2 {
		return shim.Error("Expecting 2, you are wrong")
	}
	productid := args[1:]

	// This will execute a key range query on all keys starting with 'Productid
	transactionProductidResultIterator, err := stub.GetStateByPartialCompositeKey("Productid~Transactionid", productid)
	if err != nil {
		return shim.Error(err.Error())
	}
	defer transactionProductidResultIterator.Close()

	bArrayMemberAlreadyWritten := false
	var buffer bytes.Buffer
	buffer.WriteString("[")
	for transactionProductidResultIterator.HasNext() {
		// Note that we don't get the value (2nd return variable), we'll just get the marble name from the composite key
		queryResponse, err := transactionProductidResultIterator.Next()
		if err != nil {
			return shim.Error(err.Error())
		}
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}

		objectType, compositeKeyParts, err := stub.SplitCompositeKey(queryResponse.Key)
		if err != nil {
			return shim.Error("we cannot splitcompositekey")
		}
		if objectType != "Productid~Transactionid" {
			return shim.Error("object is not we want %s" + productid[0])
		}
		transactionid := compositeKeyParts[len(compositeKeyParts)-1]

		transactionBytes, err := stub.GetState(transactionid)
		if err != nil {
			return shim.Error("the transactionid is not put in the ledger")
		}
		buffer.WriteString("{\"Key\":")
		buffer.WriteString("\"")
		buffer.WriteString(transactionid)
		buffer.WriteString("\"")

		buffer.WriteString(", \"Record\":")
		// Record is a JSON object, so we write as-is
		buffer.WriteString(string(transactionBytes))
		buffer.WriteString("}")
		bArrayMemberAlreadyWritten = true
	}

	buffer.WriteString("]")
	return shim.Success(buffer.Bytes())

}

//得到某一产品的售卖汇总
func (t *SimpleChaincode) getProductAsset(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("0x08 Enter in getProductAsset")
	resp := t.getProductTransactionByProductID(stub, args)
	if resp.Status != shim.OK {
		return shim.Error("getProductTransactionByProductID Failed")
	}
	asset := computeProductSaleInformation(resp.GetPayload())
	assetBytes, err := json.Marshal(asset)
	if err != nil {
		fmt.Println("marshal wrong")
	}

	return shim.Success(assetBytes)

}

//得到某一产品的购买用户
func (t *SimpleChaincode) getProductAllUser(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("0x08 Enter in getProductAllUser")
	resp := t.getProductTransactionByProductID(stub, args)
	if resp.Status != shim.OK {
		return shim.Error("getProductTransactionByProductID Failed")
	}
	productAsset := computeProductAllUser(resp.GetPayload())
	productAssetBytes, err := json.Marshal(productAsset)
	if err != nil {
		fmt.Println("marshal userOperateProductMapBytes Wrong")
	}

	return shim.Success(productAssetBytes)

}

//得到某一产品的某一用户的购买情况
func (t *SimpleChaincode) getProductOneUser(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("0x08 Enter in getProductOneUser")
	resp := t.getProductTransactionByProductID(stub, args[1:])
	if resp.Status != shim.OK {
		return shim.Error("getProductTransactionByProductID Failed")
	}
	productAsset := computeProductAllUser(resp.GetPayload())
	UserAssetBytes, err := json.Marshal(productAsset.UserMap[args[1]])

	if err != nil {
		fmt.Println("marshal userOperateProductMapBytes Wrong")
	}

	return shim.Success(UserAssetBytes)

}
