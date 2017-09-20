package main

import (
	//"github.com/hyperledger/fabric/core/chaincode/shim"
	//pb "github.com/hyperledger/fabric/protos/peer"

	"encoding/json"
	"fmt"
	"time"
)


type Asset struct {
	StatisticDate   string `json:"statistic_date"`
	TradingEntityID string `json:"trading_entity_id"`
	TransactionNum  int `json:"transaction_num"`
	AssetType       string `json:"asset_type"`
	AssetInfo       string `json:"asset_info"`
	TradeStartTime  int64    `json:"trade_start_time"`
	TradeEndTime    int64    `json:"trade_end_time"`
	AssetBalance    float64    `json:"asset_balance"`
	AssetIncome     float64    `json:"asset_income"`
	AssetOutcome    float64    `json:"asset_outcome"`
	OrganizatonMap map[string]*OrganizationAsset `json:"organization_Map"`
}
type OrganizationAsset struct {
	ID             string  `json:"id"`
	Type           int      `json:"type"`
	TransactionNum int64    `json:"transactionnum"`
	TradestartTime int64    `json:"tradestarttime"`
	TradeendTime   int64    `json:"tradeendtime"`
	Balance        float64    `json:"balance"`
	Outcome        float64    `json:"outcome"`
	Income         float64    `json:"income"`

	ProductMap     map[string]*ProductAsset `json:"productmap"`

}
type ProductAsset struct {
	ID             string `json:"id"`
	Tradestarttime int64    `json:"tradestarttime"`
	Tradeendtime   int64    `json:"tradeendtime"`
	TransactionNum int64  `json:"transactionum"`
	Balance        float64    `json:"balance"`
	Outcome        float64    `json:"outcome"`
	Income         float64    `json:"income"`
}

type RecordTransaction struct {
	Key    string `json:"Key"`
	Record Transaction `json:"Record"`
}
func computeAssetByTransacitonid(statisticID string,  transactionBytes []byte) Asset {
	var asset Asset
	var recordTransaction []RecordTransaction
	asset.TradingEntityID = statisticID

	err := json.Unmarshal(transactionBytes, &recordTransaction)
	if err != nil {
		fmt.Println(err.Error())
	}
	asset.StatisticDate = fmt.Sprintf("%v", time.Now().Unix())
	AlreadyCreateOrg := false
	AlreadyCreateProductMap := false
	for _, record := range recordTransaction {
		//用户的视角 user -----> organizaition
		//         organization ------> user
		//         organization ------
		tran := record.Record
		if tran.Fromid == asset.TradingEntityID {
			asset.AssetIncome += tran.Amount * tran.Price
		} else if tran.Toid == asset.TradingEntityID {
			asset.AssetOutcome += tran.Amount * tran.Price
		}
		if (asset.AssetIncome - asset.AssetOutcome) < 0 {
			fmt.Println("negative")
		}
		asset.TradeStartTime = findMin(asset.TradeStartTime, tran.Transactiondate)
		asset.TradeEndTime = findMax(asset.TradeEndTime, tran.Transactiondate)
		asset.TransactionNum += 1
		//机构
		if AlreadyCreateOrg == false {
			asset.OrganizatonMap = make(map[string]*OrganizationAsset)

		}
		AlreadyCreateOrg = true
		_, ok := asset.OrganizatonMap[tran.Organizationid]
		if !ok {
			asset.OrganizatonMap[tran.Organizationid] = &OrganizationAsset{ID: tran.Organizationid}
		}
		//像这种情况orgAsset如果不存在，对其操作后，orgAsset是否立即存在
		asset.OrganizatonMap[tran.Organizationid].TradestartTime = findMin(asset.OrganizatonMap[tran.Organizationid].TradestartTime, tran.Transactiondate)
		//产品
		asset.OrganizatonMap[tran.Organizationid].TradeendTime = findMax(asset.OrganizatonMap[tran.Organizationid].TradeendTime, tran.Transactiondate)

		//记录机构的交易数据
		if statisticID == tran.Fromid {

			asset.OrganizatonMap[tran.Organizationid].Outcome += tran.Amount * tran.Price
		} else if statisticID == tran.Toid {
			asset.OrganizatonMap[tran.Organizationid].Income += tran.Amount * tran.Price
		}
		asset.OrganizatonMap[tran.Organizationid].TransactionNum += 1

		if AlreadyCreateProductMap  == false {
			asset.OrganizatonMap[tran.Organizationid].ProductMap = make(map[string]*ProductAsset)
		}
		AlreadyCreateProductMap = true


		//记录产品的数据
		_, ok = asset.OrganizatonMap[tran.Organizationid].ProductMap[tran.Productid]
		if !ok {

			asset.OrganizatonMap[tran.Organizationid].ProductMap[tran.Productid] = &ProductAsset{ID: tran.Productid}
		}

		asset.OrganizatonMap[tran.Organizationid].ProductMap[tran.Productid].Tradeendtime   = findMax(asset.OrganizatonMap[tran.Organizationid].ProductMap[tran.Productid].Tradeendtime, tran.Transactiondate)
		asset.OrganizatonMap[tran.Organizationid].ProductMap[tran.Productid].Tradestarttime = findMin(asset.OrganizatonMap[tran.Organizationid].ProductMap[tran.Productid].Tradestarttime, tran.Transactiondate)
		if statisticID == tran.Fromid {
			asset.OrganizatonMap[tran.Organizationid].ProductMap[tran.Productid].Outcome += tran.Amount * tran.Price
		} else if statisticID == tran.Toid {
			asset.OrganizatonMap[tran.Organizationid].ProductMap[tran.Productid].Income += tran.Amount * tran.Price

		}
		asset.OrganizatonMap[tran.Organizationid].ProductMap[tran.Productid].TransactionNum += 1


	}
	asset.AssetBalance = asset.AssetIncome - asset.AssetOutcome
	return asset

}

func findMax(num1 int64, num2 int64) int64 {
	if num1 > num2  {
		return num1
	} else {
		return num2
	}
}
func findMin(num1 int64, num2 int64) int64 {
	if num1 < num2 && num1 != 0 {
		return num1
	} else {
		return num2
	}
}