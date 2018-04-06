package main

import (
	"encoding/json"
	"fmt"

	"bytes"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

//用户
type User struct {
	ID                 string `json:"id"`
	Name               string `json:"Name"`
	Identificationtype int    `json:"identificationtype"`
	Identification     string `json:"identification"`
	Sex                int    `json:"sex"`
	Birthday           string `json:"birthday"`
	Bankcard           string `json:"bankcard"`
	Phonenumber        string `json:"phonenumber"`
	Token              string `json:"token"`
}

type UserLogin struct {
	Userid   string `json:"userid"`
	Username string `json:"username"`
	Token    string `json:"token"`
}

//用户登录
func (t *SimpleChaincode) UserLogin(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("ex02 UserLogin")

	if len(args) != 2 {
		return shim.Error("UserLogin :Incorrect number of arguments. Expecting 2")
	}
	var user User
	var userLogin UserLogin
	err := json.Unmarshal([]byte(args[1]), &userLogin)
	if err != nil {
		return shim.Error("failed to unmarshal userlogin")
	}

	userinfo, err := stub.GetState(userLogin.Userid)
	if err != nil {
		return shim.Error(err.Error())
	}
	if userinfo == nil {
		return shim.Success(nil)
	} else {
		err = json.Unmarshal(userinfo, &user)
		if err != nil {
			return shim.Error(err.Error())
		} else if (userLogin.Username == user.Name) && (userLogin.Token == user.Token) {
			return shim.Success(nil)

		}

	}
	return shim.Success(nil)
}

//createUser
func (t *SimpleChaincode) CreateUser(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("ex0201 CreateUser")

	var user User

	if len(args) != 2 {
		return shim.Error("CreateUser：Incorrect number of arguments. Expecting 2")
	}

	err := json.Unmarshal([]byte(args[1]), &user)
	if err != nil {
		return shim.Error(err.Error())
	}

	userInfo, err := json.Marshal(user)
	if err != nil {
		return shim.Error(err.Error())
	}

	userByte, err := stub.GetState(user.ID)
	if err != nil {
		return shim.Error("failed to return userbytes")
	} else if userByte != nil {
		return shim.Error(user.ID + "already have")
	}
	err = stub.PutState(user.ID, userInfo)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(nil)
}

//getUser 获取用户信息
func (t *SimpleChaincode) getUser(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("ex0202 getUser")

	var User_ID string // 用户ID
	var user User

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	// Initialize the chaincode

	User_ID = args[1]
	userinfo, err := stub.GetState(User_ID)
	if err != nil {
		return shim.Error(err.Error())
	}
	//将byte的结果转换成struct
	err = json.Unmarshal(userinfo, &user)
	return shim.Success(userinfo)
}

// TODO writeUser  修改用户信息,全部更改?
func (t *SimpleChaincode) WriteUser(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("ex02 WriteUser")

	var user User

	if len(args) != 2 {
		return shim.Error("CreateUser：Incorrect number of arguments. Expecting 2")
	}

	err := json.Unmarshal([]byte(args[1]), &user)
	if err != nil {
		return shim.Error(err.Error())
	}

	userInfo, err := json.Marshal(user)
	if err != nil {
		return shim.Error(err.Error())
	}

	userByte, err := stub.GetState(user.ID)
	if err != nil {
		return shim.Error("failed to return userbytes")
	} else if userByte == nil {
		return shim.Error(user.ID + "don't have")
	}
	err = stub.PutState(user.ID, userInfo)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(nil)
}

//得到某一用户的所有资产详情
//args[0] functionname string
//args[1] userid string
//args = []string {"getUserAsset", "1"}
func (t *SimpleChaincode) getUserAsset(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("0x0203 Enter in getUserAsset")


	resp := t.getTransactionByUserID(stub, args)
	if resp.Status != shim.OK {
		return shim.Error("getUserAssetFailed")
	}
	asset := computeAssetByUserID(args[1], resp.GetPayload())
	assetBytes, err := json.Marshal(asset)
	if err != nil {
		fmt.Println("marshal wrong")
	}

	fmt.Println(string(assetBytes))

	return shim.Success(assetBytes)
}

//用户查询某机构购买的产品信息
//args []string("getUserByproductid", "organizationid", "userid"}
//return [{product1}, {product2}]
func (t *SimpleChaincode) getUserOrgProductid(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("0x06 Enter in getUserByProductid")
	resp := t.getTransactionByUserID(stub, args[1:])
	if resp.Status != shim.OK {
		return shim.Error("getUserAssetFailed")
	}
	asset := computeAssetByUserID(args[2], resp.GetPayload())
	var buffer bytes.Buffer
	var AlreadyProduct = false
	buffer.WriteString("[")
	Organization := asset.OrganizatonMap[args[1]]
	for _, product := range Organization.ProductMap {
		productInfo, err := stub.GetState(product.ID)
		if err != nil {
			return shim.Error(err.Error())
		}
		if AlreadyProduct == true {
			buffer.WriteString(",")
		}
		buffer.WriteString(string(productInfo))
		AlreadyProduct = false
	}

	buffer.WriteString("]")
	fmt.Println(buffer.String())
	return shim.Success(buffer.Bytes())
}

//用户查询当下的所有产品
//args []string{"getUserAllProduct", "userid"}
//return product
func (t *SimpleChaincode) getUserAllProduct(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("0x0204 Enter in GetUserAllProduct")

	resp := t.getTransactionByUserID(stub, args)
	if resp.Status != shim.OK {
		return shim.Error("GetUserAllProduct")
	}
	asset := computeAssetByUserID(args[1], resp.GetPayload())

	var buffer bytes.Buffer
	var AlreadyOrg = false
	buffer.WriteString("[")

	for _, Org := range asset.OrganizatonMap {
		if AlreadyOrg == true {
			buffer.WriteString(",")
		}

		buffer.WriteString("{")
		buffer.WriteString("\"")
		buffer.WriteString(Org.ID)
		buffer.WriteString("\"")
		buffer.WriteString(":")
		var AlreadyProduct = false
		for _, Product := range Org.ProductMap {
			if AlreadyProduct {
				buffer.WriteString(",")
			}
			fmt.Println("productid", Product.ID)
			productBytes, err := stub.GetState(Product.ID)
			if err != nil {
				return shim.Error(err.Error())
			}

			buffer.WriteString(string(productBytes))
			AlreadyProduct = true
		}
		buffer.WriteString("}")
		AlreadyOrg = true
	}
	buffer.WriteString("]")
	return shim.Success(buffer.Bytes())

}

//TODO:
//getAsset里面包含了user在机构的所有信息,这个地方是否有必要？
//args []string{"getUserFromOrganizationAsset",  "organizationid", "userid"}
func (t *SimpleChaincode) getUserFromOrganizationAsset(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("0x05 Enter in getUserFromOrganizationAsset")
	resp := t.getTransactionByUserID(stub, args[1:])
	if resp.Status != shim.OK {
		return shim.Error("getTransactionByUserID")
	}
	asset := computeAssetByUserID(args[2], resp.GetPayload())
	assetBytes, err := json.Marshal(asset)
	if err != nil {
		fmt.Println("marshal wrong")
	}
	fmt.Println(string(assetBytes))

	productInfo := asset.OrganizatonMap[args[1]]

	productBytes, err := json.Marshal(productInfo)
	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(productBytes)
}
