package main

import (
	"encoding/json"
	"fmt"
	"strconv"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"

)

//createOrganization
func (t *SimpleChaincode) createOrganization(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("ex02 createOrganization")

	var OrganizationID string    //机构id
	var OrganizationName string  //机构名称
	var OrganizationType int     //机构类型
	var organization Organizaton //机构

	if len(args) != 4 {
		return shim.Error("Incorrect number of arguments. Expecting 3")
	}

	// Initialize the chaincode
	OrganizationID = args[1]
	OrganizationName = args[2]


	OrganizationType, err := strconv.Atoi(args[3])
	if err != nil {
		return shim.Error("Expecting integer value for asset holding：Number ")
	}
	organization.OrganizationID = OrganizationID
	organization.OrganizationName = OrganizationName
	organization.OrganizationType = OrganizationType

	jsons_organization, err := json.Marshal(organization) //转换成JSON返回的是byte[]
	if err != nil {
		return shim.Error(err.Error())
	}

	orginfo, err := stub.GetState(OrganizationID)
	if err != nil {
		return shim.Error("failed to get orginfo")
	} else if orginfo != nil {
		return shim.Error(string(orginfo) + "\t is already exists")
	}
	// Write the state to the ledger
	err = stub.PutState(args[1], jsons_organization)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(nil)
}
//getOrganization 获取机构信息
func (t *SimpleChaincode) getOrganization(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("ex02 getOrganization")

	var Organization_ID string // 商业银行ID
	var organization Organizaton

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	// Initialize the chaincode

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
	fmt.Printf("  OrganizationInfo  = %d  \n", OrganizationInfo)

	return shim.Success(OrganizationInfo)
}

//WriteOrganization         修改机构
func (t *SimpleChaincode) WriteOrganization(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("ex02 WriteOrganization")

	var OrganizationID string      //机构id
	var OrganizationName string    //机构名称
	var OrganizationType int       //机构类型


	if len(args) != 4 {
		return shim.Error("Incorrect number of arguments. Expecting 3")
	}

	// Initialize the chaincode
	OrganizationID = args[1]
	OrganizationName = args[2]

	OrganizationType, err := strconv.Atoi(args[3])
	if err != nil {
		return shim.Error("Expecting integer value for asset holding：Number ")
	}

	//check org if exists
	var organization Organizaton

	orginfo , err := stub.GetState(OrganizationID)
	if err != nil {
		return shim.Error("failed to get org info")
	} else if orginfo == nil {
		return shim.Error("org is not exists")
	}

	err = json.Unmarshal(orginfo, &organization)
	if err != nil {
		return shim.Error("cannot marshal orgazation")
	}
	organization.OrganizationID = OrganizationID
	organization.OrganizationName = OrganizationName
	organization.OrganizationType = OrganizationType

	jsons_organization, err := json.Marshal(organization) //转换成JSON返回的是byte[]
	if err != nil {
		return shim.Error(err.Error())
	}
	// Write the state to the ledger
	err = stub.PutState(args[1], jsons_organization)
	if err != nil {
		return shim.Error(err.Error())
	}

	fmt.Printf("CreateOrganization \n")

	return shim.Success(nil)
}

