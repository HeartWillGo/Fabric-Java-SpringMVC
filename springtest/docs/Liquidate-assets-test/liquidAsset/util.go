package main

import (
	"encoding/json"
	"fmt"
	"math/rand"
	"os"
	"time"
)

func RandStr(strlen int) string {
	rand.Seed(time.Now().Unix())
	data := make([]byte, strlen)
	var num int
	for i := 0; i < strlen; i++ {
		num = rand.Intn(57) + 65
		for {
			if num > 90 && num < 97 {
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
