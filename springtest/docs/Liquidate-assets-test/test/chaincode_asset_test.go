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
package test

import (
	"fmt"
	"testing"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	"encoding/json"
)



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
func checkInvokeUser(t *testing.T, stub *shim.MockStub, args [][]byte, checkargs string) {

	res := stub.MockInvoke("1", args)
	if res.Status != shim.OK {
		fmt.Println("Invoke", args, "failed", string(res.Message))
		t.FailNow()
	}
	switch  {
	case "CreateUser" == string(args[1]):
		fmt.Println("start test CreateUser")

	case "getUser" == string(args[1]):
		if string(res.GetPayload()) == checkargs {
			fmt.Println("getUser success")
		} else {
			t.FailNow()
		}
	case "getUserAsset" == string(args[1]):
		fmt.Println("start test getUserAsset")
		fmt.Println("UserAsset", string(res.GetPayload()))

	case "getUserOrgProductid" == string(args[1]):
		fmt.Println("start test getUserOrgProductid")
		fmt.Println("getUserOrgProductid", string(res.GetPayload()))

	case "getUserAllProduct" == string(args[1]):
		fmt.Println("start test getUserAllProduct")
		fmt.Println("getUserAllProduct", string(res.GetPayload()))

	case "getTransactionByUserID" == string(args[1]):
		fmt.Println("start test getTransactionByUserID")
		fmt.Println("transactionByUserID", string(res.GetPayload()))
	case "getUserFromOrganizationAsset" == string(args[1]):
		fmt.Println("start test getUserFromOrganizationAsset")
		fmt.Println("getUserFromOrganizationAsset", string(res.GetPayload()))

	}

}

func checkInvokeTransaction(t *testing.T, stub *shim.MockStub, args [][]byte, checkargs string) {
	res := stub.MockInvoke("1", args)
	if res.Status != shim.OK {
		fmt.Println("Invoke", args, "failed", string(res.Message))
		t.FailNow()
	}
	switch {
	case "Transaction" == string(args[1]):
		fmt.Println("start Transaction")
	case "getTransactionByID" == string(args[1]):

		if string(res.GetPayload()) != checkargs {
			t.FailNow()
		}else {
			fmt.Println("getTransactionByID success")
		}

	case "getTransactionByTransactionidRange" == string(args[1]):
		fmt.Println("start test getTransactionByTransactionidRange")
		fmt.Println("getTransactionByTransactionidRange", string(res.GetPayload()))


	case "getTransactionByOrganizationid" == string(args[1]):
		fmt.Println("start test getTransactionByOrganizationid")
		fmt.Println("getTransactionByOrganizationid", string(res.GetPayload()))
	case "getOrganizationProduct" == string(args[1]):
		fmt.Println("start test getOrganizationProduct")
		fmt.Println("getOrganizationProduct", string(res.GetPayload()))
	}
}

func checkInvokeProduct(t *testing.T, stub *shim.MockStub, args [][]byte, checkargs string) {
	res := stub.MockInvoke("1", args)
	if res.Status != shim.OK {
		fmt.Println("Invoke", args, "failed", string(res.Message))
		t.FailNow()
	}
	switch {
	case "CreateProduct" == string(args[1]):
		fmt.Println("start teste CreateProduct")
		fmt.Println("CreateProduct", string(res.GetPayload()))
	case "getProduct" == string(args[1]):
		fmt.Println("start teste getProduct")
		if string(res.GetPayload()) != checkargs {
			t.FailNow()
		} else {
			fmt.Println("getProduct success")
		}
	case "getProductAsset" == string(args[1]):
		fmt.Println("start teste getProductAsset")
		fmt.Println("getProductAsset", string(res.GetPayload()))

	case "getProductOneUser" == string(args[1]):
		fmt.Println("start teste getProductOneUser")
		fmt.Println("getProductOneUser", string(res.GetPayload()))

	case "getProductAllUser" == string(args[1]):
		fmt.Println("start teste getProductAllUser")
		fmt.Println("getProductAllUser", string(res.GetPayload()))

	case "getProductTransactionByProductID" == string(args[1]):
		fmt.Println("start teste getProductTransactionByProductID")
		fmt.Println("getProductTransactionByProductID", string(res.GetPayload()))

	case "getOrganizationProduct" == string(args[1]):
		fmt.Println("start test getOrganizationProduct")
		fmt.Println("getOrganizationProduct", string(res.GetPayload()))
	}
}
func checkInvokeOrganization(t *testing.T, stub *shim.MockStub, args [][]byte, checkargs string) {
	res := stub.MockInvoke("1", args)
	if res.Status != shim.OK {
		fmt.Println("Invoke", args, "failed", string(res.Message))
		t.FailNow()
	}
	switch {


	case "createOrganization" == string(args[1]):
		fmt.Println("start teste createOrganization")
		fmt.Println("createOrganization", string(res.GetPayload()))
	case "getOrganization" == string(args[1]):
		if string(res.GetPayload()) != checkargs {
			t.FailNow()
		}else {
			fmt.Println("getOrganization success")
	}
	case "getTransactionByOrganizationid" == string(args[1]):
		fmt.Println("start test getTransactionByOrganizationid")
		fmt.Println("getTransactionByOrganizationid", string(res.GetPayload()))

	case "getOrganizationProduct" == string(args[1]):
		fmt.Println("start teste getOrganizationProduct")
		fmt.Println("getOrganizationProduct", string(res.GetPayload()))

	case "getOrganizationAsset" == string(args[1]):
		fmt.Println("start teste getOrganizationAsset")
		fmt.Println("getOrganizationAsset", string(res.GetPayload()))

	case "getOrganizationUser" == string(args[1]):
		fmt.Println("start teste getOrganizationUser")
		fmt.Println("getOrganizationUser", string(res.GetPayload()))

	}
}



func getALLToHyperledger(t *testing.T, stub *shim.MockStub){
	Users := getUsers()
	Products := getProducts()
	Organizations := getOrganizations()
	Transactions := getTrans()


	trans_map := make(map[string][]byte)
	for _, tran := range Transactions {
		tranBytes, err:= json.Marshal(tran)
		if err != nil {
			t.Fail()
		}
		trans_map[tran.Transactionid] = tranBytes
		checkInvokeTransaction(t, stub, [][]byte{[]byte("invoke"), []byte("Transaction"), tranBytes}, string(tranBytes))
		checkInvokeTransaction(t, stub, [][]byte{[]byte("invoke"), []byte("getTransactionByID"), []byte(tran.Transactionid)}, string(tranBytes))

	}
	for _, user := range Users  {
		userBytes, err := json.Marshal(user)
		if err != nil {
			fmt.Println("marshal wrong")
		}
		checkInvokeUser(t, stub, [][]byte{[]byte("invoke"), []byte("CreateUser"), userBytes}, string(userBytes))
		checkInvokeUser(t, stub, [][]byte{[]byte("invoke"), []byte("getUser"), []byte(user.ID)}, string(userBytes))
	}

	for _, product := range Products {
		productBytes, err := json.Marshal(product)
		if err != nil {
			fmt.Println(err.Error())
		}
		checkInvokeProduct(t, stub, [][]byte{[]byte("invoke"), []byte("CreateProduct"), productBytes}, string(productBytes))
		checkInvokeProduct(t, stub, [][]byte{[]byte("invoke"), []byte("getProduct"), []byte(product.Productid)}, string(productBytes))

	}
	for _, organization := range Organizations {
		organizationBytes, err := json.Marshal(organization)
		if err != nil {
			fmt.Println(err.Error())
		}
		checkInvokeOrganization(t, stub, [][]byte{[]byte("invoke"), []byte("createOrganization"), organizationBytes}, string(organizationBytes))
		checkInvokeOrganization(t, stub, [][]byte{[]byte("invoke"), []byte("getOrganization"), []byte(organization.OrganizationID)}, string(organizationBytes))

	}
}


func TestInit(t *testing.T) {
	scc := new(SimpleChaincode)
	stub := shim.NewMockStub("asset", scc)

	str0 := `this we arrive the init function`
	checkInit(t, stub, [][]byte{[]byte("init"), []byte(str0)})

	checkState(t, stub, "qwertyuiop", str0)
}
func TestUser (t *testing.T) {
	scc := new(SimpleChaincode)
	stub := shim.NewMockStub("asset", scc)
	str0 := `this we arrive the init function`

	checkInit(t, stub, [][]byte{[]byte("init"), []byte(str0)})
	getALLToHyperledger(t, stub)


	checkInvokeUser(t, stub, [][]byte{[]byte("invoke"), []byte("getTransactionByUserID"),[]byte("userid0")} , string("w43"))
	checkInvokeUser(t, stub, [][]byte{[]byte("invoke"), []byte("getUserAsset"), []byte("userid0")}, string("1"))
	checkInvokeUser(t, stub, [][]byte{[]byte("invoke"), []byte("getUserAllProduct"), []byte("userid0")}, string(""))
	checkInvokeUser(t, stub, [][]byte{[]byte("invoke"), []byte("getUserOrgProductid"),  []byte("pingan"), []byte("userid0")}, string(""))
	checkInvokeUser(t, stub, [][]byte{[]byte("invoke"), []byte("getUserFromOrganizationAsset"),  []byte("pingan"),  []byte("userid0")}, string(""))

}

func TestTransaction (t *testing.T) {

	scc := new(SimpleChaincode)
	stub := shim.NewMockStub("asset", scc)
	str0 := `this we arrive the init function`

	checkInit(t, stub, [][]byte{[]byte("init"), []byte(str0)})
	getALLToHyperledger(t, stub)




	checkInvokeTransaction(t, stub, [][]byte{[]byte("invoke"), []byte("getTransactionByTransactionidRange"), []byte("transactionid0"),[]byte("transactionid4")}, string("1"))


}

func TestProduct (t *testing.T) {
	scc := new(SimpleChaincode)
	stub := shim.NewMockStub("asset", scc)
	str0 := `this we arrive the init function`

	checkInit(t, stub, [][]byte{[]byte("init"), []byte(str0)})
	getALLToHyperledger(t, stub)

	checkInvokeProduct(t, stub, [][]byte{[]byte("invoke"), []byte("getProductTransactionByProductID"), []byte("productid0")}, string("d"))
	checkInvokeProduct(t, stub, [][]byte{[]byte("invoke"), []byte("getProductAsset"), []byte("productid0")}, string("d"))
	checkInvokeProduct(t, stub, [][]byte{[]byte("invoke"), []byte("getProductAllUser"), []byte("productid0")}, string("d"))
	checkInvokeProduct(t, stub, [][]byte{[]byte("invoke"), []byte("getProductOneUser"),[]byte("12324"), []byte("productid0")}, string("d"))


}

func TestOrganization (t *testing.T) {
	scc := new(SimpleChaincode)
	stub := shim.NewMockStub("asset", scc)
	str0 := `this we arrive the init function`

	checkInit(t, stub, [][]byte{[]byte("init"), []byte(str0)})
	getALLToHyperledger(t, stub)

	checkInvokeOrganization(t, stub, [][]byte{[]byte("invoke"), []byte("getTransactionByOrganizationid"), []byte("pingan")}, string("d"))
	checkInvokeOrganization(t, stub, [][]byte{[]byte("invoke"), []byte("getOrganizationProduct"), []byte("pingan")}, string("d"))
	checkInvokeOrganization(t, stub, [][]byte{[]byte("invoke"), []byte("getOrganizationAsset"), []byte("pingan")}, string("d"))
	checkInvokeOrganization(t, stub, [][]byte{[]byte("invoke"), []byte("getOrganizationUser"), []byte("pingan")}, string("d"))

}


func TestGenerateDate(t *testing.T) {
	generate_userData(9)
	generate_transdata(9)
	generate_productData(9)
	generate_organizationData(9)
}


