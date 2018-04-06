package main

import (
	"encoding/json"
	"fmt"

	"bytes"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

// 账本数据
type Transaction struct {

	//交易头部
	SID               string `json:"SID"`
	ReceiverSID       string `json:"ReceiverSID"`
	OriginSID         string `json:"OriginSID"`
	RequestSerial     string `json:"RequestSerial"`
	NextRequestSerial string `json:"NextRequestSerial"`
	Proposaltime      int64  `json:"Proposaltime"`
	//交易ID,区块链中的索引
	Transactionid   string `json:"transactionid"`
	Transactiondate int64  `json:"transactiondate"`
	Parentorder     string `json:"parentorder"`
	Suborder        string `json:"suborder"`
	Payid           string `json:"payid"`
	//交易双方
	Transtype string `json:"transtype"`
	Fromtype  int    `json:"fromtype"`
	Fromid    string `json:"fromid"`
	Totype    int    `json:"totype"`
	Toid      string `json:"toid"`
	//实际内容
	Productid      string  `json:"productid"`
	Productinfo    string  `json:"productinfo"`
	Organizationid string  `json:"organizationid"`
	Amount         float64 `json:"amount"`
	Price          float64 `json:"price"`
}




//交易信息入链,创建索引信息
//args[0] functionname string
//args[1] userid string
//args = []string{"Transaction", "json格式的交易数据"}
func (t *SimpleChaincode) Transaction(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("0x0301 Transaction")

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


	erf,_ := json.Marshal(transaction)
	fmt.Println("this is for ", string(erf))
	// 以下添加各种索引

	stub.GetTxTimestamp()
	value := []byte{0x00}

	// Fromid~Transactionid
	Fromid_Transactionid, err := stub.CreateCompositeKey("Fromid~Transactionid", []string{transaction.Fromid, transaction.Transactionid})
	if err != nil {
		return shim.Error(err.Error())
	}
	stub.PutState(Fromid_Transactionid, value)

	// Fromid~Productid~Transactionid
	Fromid_Productid_Transactionid, err := stub.CreateCompositeKey("Fromid~Productid~Transactionid", []string{transaction.Fromid,
		transaction.Productid, transaction.Transactionid})
	if err != nil {
		return shim.Error(err.Error())
	}
	stub.PutState(Fromid_Productid_Transactionid, value)

	// Fromid~Organizationid~Transactionid
	Fromid_Organizationid_Transactionid, err := stub.CreateCompositeKey("Fromid~Organizationid~Transactionid", []string{transaction.Fromid, transaction.Organizationid, transaction.Transactionid})
	if err != nil {
		return shim.Error(err.Error())
	}
	stub.PutState(Fromid_Organizationid_Transactionid, value)

	// Toid~Transactionid
	Toid_Transactionid, err := stub.CreateCompositeKey("Toid~Transactionid", []string{transaction.Toid, transaction.Transactionid})
	if err != nil {
		return shim.Error(err.Error())
	}
	stub.PutState(Toid_Transactionid, value)

	// Toid~Productid~Transactionid
	Toid_Productid_Transactionid, err := stub.CreateCompositeKey("Toid~Productid~Transactionid", []string{transaction.Toid, transaction.Productid, transaction.Transactionid})
	if err != nil {
		return shim.Error(err.Error())
	}
	stub.PutState(Toid_Productid_Transactionid, value)

	// Toid~Organizationid~Transactionid
	Toid_Organizationid_Transactionid, err := stub.CreateCompositeKey("Toid~Organizationid~Transactionid", []string{transaction.Toid, transaction.Organizationid, transaction.Transactionid})
	if err != nil {
		return shim.Error(err.Error())
	}
	stub.PutState(Toid_Organizationid_Transactionid, value)

	// Productid~Transactionid
	Productid_Transactionid, err := stub.CreateCompositeKey("Productid~Transactionid", []string{transaction.Productid, transaction.Transactionid})
	if err != nil {
		return shim.Error(err.Error())
	}
	stub.PutState(Productid_Transactionid, value)
	// Productid~Fromid
	Productid_Fromid, err := stub.CreateCompositeKey("Productid~Fromid", []string{transaction.Productid, transaction.Fromid})
	if err != nil {
		return shim.Error(err.Error())
	}
	stub.PutState(Productid_Fromid, value)
	// Productid~Toid
	Productid_Toid, err := stub.CreateCompositeKey("Productid~Toid", []string{transaction.Productid, transaction.Toid})
	if err != nil {
		return shim.Error(err.Error())
	}
	stub.PutState(Productid_Toid, value)

	// Organizationid~Transactionid
	Organizationid_Transactionid, err := stub.CreateCompositeKey("Organizationid~Transactionid", []string{transaction.Organizationid, transaction.Transactionid})
	if err != nil {
		return shim.Error(err.Error())
	}
	stub.PutState(Organizationid_Transactionid, value)

	// Organizationid~Fromid
	Organizationid_Fromid, err := stub.CreateCompositeKey("Organizationid~Fromid", []string{transaction.Organizationid, transaction.Fromid})
	if err != nil {
		return shim.Error(err.Error())
	}
	stub.PutState(Organizationid_Fromid, value)

	// Organizationid~Fromid~Productid
	Organizationid_Fromid_Productid, err := stub.CreateCompositeKey("Organizationid~Fromid~Productid", []string{transaction.Organizationid,
		transaction.Fromid, transaction.Productid})
	if err != nil {
		return shim.Error(err.Error())
	}
	stub.PutState(Organizationid_Fromid_Productid, value)

	// Organizationid~Toid
	Organizationid_Toid, err := stub.CreateCompositeKey("Organizationid~Toid", []string{transaction.Organizationid, transaction.Toid})
	if err != nil {
		return shim.Error(err.Error())
	}
	stub.PutState(Organizationid_Toid, value)

	// Organizationid~Toid~Productid
	Organizationid_Toid_Productid, err := stub.CreateCompositeKey("Organizationid~Toid~Productid", []string{transaction.Organizationid,
		transaction.Toid, transaction.Productid})
	if err != nil {
		return shim.Error(err.Error())
	}
	stub.PutState(Organizationid_Toid_Productid, value)

	// ================
	return shim.Success(nil)
}

// getTransactionByID 获取某笔交易
// args[0] functionname string
// args[1] userid string
func (t *SimpleChaincode) getTransactionByID(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("ex0302 getTransactionByID")

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

	return shim.Success(TransactionInfo)
}

//得到某一用户的所有交易,
//args[0] functionname string
//args[1] userid string
//args = []string {"getTransactionByUserID", "1"}
func (t *SimpleChaincode) getTransactionByUserID(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("0x0303 getTransactionByUserID")
	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}
	Fromid := args[1:]

	// Query the TransactionObject index by FromID
	// This will execute a key range query on all keys starting with 'Fromid'
	transactionFromidResultsIterator, err := stub.GetStateByPartialCompositeKey("Fromid~Transactionid", Fromid)
	if err != nil {
		return shim.Error(err.Error())
	}
	defer transactionFromidResultsIterator.Close()

	// Iterate through result set and for each marble found, transfer to newOwner
	bArrayMemberAlreadyWritten := false
	var buffer bytes.Buffer
	buffer.WriteString("[")

	for transactionFromidResultsIterator.HasNext() {
		// Note that we don't get the value (2nd return variable), we'll just get the marble name from the composite key
		queryResponse, err := transactionFromidResultsIterator.Next()
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
		if objectType != "Fromid~Transactionid" {
			return shim.Error("object is not we want %s" + Fromid[0])
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


	transactionToidResultsIterator, err := stub.GetStateByPartialCompositeKey("Toid~Transactionid",
		Fromid)
	if err != nil {
		return shim.Error("wrong")
	}
	defer transactionToidResultsIterator.Close()

	for transactionToidResultsIterator.HasNext() {
		// Note that we don't get the value (2nd return variable), we'll just get the marble name from the composite key
		queryResponse, err := transactionToidResultsIterator.Next()
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
		if objectType != "Toid~Transactionid" {
			fmt.Println("objectType", objectType)
			return shim.Error("object is not we want %s" + Fromid[0])
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
		fmt.Println("string(transactionBytes")
		buffer.WriteString(string(transactionBytes))
		buffer.WriteString("}")
		bArrayMemberAlreadyWritten = true
	}
	buffer.WriteString("]")
	fmt.Println("just make fromid, toid", buffer.String())
	return shim.Success(buffer.Bytes())
}

//args[0] functionname string
//args[1] userid string
//args = []string {"getTransactionByTransactionidRange", "startkey","endkey"}
func (t *SimpleChaincode) getTransactionByTransactionidRange(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	fmt.Println("0x04 getTransactionByTransactionidRange")
	if len(args) != 3 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}
	if len(args) < 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}

	startKey := args[1]
	endKey := args[2]

	resultsIterator, err := stub.GetStateByRange(startKey, endKey)
	if err != nil {
		return shim.Error(err.Error())
	}
	defer resultsIterator.Close()

	// buffer is a JSON array containing QueryResults
	var buffer bytes.Buffer
	buffer.WriteString("[")

	bArrayMemberAlreadyWritten := false
	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()
		if err != nil {
			return shim.Error(err.Error())
		}
		// Add a comma before array members, suppress it for the first array member
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}
		buffer.WriteString("{\"Key\":")
		buffer.WriteString("\"")
		buffer.WriteString(queryResponse.Key)
		buffer.WriteString("\"")

		buffer.WriteString(", \"Record\":")
		// Record is a JSON object, so we write as-is
		buffer.WriteString(string(queryResponse.Value))
		buffer.WriteString("}")
		bArrayMemberAlreadyWritten = true
	}
	buffer.WriteString("]")

	return shim.Success(buffer.Bytes())

}
