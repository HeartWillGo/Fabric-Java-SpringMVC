package main
import (
	"encoding/json"
	"fmt"
	"strconv"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
	"bytes"
)

//用户
type User struct {
	ID                 string `json:"id"`                 //用户ID
	Name               string `json:"name"`               //用户名字
	IdentificationType int    `json:"identificationType"` // 证件类型
	Identification     string `json:"identification"`     //证件号码
	Sex                int    `json:"sex"`                //性别
	Birthday           string `json:"birthday"`           //生日
	BankCard           string `json:"bankcard"`           //银行卡号
	PhoneNumber        string `json:"phonoumber"`         //手机号
	Token              string `json:"token"`              //密钥

	ProductMap     map[string]Product     `json:"productmap"`     //产品
	TransactionMap map[string]Transaction `json:"transactionmap"` //交易
}
//用户登录
func (t *SimpleChaincode) UserLogin(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("ex02 UserLogin")

	var userid string   // 用户ID
	var username string //用户名称
	var token string    //用户密钥
	var user User

	if len(args) != 4 {
		return shim.Error("UserLogin :Incorrect number of arguments. Expecting 4")
	}

	// Initialize the chaincode

	userid = args[1]
	username = args[2]
	token = args[3]

	userinfo, err := stub.GetState(userid)
	if err != nil {
		return shim.Error(err.Error())
	}
	if userinfo == nil {
		return shim.Success(nil)
	} else {
		err = json.Unmarshal(userinfo, &user)
		if err != nil {
			return shim.Error(err.Error())
		} else if (username == user.Name) && (token == user.Token) {
			return shim.Success(nil)

		}

	}
	return shim.Success(nil)
}
//用户查询当下的所有产品
func (t *SimpleChaincode) GetUserProduct(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("ex02 WriteUser")

	var userid string //用户ID
	var user User

	var buffer bytes.Buffer

	if len(args) != 2 {
		return shim.Error("getUserProduct：Incorrect number of arguments. Expecting 2")
	}

	userid = args[1]
	UserInfo, err := stub.GetState(userid)
	if err != nil {
		return shim.Error(err.Error())
	}
	if UserInfo != nil {
		//将byte的结果转换成struct
		err = json.Unmarshal(UserInfo, &user)
		if err != nil {
			return shim.Error(err.Error())
		}
		buffer.WriteString("{")
		bArrayMemberAlreadyWritten := false

		for key, product_value := range user.ProductMap {

			if bArrayMemberAlreadyWritten == true {
				buffer.WriteString(",")
			}
			productbytes, err := json.Marshal(product_value)
			if err != nil {
				return shim.Error("wrong value")
			}
			buffer.WriteString(string(productbytes))


			fmt.Printf("产品：", key, "产品内容：", productbytes)
			bArrayMemberAlreadyWritten = true

		}
		buffer.WriteString("}")

		return shim.Success(buffer.Bytes())

	}
	return shim.Success(nil)

}

//createUser
func (t *SimpleChaincode) CreateUser(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("ex02 CreateUser")
	//
	var ID string              //用户ID
	var Name string            //用户名字
	var IdentificationType int // 证件类型
	var Identification string  //证件号码
	var Sex int                //性别
	var Birthday string        //生日
	var BankCard string        //银行卡号
	var PhoneNumber string     //手机号
	var token string           //密钥

	var user User

	if len(args) != 10 {
		return shim.Error("CreateUser：Incorrect number of arguments. Expecting 10")
	}

	ID = args[1]
	Name = args[2]
	var err error
	IdentificationType, err = strconv.Atoi(args[3])
	if err != nil {
		return shim.Error("Expecting integer value for asset holding：TotalNumber ")
	}
	Identification = args[4]
	Sex, err = strconv.Atoi(args[5])
	if err != nil {
		return shim.Error("Expecting integer value for asset holding：TotalNumber ")
	}
	Birthday = args[6]
	BankCard = args[7]
	PhoneNumber = args[8]
	token = args[9]

	user.ID = ID
	user.BankCard = BankCard
	user.Birthday = Birthday
	user.Identification = Identification
	user.IdentificationType = IdentificationType
	user.Name = Name
	user.PhoneNumber = PhoneNumber
	user.Sex = Sex
	user.Token = token
	user.ProductMap = make(map[string]Product)
	user.TransactionMap = make(map[string]Transaction)


	jsons_users, err := json.Marshal(user) //转换成JSON返回的是byte[]
	if err != nil {
		return shim.Error(err.Error())
	}
	userbytes, err := stub.GetState(ID)
	if err != nil {
		return shim.Error("failed to return userbytes")
	} else  if userbytes != nil {
		return shim.Error(ID + "already have")
	}
	err = stub.PutState(ID, jsons_users)
	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(nil)
}

//用户查询某机构的产品
func (t *SimpleChaincode) getUserProductogOrg(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("ex02 WriteUser")

	var userid string //用户ID
	var org_id string //用户ID
	var user User
	var buffer bytes.Buffer

	if len(args) != 3 {
		return shim.Error("getUserProductogOrg number of arguments. Expecting 3")
	}

	userid = args[1]
	org_id = args[2]
	UserInfo, err := stub.GetState(userid)
	if err != nil {
		return shim.Error(err.Error())
	} else if UserInfo == nil {
		return shim.Error(string(UserInfo) + "is not exists")
	}
	err = json.Unmarshal(UserInfo, &user)
	if err != nil {
		return shim.Error("unmarshal user not successful")
	}


	buffer.WriteString("{")
	bArrayMemberAlreadyWritten := false

	for key, product_value := range user.ProductMap {



		if product_value.OrganizationID == org_id {

			productbytes, err := json.Marshal(product_value)
			if err != nil {
				return shim.Error("productbytes marshal error")
			}

			if bArrayMemberAlreadyWritten == true {
				buffer.WriteString(",")
			}
			buffer.WriteString(string(productbytes))
			bArrayMemberAlreadyWritten = true
		}

		fmt.Printf("产品：", key, "产品内容：", product_value)

	}

	return shim.Success(buffer.Bytes())

}
//getUser 获取用户信息
func (t *SimpleChaincode) getUser(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("ex02 getUser")

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

	fmt.Printf("  userinfo  = %d  \n", userinfo)

	return shim.Success(userinfo)
}

//writeUser  修改用户信息,全部更改?
func (t *SimpleChaincode) WriteUser(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("ex02 WriteUser")

	var ID string              //用户ID
	var Name string            //用户名字
	var IdentificationType int // 证件类型
	var Identification string  //证件号码
	var Sex int                //性别
	var Birthday string        //生日
	var BankCard string        //银行卡号
	var PhoneNumber string     //手机号


	// check if user
	var user User
	userinfo , err := stub.GetState(ID)
	if err != nil {
		return shim.Error("failed to get userinfo")
	}	else if userinfo == nil {
		return shim.Error("you should first create user!!!")
	}
	json.Unmarshal(userinfo, &user)
	if len(args) != 9 {
		return shim.Error("Incorrect number of arguments. Expecting 4")
	}

	ID = args[1]
	Name = args[2]
	IdentificationType, err = strconv.Atoi(args[3])
	if err != nil {
		return shim.Error("Expecting integer value for asset holding：TotalNumber ")
	}
	Identification = args[4]
	Sex, err = strconv.Atoi(args[5])
	if err != nil {
		return shim.Error("Expecting integer value for asset holding：TotalNumber ")
	}
	Birthday = args[6]
	BankCard = args[7]
	PhoneNumber = args[8]

	user.ID = ID
	user.BankCard = BankCard
	user.Birthday = Birthday
	user.Identification = Identification
	user.IdentificationType = IdentificationType
	user.Name = Name
	user.PhoneNumber = PhoneNumber
	user.Sex = Sex

	jsons_users, err := json.Marshal(user) //转换成JSON返回的是byte[]
	if err != nil {
		return shim.Error(err.Error())
	}

	// Write the state to the ledger
	err = stub.PutState(args[1], jsons_users)
	if err != nil {
		return shim.Error(err.Error())
	}
	fmt.Printf(" CeateBank success \n")
	return shim.Success(nil)
}

