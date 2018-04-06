package main

import (
	"encoding/json"
	"fmt"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"

	"bytes"
)

//机构
type Organization struct {
	OrganizationID   string `json:"organizationid"`   //机构id
	OrganizationName string `json:"organizationname"` //机构名称
	OrganizationType int    `json:"organizationtype"` //机构类型
}

//createOrganization
func (t *SimpleChaincode) createOrganization(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("ex02 createOrganization")

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 3")
	}

	var organization Organization

	err := json.Unmarshal([]byte(args[1]), &organization)
	if err != nil {
		return shim.Error(err.Error())
	}

	OrganizationBytes, err := stub.GetState(organization.OrganizationID)
	if err != nil {
		return shim.Error("failed to get orginfo")
	} else if OrganizationBytes != nil {
		return shim.Error(string(OrganizationBytes) + "\t is already exists")
	}

	// Write the state to the ledger
	err = stub.PutState(organization.OrganizationID, []byte(args[1]))
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(nil)
}

//getOrganization 获取机构信息
func (t *SimpleChaincode) getOrganization(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("ex02 getOrganization")

	var Organization_ID string // 商业银行ID
	var organization Organization

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	Organization_ID = args[1]

	OrganizationInfo, err := stub.GetState(Organization_ID)
	if err != nil {
		return shim.Error(err.Error())
	}
	//将byte的结果转换成struct
	err = json.Unmarshal(OrganizationInfo, &organization)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(OrganizationInfo)
}

//TODO:修改机构
func (t *SimpleChaincode) WriteOrganization(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("ex02 WriteOrganization")

	fmt.Printf("CreateOrganization \n")

	return shim.Success(nil)
}

func (t *SimpleChaincode) getTransactionByOrganizationid(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("0x07 getProductTransactionByProductID")

	if len(args) != 2 {
		return shim.Error("Expecting 2, you are wrong")
	}
	organizationid := args[1:]

	// This will execute a key range query on all keys starting with 'Productid
	transactionOrganizationidResultIterator, err := stub.GetStateByPartialCompositeKey("Organizationid~Transactionid", organizationid)
	if err != nil {
		return shim.Error(err.Error())
	}
	defer transactionOrganizationidResultIterator.Close()

	bArrayMemberAlreadyWritten := false
	var buffer bytes.Buffer
	buffer.WriteString("[")
	for transactionOrganizationidResultIterator.HasNext() {
		// Note that we don't get the value (2nd return variable), we'll just get the marble name from the composite key
		queryResponse, err := transactionOrganizationidResultIterator.Next()
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
		if objectType != "Organizationid~Transactionid" {
			return shim.Error("object is not we want " + organizationid[0])
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

func (t *SimpleChaincode) getOrganizationProduct(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("0x11 getOrganizationProduct")

	if len(args) != 2 {
		return shim.Error("Expecting 2, but get wrong")
	}
	resp := t.getTransactionByOrganizationid(stub, args)
	if resp.Status != shim.OK {
		return shim.Error("getUserAssetFailed")
	}
	productAsset := computeOrgnazitionAllProduct(resp.GetPayload())
	productAssetBytes, err := json.Marshal(productAsset)
	if err != nil {
		fmt.Println("marshal wrong")
	}

	return shim.Success(productAssetBytes)

}

func (t *SimpleChaincode) getOrganizationAsset(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	fmt.Println("0x11 getOrganizationAsset")

	if len(args) != 2 {
		return shim.Error("Expecting 2, but get wrong")
	}
	resp := t.getTransactionByOrganizationid(stub, args)
	if resp.Status != shim.OK {
		return shim.Error("getUserAssetFailed")
	}
	productAsset := computeOrgnazitionAllProduct(resp.GetPayload())
	productAssetBytes, err := json.Marshal(productAsset)
	if err != nil {
		fmt.Println("marshal wrong")
	}

	return shim.Success(productAssetBytes)
}
func (t *SimpleChaincode) getOrganizationUser(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("0x11 getOrganizationUser")

	if len(args) != 2 {
		return shim.Error("Expecting 2, but get wrong")
	}
	resp := t.getTransactionByOrganizationid(stub, args)
	if resp.Status != shim.OK {
		return shim.Error("getUserAssetFailed")
	}
	productAsset := computeOrgnazitionAllUser(resp.GetPayload())
	productAssetBytes, err := json.Marshal(productAsset)
	if err != nil {
		fmt.Println("marshal wrong")
	}

	return shim.Success(productAssetBytes)
}
