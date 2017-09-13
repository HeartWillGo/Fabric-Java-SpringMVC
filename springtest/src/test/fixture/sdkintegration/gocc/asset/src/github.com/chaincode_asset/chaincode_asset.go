package main

//WARNING - this chaincode's ID is hard-coded in chaincode_example04 to illustrate one way of
//calling chaincode from a chaincode. If this example is modified, chaincode_example04.go has
//to be modified as well with the new ID of chaincode_example02.
//chaincode_example05 show's how chaincode ID can be passed in as a parameter instead of
//hard-coding.

import (
	"encoding/json"
	"fmt"
	"strconv"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

// SimpleChaincode example simple Chaincode implementation
type SimpleChaincode struct {
}

//用户
type User struct {
	ID                 string `json:"id"`                 //用户ID
	Name               string `json:"name"`               //用户名字
	IdentificationType int    `json:"identificationType"` // 证件类型
	Identification     string `json:"id"`                 //证件号码
	Sex                int    `json:"sex"`                //性别
	Birthday           string `json:"birthday"`           //生日
	BankCard           string `json:"bankcard"`           //银行卡号
	PhoneNumber        string `json:"phonoumber"`         //手机号

	ProductMap map[string]Product `json:"productmap"` //产品
}
type ProductProcess struct {
	ProcessType   int `json:"processtype“`   //操作类型
	ProcessAmount int `json:"processamount"` //操作份额
	ProcessPrice  int `json:"ProcessPrice"`  //价格
}

//资金
type Fund struct {
	CardID string `json:"cardid"` //银行卡id
	Amount int    `json:"amount"` //卡上剩余金额

}

//产品
type Product struct {
	ProductID      string `json:"productid"`      //产品id
	ProductName    int    `json:"productname"`    //产品名称
	ProductType    int    `json:"producttype"`    //产品类型
	OrganizationID string `json:"organizationid"` //产品所属机构id
	Portion        int    `json:"portion"`        //产品份额
	Price          int    `json:"price"`          //单价

}

//机构
type Oraganization struct {
	OrganizationID   string `json:"organizationid"`   //机构id
	OrganizationName string `json:"organizationname"` //机构名称
	OrganizationType int    `json:"organizationtype"` //机构类型

}

//交易内容
type Transaction struct {
	TransId       string `json:"id"`         //交易id
	TransType     int    `json:"transtype"`  //交易类型 0 表示申购，1，表示赎回， 2，表示入金
	FromType      int    `json:"fromtype"`   //发送方角色
	FromID        string `json:"fromid"`     //发送方 ID
	ToType        int    `json:"totype"`     //接收方角色
	ToID          string `json:"toid"`       //接收方 ID
	Time          string `json:"time"`       //交易时间
	ProductID     string `json:"productid"`  //交易产品id
	Account       int    `json:"account"`    //交易 份额
	Price         int    `json:"price"`      //交易价格
	ParentOrderNo string `json:parentorder"` //父订单号
}

var err error

func (t *SimpleChaincode) Init(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Println("ex02 Init")
	_, args := stub.GetFunctionAndParameters()
	if len(args) != 0 {
	return shim.Error("Incorrect number of arguments. Expecting 0")
	}
	return shim.Success(nil)
}
func (t *SimpleChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Println("ex02 Invoke")
	function, args := stub.GetFunctionAndParameters()
	if function != "invoke" {
		return shim.Error("Unknown function call")
	}
	if len(args) < 2 {
		return shim.Error("Incorrect number of arguments. Expecting at least 2")
	}
	switch {

	case args[0] == "CreateUser":
		return t.CreateUser(stub, args)
	case args[0] == "createOrganization":
		return t.createOrganization(stub, args)
	case args[0] == "CreateProduct":
		return t.CreateProduct(stub, args)
	case args[0] == "getTransactionByID":
		return t.getTransactionByID(stub, args)
	case args[0] == "getProduct":
		return t.getProduct(stub, args)
	case args[0] == "getOrganization":
		return t.getOrganization(stub, args)
	case args[0] == "getUser":
		return t.getUser(stub, args)
	case args[0] == "WriteUser":
		return t.WriteUser(stub, args)
	case args[0] == "WriteOrganization":
		return t.WriteOrganization(stub, args)
	case args[0] == "WriteProduct":
		return t.WriteProduct(stub, args)

	case args[0] == "transation":
		return t.transation(stub, args)
	case args[0] == "getUserAsset":
		return t.getUserAsset(stub, args)
	case args[0] == "query":
		return t.query(stub, args)
	default:
		fmt.Printf("function is not exist\n")
	}

	return shim.Error("Unknown action,")
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

	var user User

	if len(args) != 9 {
		return shim.Error("Incorrect number of arguments. Expecting 8")
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

	map_pro := make(map[string]Product)
	user.ProductMap = map_pro

	jsons_users, err := json.Marshal(user) //转换成JSON返回的是byte[]
	if err != nil {
		return shim.Error(err.Error())
	}

	// Write the state to the ledger
	err = stub.PutState(args[1], jsons_users)
	if err != nil {
		return shim.Error(err.Error())
	}
	fmt.Printf(" CreateUser success \n", jsons_users)
	return shim.Success(nil)
}

//createOrganization
func (t *SimpleChaincode) createOrganization(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("ex02 createOrganization")

	var OrganizationID string      //机构id
	var OrganizationName string    //机构名称
	var OrganizationType int       //机构类型
	var oragnization Oraganization //机构

	if len(args) != 4 {
		return shim.Error("Incorrect number of arguments. Expecting 3")
	}

	// Initialize the chaincode
	OrganizationID = args[1]
	OrganizationName = args[2]

	OrganizationType, err = strconv.Atoi(args[3])
	if err != nil {
		return shim.Error("Expecting integer value for asset holding：Number ")
	}
	oragnization.OrganizationID = OrganizationID
	oragnization.OrganizationName = OrganizationName
	oragnization.OrganizationType = OrganizationType

	jsons_organization, err := json.Marshal(oragnization) //转换成JSON返回的是byte[]
	if err != nil {
		return shim.Error(err.Error())
	}
	// Write the state to the ledger
	err = stub.PutState(args[1], jsons_organization)
	if err != nil {
		return shim.Error(err.Error())
	}

	fmt.Printf("CreateOrganization \n", jsons_organization)

	return shim.Success(nil)
}

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
	// Write the state to the ledger
	err = stub.PutState(args[1], jsons_product)
	if err != nil {
		return shim.Error(err.Error())
	}

	fmt.Printf(" CreateProduct success \n", jsons_product)
	return shim.Success(nil)
}

//getTransactionByID 获取某笔交易
func (t *SimpleChaincode) getTransactionByID(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("ex02 getTransactionByID")

	var Transactin_ID string //交易ID
	var transaction Transaction
	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}
	Transactin_ID = args[1]

	TransactionInfo, err := stub.GetState(Transactin_ID)
	if err != nil {
		return shim.Error(err.Error())
	}
	//将byte的结果转换成struct
	err = json.Unmarshal(TransactionInfo, &transaction)
	if err != nil {
		return shim.Error(err.Error())
	}
	fmt.Printf("  TransactionInfo  = %d  \n", TransactionInfo)

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
	return shim.Success(nil)
}

//getOrganization 获取机构信息
func (t *SimpleChaincode) getOrganization(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("ex02 getOrganization")

	var Organization_ID string // 商业银行ID
	var organization Oraganization

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

	return shim.Success(nil)
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

	return shim.Success(nil)
}

//writeUser  修改用户信息
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
	var user User

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

//WriteOrganization         修改机构
func (t *SimpleChaincode) WriteOrganization(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("ex02 WriteOrganization")

	var OrganizationID string      //机构id
	var OrganizationName string    //机构名称
	var OrganizationType int       //机构类型
	var oragnization Oraganization //机构

	if len(args) != 4 {
		return shim.Error("Incorrect number of arguments. Expecting 3")
	}

	// Initialize the chaincode
	OrganizationID = args[1]
	OrganizationName = args[2]

	OrganizationType, err = strconv.Atoi(args[3])
	if err != nil {
		return shim.Error("Expecting integer value for asset holding：Number ")
	}
	oragnization.OrganizationID = OrganizationID
	oragnization.OrganizationName = OrganizationName
	oragnization.OrganizationType = OrganizationType

	jsons_organization, err := json.Marshal(oragnization) //转换成JSON返回的是byte[]
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

//Transation交易
func (t *SimpleChaincode) transation(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("ex02 Transation交易")

	var TransId string          //交易id
	var TransType int           //交易类型
	var FromType int            //发送方角色
	var FromID string           //发送方 ID
	var ToType int              //接收方角色
	var ToID string             //接收方 ID
	var Time string             //交易时间
	var ProductID string        //交易产品id
	var Account int             //交易 份额
	var ParentOrderNo string    //父订单号
	var price int               //价格
	var transaction Transaction //交易
	var product Product

	var user User
	var ProductMap map[string]Product
	if len(args) != 12 {
		return shim.Error("Incorrect number of arguments. Expecting 5")
	}

	TransId = args[1]
	TransType, err = strconv.Atoi(args[2])
	if err != nil {
		return shim.Error("Expecting integer value for asset holding：Number ")
	}
	FromType, err = strconv.Atoi(args[3])
	if err != nil {
		return shim.Error("Expecting integer value for asset holding：Number ")
	}
	FromID = args[4]
	ToType, err = strconv.Atoi(args[5])
	if err != nil {
		return shim.Error("Expecting integer value for asset holding：Number ")
	}
	ToID = args[6]
	Time = args[7]
	ProductID = args[8]
	Account, err = strconv.Atoi(args[9])
	if err != nil {
		return shim.Error("Expecting integer value for asset holding：Number ")
	}
	ParentOrderNo = args[10]
	price, err = strconv.Atoi(args[11])
	if err != nil {
		return shim.Error("Expecting integer value for asset holding：Number ")
	}

	transaction.Account = Account
	transaction.FromID = FromID
	transaction.FromType = FromType
	transaction.ParentOrderNo = ParentOrderNo
	transaction.ProductID = ProductID
	transaction.Time = Time
	transaction.ToID = ToID
	transaction.ToType = ToType
	transaction.TransId = TransId
	transaction.TransType = TransType
	transaction.Price = price

	jsons_transaction, err := json.Marshal(transaction) //转换成JSON返回的是byte[]
	if err != nil {
		return shim.Error(err.Error())
	}
	// Write the state to the ledger
	err = stub.PutState(TransId, jsons_transaction)
	if err != nil {
		return shim.Error(err.Error())
	}

	FromInfo, err := stub.GetState(FromID)
	if err != nil {
		return shim.Error(err.Error())
	}
	//将byte的结果转换成struct
	err = json.Unmarshal(FromInfo, &user)
	if err != nil {
		return shim.Error(err.Error())
	}

	ProductMap = user.ProductMap
	product = ProductMap[ProductID]
	product.Portion = product.Portion + Account
	ProductMap[ProductID] = product

	user.ProductMap = ProductMap

	jsons_User, err := json.Marshal(user) //转换成JSON返回的是byte[]
	if err != nil {
		return shim.Error(err.Error())
	}
	// Write the state to the ledger
	err = stub.PutState(FromID, jsons_User)

	fmt.Printf(" transation success \n")
	return shim.Success(nil)
}

//getUserAsset  查询用户资产
func (t *SimpleChaincode) getUserAsset(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("ex02 WriteUser")

	var User_ID string //用户ID
	//	var Name string            //用户名字
	//	var IdentificationType int // 证件类型
	//	var Identification string  //证件号码
	//	var Sex int                //性别
	//	var Birthday string        //生日
	//	var BankCard string        //银行卡号
	//	var PhoneNumber string     //手机号
	//	var TransactionIDArray []string

	var user User
	var ProductMap map[string]Product

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 4")
	}

	User_ID = args[1]
	UserInfo, err := stub.GetState(User_ID)
	if err != nil {
		return shim.Error(err.Error())
	}
	//将byte的结果转换成struct
	err = json.Unmarshal(UserInfo, &user)
	if err != nil {
		return shim.Error(err.Error())
	}
	ProductMap = user.ProductMap

	for key, value := range ProductMap {
		fmt.Printf("%s-%d\n", key, value)

		fmt.Printf("产品：", key, "产品内容：", value)

	}

	fmt.Printf(" CeateBank success \n")
	return shim.Success(nil)
}

// Deletes an entity from state
func (t *SimpleChaincode) delete(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	A := args[1]

	// Delete the key from the state in ledger
	err := stub.DelState(A)
	if err != nil {
		return shim.Error("Failed to delete state")
	}

	return shim.Success(nil)
}

// Deletes an entity from state

// query callback representing the query of a chaincode
func (t *SimpleChaincode) query(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var A string // Entities
	var err error

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
	}

	A = args[1]

	// Get the state from the ledger
	Avalbytes, erro := stub.GetState(A)
	if erro != nil {
		return shim.Error(erro.Error())
	}
	if err != nil {
		jsonResp := "{\"Error\":\"Failed to get state for " + A + "\"}"
		return shim.Error(jsonResp)
	}

	if Avalbytes == nil {
		jsonResp := "{\"Error\":\"Nil amount for " + A + "\"}"
		return shim.Error(jsonResp)
	}

	jsonResp := "{\"Name\":\"" + A + "\",\"Amount\":\"" + string(Avalbytes) + "\"}"
	fmt.Printf("Query Response:%s\n", jsonResp)
	return shim.Success(Avalbytes)
}

func main() {
	err := shim.Start(new(SimpleChaincode))
	if err != nil {
		fmt.Printf("Error starting Simple chaincode: %s", err)
	}
}
