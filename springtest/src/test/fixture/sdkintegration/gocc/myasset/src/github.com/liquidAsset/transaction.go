package main

import (
	"encoding/json"
	"fmt"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
	"bytes"
)

//交易内容
type Transaction struct {
	//交易的ID和
	Transactionid   string `json:"transactionid"`
	Transactiondate string `json:"transactiondate"`

	Parentorder     string `json:"parentorder"`
	Suborder        string `json:"suborder"`
	Payid           string `json:"payid"`
	//交易头部
	Transtype       string `json:"transtype"`
	Fromtype        int    `json:"fromtype"`
	Fromid          string `json:"fromid"`
	Totype          int    `json:"totype"`
	Toid            string `json:"toid"`
	//交易内容
	Productid       string `json:"productid"`
	Productinfo     string `json:"productinfo"`
	Organizationid  string `json:"organizationid"`

	Amount          int    `json:"amount"`
	Price           int    `json:"price"`
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
	fmt.Printf("TransactionInfo %s  \n", string(TransactionInfo))

	return shim.Success(TransactionInfo)
}




//Transation交易
func (t *SimpleChaincode) Transaction(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("put order in ledger")

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments")
	}
	var transaction Transaction
	transactionBytes := args[1]
	err := json.Unmarshal([]byte(transactionBytes), &transaction)
	if err != nil {
		return shim.Error("wrong marshal get transaction")
	}
	err = stub.PutState(transaction.Transactionid, []byte(transactionBytes))
	if err != nil {
		return shim.Error(err.Error())
	}

	NameIndexKey, err := stub.CreateCompositeKey("TransactionObject", []string{transaction.Fromid, transaction.Transactionid})


	if err != nil {
		return shim.Error(err.Error())
	}

	value := []byte{0x00}
	stub.PutState(NameIndexKey, value)

// ================
	return shim.Success(nil)
}

// 得到某一用户的交易
func  (t *SimpleChaincode) getTransactionByUserID(stub shim.ChaincodeStubInterface, args []string) pb.Response  {
	fmt.Println("0x03 进入了信息时代")
	if len(args) < 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}

	getIndex := args[1:]
	fmt.Println("we get Index", getIndex)
	// Query the TransactionObject index by color
	// This will execute a key range query on all keys starting with 'color'
	transactionResultsIterator, err := stub.GetStateByPartialCompositeKey("TransactionObject", []string{"0","0"})
	if err != nil {
		return shim.Error(err.Error())
	}
	defer transactionResultsIterator.Close()

	// Iterate through result set and for each marble found, transfer to newOwner
	bArrayMemberAlreadyWritten := false
	var buffer bytes.Buffer
	buffer.WriteString("[")
	for transactionResultsIterator.HasNext() {
		// Note that we don't get the value (2nd return variable), we'll just get the marble name from the composite key
		queryResponse, err := transactionResultsIterator.Next()
		if err != nil {
			return shim.Error(err.Error())
		}
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}
		buffer.WriteString("{\"Key\":")
		buffer.WriteString("\"")
		buffer.WriteString(queryResponse.Key)
		buffer.WriteString("\"")

		buffer.WriteString(", \"Record\":")
		// Record is a JSON object, so we write as-is
		fmt.Println("queryUserID", queryResponse)
		buffer.WriteString("}")
		bArrayMemberAlreadyWritten = true


	}
	buffer.WriteString("}")

	return shim.Success(buffer.Bytes())
}