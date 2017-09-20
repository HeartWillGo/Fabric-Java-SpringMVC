package main

import (
	"io/ioutil"
	"fmt"
	"bytes"
	"time"
	"math/rand"
	"strconv"
	"encoding/json"
	"os"
)

func RandStr(strlen int) string {
	rand.Seed(time.Now().Unix())
	data := make([]byte, strlen)
	var num int
	for i := 0; i < strlen; i++ {
		num = rand.Intn(57) + 65
		for {
			if num>90 && num<97 {
				num = rand.Intn(57) + 65
			} else {
				break
			}
		}
		data[i] = byte(num)
	}
	return string(data)
}

func RandInt(strlen int) string {
	rand.Seed(time.Now().Unix())

	var data string
	var num int
	for i := 0; i < strlen; i++ {
		num = rand.Intn(57) + 65

		data += string(num)
	}
	return data
}

func generate_transdata(number int ) {
	var buffer bytes.Buffer
	buffer.WriteString("[")
	AlreadyWrite := false

	for i:=0; i < number; i++ {
		if AlreadyWrite == true {
			buffer.WriteString(",")
		}
		var transaction Transaction
		var idx string
		idx = strconv.Itoa(i)
		transaction.Transactionid =idx
		transaction.Transactiondate = time.Now().Unix()
		transaction.Parentorder =  idx
		transaction.Suborder =  idx
		transaction.Payid =  idx
		transaction.Transtype =  idx
		transaction.Fromtype = 1
		transaction.Fromid =  idx
		transaction.Totype = 1
		transaction.Toid = RandStr(8)
		transaction.Productid = idx
		transaction.Productinfo = "wegood"+"i%3"
		transaction.Organizationid = "pingan"
		transaction.Amount = 4
		transaction.Price = 9
		transactionBytes, _ := json.Marshal(transaction)
		buffer.WriteString(string(transactionBytes))
		AlreadyWrite = true
	}
	buffer.WriteString("]")
	err := ioutil.WriteFile("../testdata/transaction.json", buffer.Bytes(), 0644)
	if err != nil {
		fmt.Println("not success is failed, am I right?")
	}
}
func toJson(transaction interface{}) string {
	bytes, err := json.Marshal(transaction)
	if err != nil {
		fmt.Println(err.Error())
		os.Exit(1)
	}

	return string(bytes)
}

func (transaction Transaction) toString() string {
	return toJson(transaction)
}



func getTrans() []Transaction {
	raw, err := ioutil.ReadFile("../testdata/transaction.json")
	if err != nil {
		fmt.Println(err.Error())
		os.Exit(1)
	}

	fmt.Println("raw",string(raw))
	var c []Transaction
	json.Unmarshal(raw, &c)
	return c
}
