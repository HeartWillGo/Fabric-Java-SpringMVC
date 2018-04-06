package main

import (
	//"github.com/hyperledger/fabric/core/chaincode/shim"
	//pb "github.com/hyperledger/fabric/protos/peer"
	"github.com/hyperledger/fabric/core/chaincode/shim"

	"encoding/json"
	"fmt"
	"time"
)

type UserAsset struct {
	StatisticDate   string                        `json:"statistic_date"`
	TradingEntityID string                        `json:"trading_entity_id"`
	TransactionNum  int                           `json:"transaction_num"`
	AssetType       string                        `json:"asset_type"`
	AssetInfo       string                        `json:"asset_info"`
	TradeStartTime  int64                         `json:"trade_start_time"`
	TradeEndTime    int64                         `json:"trade_end_time"`
	AssetBalance    float64                       `json:"asset_balance"`
	AssetIncome     float64                       `json:"asset_income"`
	AssetOutcome    float64                       `json:"asset_outcome"`
	OrganizatonMap  map[string]*OrganizationAsset `json:"organization_Map"`
	ProductMap      map[string]*ProductAsset      `json:"productmap"`
}
type OrganizationAsset struct {
	ID            string `json:"id"`
	StatisticDate string `json:"statistic_date"`

	Type           int     `json:"type"`
	TransactionNum int64   `json:"transactionnum"`
	TradestartTime int64   `json:"tradestarttime"`
	TradeendTime   int64   `json:"tradeendtime"`
	Balance        float64 `json:"balance"`
	Outcome        float64 `json:"outcome"`
	Income         float64 `json:"income"`

	ProductMap map[string]*ProductAsset `json:"productmap"`
	UserMap    map[string]*UserAsset    `json:"asset"`
}
type ProductAsset struct {
	ID             string                `json:"id"`
	StatisticDate  string                `json:"statistic_date"`
	Tradestarttime int64                 `json:"tradestarttime"`
	Tradeendtime   int64                 `json:"tradeendtime"`
	TransactionNum int64                 `json:"transactionum"`
	Balance        float64               `json:"balance"`
	Outcome        float64               `json:"outcome"`
	Income         float64               `json:"income"`
	UserMap        map[string]*UserAsset `json:"asset"`
}

type RecordTransaction struct {
	Key    string      `json:"Key"`
	Record Transaction `json:"Record"`
}

func computeAssetByUserID(statisticID string, transactionBytes []byte) UserAsset {
	var asset UserAsset
	var recordTransaction []RecordTransaction
	asset.TradingEntityID = statisticID
	fmt.Println(string(transactionBytes))
	err := json.Unmarshal(transactionBytes, &recordTransaction)
	if err != nil {

		shim.Error("it wrong")
	}

	asset.StatisticDate = fmt.Sprintf("%v", time.Now().Unix())
	asset.OrganizatonMap = make(map[string]*OrganizationAsset)
	AlreadyCreateProductMap := make(map[string]bool)


	for _, record := range recordTransaction {
		//用户的视角 user -----> organizaition
		//         organization ------> user
		//         organization ------
		tran := record.Record
		if tran.Fromid == asset.TradingEntityID {
			asset.AssetIncome += tran.Amount * tran.Price

		} else if  tran.Toid == asset.TradingEntityID {
			asset.AssetOutcome += tran.Amount * tran.Price
		}
		if (asset.AssetIncome - asset.AssetOutcome) < 0 {
			shim.Error("negative")
		}
		asset.TradeStartTime = findMin(asset.TradeStartTime, tran.Transactiondate)
		asset.TradeEndTime =   findMax(asset.TradeEndTime, tran.Transactiondate)
		asset.TransactionNum += 1
		//机构

		_, ok := asset.OrganizatonMap[tran.Organizationid]
		if !ok {
			asset.OrganizatonMap[tran.Organizationid] = &OrganizationAsset{ID: tran.Organizationid}
		}
		//像这种情况orgAsset如果不存在，对其操作后，orgAsset是否立即存在
		//asset.OrganizatonMap[tran.Organizationid].TradestartTime = findMin(asset.OrganizatonMap[tran.Organizationid].TradestartTime, tran.Transactiondate)
		//产品
		//asset.OrganizatonMap[tran.Organizationid].TradeendTime = findMax(asset.OrganizatonMap[tran.Organizationid].TradeendTime, tran.Transactiondate)

		//记录机构的交易数据
		if statisticID == tran.Fromid {

			asset.OrganizatonMap[tran.Organizationid].Outcome += tran.Amount * tran.Price
		} else if statisticID == tran.Toid {
			asset.OrganizatonMap[tran.Organizationid].Income += tran.Amount * tran.Price
		}
		asset.OrganizatonMap[tran.Organizationid].TransactionNum += 1


		if AlreadyCreateProductMap[tran.Organizationid] == false {
			asset.OrganizatonMap[tran.Organizationid].ProductMap = make(map[string]*ProductAsset)
			AlreadyCreateProductMap[tran.Organizationid] = true

		}

		//记录产品的数据
		_, ok = asset.OrganizatonMap[tran.Organizationid].ProductMap[tran.Productid]
		if !ok {
			asset.OrganizatonMap[tran.Organizationid].ProductMap[tran.Productid] = &ProductAsset{ID: tran.Productid}
		}

		asset.OrganizatonMap[tran.Organizationid].ProductMap[tran.Productid].Tradeendtime =   findMax(asset.OrganizatonMap[tran.Organizationid].ProductMap[tran.Productid].Tradeendtime, tran.Transactiondate)
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
func computeProductSaleInformation(transactionBytes []byte) ProductAsset {
	var productAsset ProductAsset
	var recordTransaction []RecordTransaction

	err := json.Unmarshal(transactionBytes, &recordTransaction)
	if err != nil {
		fmt.Println(err.Error())
	}
	productAsset.StatisticDate = fmt.Sprintf("%v", time.Now().Unix())

	for _, record := range recordTransaction {
		productAsset.TransactionNum += 1
		tran := record.Record
		productAsset.Balance += tran.Amount * tran.Price
		productAsset.ID = tran.Productid
		productAsset.Tradestarttime = findMin(productAsset.Tradestarttime, tran.Transactiondate)
		productAsset.Tradeendtime = findMax(productAsset.Tradeendtime, tran.Transactiondate)
	}
	return productAsset

}

func computeProductAllUser(transactionBytes []byte) ProductAsset {

	var recordTransaction []RecordTransaction
	var productAsset ProductAsset
	productAsset.UserMap = make(map[string]*UserAsset)

	err := json.Unmarshal(transactionBytes, &recordTransaction)
	if err != nil {
		fmt.Println(err.Error())
	}
	for _, record := range recordTransaction {
		tran := record.Record

		_, ok := productAsset.UserMap[tran.Toid]
		if ok == false {
			productAsset.UserMap[tran.Toid] = &UserAsset{TradingEntityID: tran.Toid}
			productAsset.ID = tran.Productid
			productAsset.StatisticDate = fmt.Sprintf("%v", time.Now().Unix())

		}
		productAsset.TransactionNum += 1
		productAsset.Balance += tran.Amount * tran.Price
		productAsset.UserMap[tran.Toid].AssetBalance += tran.Amount * tran.Price
		productAsset.UserMap[tran.Toid].TransactionNum += 1
	}

	return productAsset

}

func computeOrgnazitionAllProduct(transactionBytes []byte) OrganizationAsset {

	var organizationAsset OrganizationAsset
	var recordTransaction []RecordTransaction
	err := json.Unmarshal(transactionBytes, &recordTransaction)
	if err != nil {
		fmt.Println(err.Error())
	}
	organizationAsset.StatisticDate = fmt.Sprintf("%v", time.Now().Unix())
	organizationAsset.ProductMap = make(map[string]*ProductAsset)
	AlreadyCreateUser := false
	for _, record := range recordTransaction {
		tran := record.Record

		_, ok := organizationAsset.ProductMap[tran.Productid]
		if !ok {
			organizationAsset.ProductMap[tran.Productid] = &ProductAsset{ID: tran.Productid}
			organizationAsset.ID = tran.Organizationid
		}
		organizationAsset.TransactionNum += 1
		organizationAsset.Balance += tran.Amount * tran.Price

		organizationAsset.ProductMap[tran.Productid].TransactionNum += 1
		organizationAsset.ProductMap[tran.Productid].Balance += tran.Amount * tran.Price

		if AlreadyCreateUser == false {
			//organizationAsset.ProductMap[tran.Organizationid].UserMap = make(map[string]*UserAsset)
		}
		AlreadyCreateUser = true
		//_, ok = organizationAsset.ProductMap[tran.Organizationid].UserMap[tran.Toid]
		//if !ok {
		//	organizationAsset.ProductMap[tran.Organizationid].UserMap[tran.Toid] = &UserAsset{TradingEntityID:tran.Toid}
		//}
		//
		//_, ok = organizationAsset.ProductMap[tran.Organizationid].UserMap[tran.Fromid]
		//if !ok {
		//	organizationAsset.ProductMap[tran.Organizationid].UserMap[tran.Fromid] = &UserAsset{TradingEntityID:tran.Fromid}
		//}
		//
		//organizationAsset.ProductMap[tran.Productid].UserMap[tran.Toid].AssetIncome += tran.Amount * tran.Price
		//
		//organizationAsset.ProductMap[tran.Productid].UserMap[tran.Fromid].AssetOutcome += tran.Amount * tran.Price
		//

	}
	organizationAssetBytes, err := json.Marshal(organizationAsset)
	if err != nil {
		fmt.Println("marshal userOperateProductMapBytes Wrong")
	}
	fmt.Println("organizationAssetBytes", string(organizationAssetBytes))

	return organizationAsset
}

func computeOrgnazitionAllUser(transactionBytes []byte) OrganizationAsset {

	var organizationAsset OrganizationAsset
	var recordTransaction []RecordTransaction
	err := json.Unmarshal(transactionBytes, &recordTransaction)
	if err != nil {
		fmt.Println(err.Error())
	}
	organizationAsset.StatisticDate = fmt.Sprintf("%v", time.Now().Unix())
	organizationAsset.UserMap = make(map[string]*UserAsset)
	AlreadyCreateUser := false
	for _, record := range recordTransaction {
		tran := record.Record

		_, ok := organizationAsset.UserMap[tran.Toid]
		if !ok {
			organizationAsset.UserMap[tran.Toid] = &UserAsset{TradingEntityID: tran.Toid}
			organizationAsset.ID = tran.Organizationid
		}
		_, ok = organizationAsset.UserMap[tran.Fromid]
		if !ok {
			organizationAsset.UserMap[tran.Fromid] = &UserAsset{TradingEntityID: tran.Fromid}
		}

		organizationAsset.TransactionNum += 1
		organizationAsset.Balance += tran.Amount * tran.Price

		organizationAsset.UserMap[tran.Toid].TransactionNum += 1
		organizationAsset.UserMap[tran.Toid].AssetIncome += tran.Amount * tran.Price

		organizationAsset.UserMap[tran.Fromid].TransactionNum += 1
		organizationAsset.UserMap[tran.Fromid].AssetOutcome += tran.Amount * tran.Price

		if AlreadyCreateUser == false {
			//organizationAsset.ProductMap[tran.Organizationid].UserMap = make(map[string]*UserAsset)
		}
		AlreadyCreateUser = true
		//_, ok = organizationAsset.ProductMap[tran.Organizationid].UserMap[tran.Toid]
		//if !ok {
		//	organizationAsset.ProductMap[tran.Organizationid].UserMap[tran.Toid] = &UserAsset{TradingEntityID:tran.Toid}
		//}
		//
		//_, ok = organizationAsset.ProductMap[tran.Organizationid].UserMap[tran.Fromid]
		//if !ok {
		//	organizationAsset.ProductMap[tran.Organizationid].UserMap[tran.Fromid] = &UserAsset{TradingEntityID:tran.Fromid}
		//}
		//
		//organizationAsset.ProductMap[tran.Productid].UserMap[tran.Toid].AssetIncome += tran.Amount * tran.Price
		//
		//organizationAsset.ProductMap[tran.Productid].UserMap[tran.Fromid].AssetOutcome += tran.Amount * tran.Price
		//

	}
	organizationAssetBytes, err := json.Marshal(organizationAsset)
	if err != nil {
		fmt.Println("marshal userOperateProductMapBytes Wrong")
	}
	fmt.Println("organizationAssetBytes", string(organizationAssetBytes))

	return organizationAsset
}

func findMax(num1 int64, num2 int64) int64 {
	if num1 > num2 {
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
