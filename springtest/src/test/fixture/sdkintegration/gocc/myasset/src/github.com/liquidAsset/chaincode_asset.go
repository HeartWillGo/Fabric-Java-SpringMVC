package main

//WARNING - this chaincode's ID is hard-coded in chaincode_example04 to illustrate one way of
//calling chaincode from a chaincode. If this example is modified, chaincode_example04.go has
//to be modified as well with the new ID of chaincode_example02.
//chaincode_example05 show's how chaincode ID can be passed in as a parameter instead of
//hard-coding.

import (
	"fmt"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

// SimpleChaincode example simple Chaincode implementation
type SimpleChaincode struct {
}


type ProductProcess struct {
	ProcessType   int `json:"processtype"`   //操作类型
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
type Organizaton struct {
	OrganizationID   string `json:"organizationid"`   //机构id
	OrganizationName string `json:"organizationname"` //机构名称
	OrganizationType int    `json:"organizationtype"` //机构类型
	ProductMap 	  map[string](map[string]Transaction) `json:"productmap"`

}


func (t *SimpleChaincode) Init(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Println("ex02 Init")
	_, args := stub.GetFunctionAndParameters()
	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 0")
	}

	stub.PutState("qwertyuiop", []byte(args[0]))

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
	case args[0] == "getUserAsset":
		fmt.Println("entering getuesrAsset")
		return t.getUserAsset(stub, args)

	case args[0] == "createOrganization":
		return t.createOrganization(stub, args)
	case args[0] == "CreateProduct":
		return t.CreateProduct(stub, args)

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

	case args[0] == "Transaction":
		return t.Transaction(stub, args)
	case args[0] == "getTransactionByID":
		return t.getTransactionByID(stub, args)
	case args[0] == "getTransactionByUserID":
		return t.getTransactionByUserID(stub, args)
	case args[0] == "query":
		return t.query(stub, args)
	default:
		fmt.Printf("function is not exist\n")
	}

	return shim.Error("Unknown action,")
}
// query callback representing the query of a chaincode
func (t *SimpleChaincode) query(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var A string // Entities
	var err error

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
	}

	A = args[1]

	// Get the state from the ledger
	Avalbytes, err := stub.GetState(A)
	if err != nil {
		return shim.Error(err.Error())
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
