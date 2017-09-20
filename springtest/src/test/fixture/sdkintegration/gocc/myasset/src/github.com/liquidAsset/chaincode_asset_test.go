/*
Copyright IBM Corp. 2016 All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

		 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package main

import (
	"fmt"
	"testing"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	"encoding/json"
)
////交易内容
//type Transaction struct {
//	//交易的ID和
//	Transactionid   string `json:"transactionid"`
//	Transactiondate string `json:"transactiondate"`
//
//	Parentorder     string `json:"parentorder"`
//	Suborder        string `json:"suborder"`
//	Payid           string `json:"payid"`
//	//交易头部
//	Transtype       string `json:"transtype"`
//	Fromtype        int    `json:"fromtype"`
//	Fromid          string `json:"fromid"`
//	Totype          int    `json:"totype"`
//	Toid            string `json:"toid"`
//	//交易内容
//	Productid       string `json:"productid"`
//	Productinfo     string `json:"productinfo"`
//	Organizationid  string `json:"organizationid"`
//
//	Amount          int    `json:"amount"`
//	Price           int    `json:"price"`
//}

var chaincodeName = "aaset"

// chaincode_example05 looks like it wanted to return a JSON response to Query()
// it doesn't actually do this though, it just returns the sum value
func jsonResponse(name string, value string) string {
	return fmt.Sprintf("jsonResponse = \"{\"Name\":\"%v\",\"Value\":\"%v\"}", name, value)
}

func checkInit(t *testing.T, stub *shim.MockStub, args [][]byte) {
	res := stub.MockInit("1", args)
	if res.Status != shim.OK {
		fmt.Println("Init failed", string(res.Message))
		t.FailNow()
	}
}

func checkState(t *testing.T, stub *shim.MockStub, name string, expect string) {
	bytes := stub.State[name]
	if bytes == nil {
		fmt.Println("State", name, "failed to get value")
		t.FailNow()
	}
	if string(bytes) != expect {
		fmt.Println("State value", name, "was not", expect, "as expected")
		t.FailNow()
	}
}

func checkQuery(t *testing.T, stub *shim.MockStub, args [][]byte, expect string) {
	res := stub.MockInvoke("1", args)
	if res.Status != shim.OK {
		fmt.Println("Query", args, "failed", string(res.Message))
		t.FailNow()
	}
	if res.Payload == nil {
		fmt.Println("Query", args, "failed to get result")
		t.FailNow()
	}
	if string(res.Payload) != expect {
		fmt.Println("Query result ", string(res.Payload), "was not", expect, "as expected")
		t.FailNow()
	}
}

func checkInvoke(t *testing.T, stub *shim.MockStub, args [][]byte, checkargs string) {
	res := stub.MockInvoke("1", args)
	if res.Status != shim.OK {
		fmt.Println("Invoke", args, "failed", string(res.Message))
		t.FailNow()
	}
	switch {
	case "getTransactionByID" == string(args[1]):

		if string(res.GetPayload()) != checkargs {
				t.FailNow()
			}
	case "getTransactionByUserID" == string(args[1]):
		fmt.Println("start test getTransactionByUserID")
		fmt.Println("transactionByUserID")


	case "getUserAsset"	== string(args[1]):
		fmt.Println("start test getUserAsset")
		fmt.Println("UserAsset", string(res.GetPayload()))
	}
}



func TestInit(t *testing.T) {
	scc := new(SimpleChaincode)
	stub := shim.NewMockStub("ex05", scc)

	str0 := `this we arrive the init function`
	checkInit(t, stub, [][]byte{[]byte("init"), []byte(str0)})

	checkState(t, stub, "qwertyuiop", str0)
}

func TestTransaction (t *testing.T) {
	scc := new(SimpleChaincode)
	stub := shim.NewMockStub("ex05", scc)

	str0 := `this we arrive the init function`
	checkInit(t, stub, [][]byte{[]byte("init"), []byte(str0)})
	// generate_transdata(3)
	Transactions := getTrans()
	trans_map := make(map[string][]byte)

	for _, tran := range Transactions {
		tranBytes, err:= json.Marshal(tran)
		if err != nil {
			t.Fail()
		}
		trans_map[tran.Transactionid] = tranBytes
		checkInvoke(t, stub, [][]byte{[]byte("invoke"), []byte("Transaction"), tranBytes},string(tranBytes))
		checkInvoke(t, stub, [][]byte{[]byte("invoke"), []byte("getTransactionByID"), []byte(tran.Transactionid)}, string(tranBytes))
	}

	 checkInvoke(t, stub, [][]byte{[]byte("invoke"), []byte("getTransactionByUserID"), []byte("1")}, string(trans_map["1"]))
	 checkInvoke(t, stub, [][]byte{[]byte("invoke"), []byte("getUserAsset"), []byte("1")}, string(trans_map["1"]))

}





