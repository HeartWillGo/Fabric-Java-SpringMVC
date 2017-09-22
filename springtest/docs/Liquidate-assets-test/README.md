__
# Liquidate-assets
这是一个关于统计用户的区块链智能合约，机构，产品，用户之间的资产统计。
```
##主要函数
CreateUser #创建用户 
getUser #查询用户
CreateProduct #创建产品 
getProduct #得到产品
createOrganization #创建机构 
getOrganization #创建机构 


#TODO
WriteUser
WriteProduct
WriteOrganization 
[sorry]
##
getTransactionByUserID #根据UserID获取某个用户下的所有交易
getUserAsset #获取某一用户的资产详情
getUserAllProduct #得到该用户购买的所有产品
getUserOrgProductid #获取某个机构下产品的所有购买情况
getUserFromOrganizationAsset #获取用户在某个机构下资产详情

getProductTransactionByProductID #根据产品获取该产品的所有账本条目
getProductAsset #得到产品的资产信息 
getProductAllUser #得到产品下所有的用户购买详情 
getProductOneUser #得到产品下某个用户购买详情 

getTransactionByOrganizationid #得到机构的所有交易条目 
getOrganizationProduct #得到机构下的所有产品 
getOrganizationAsset #得到机构下所有的资产,与上一函数类似 
getOrganizationUser #得到机构下所有下用户的购买信息 

Transaction # 入账 
getTransactionByID #根据交易ID获取交易数据
getTransactionByTransactionidRange #获取在某个交易ID范围内的数据


```
##数据结构设计

###用户
```
//用户
type User struct {
	ID                 string `json:"id"`
	Name               string `json:"Name"`
	Identificationtype int    `json:"identificationtype"`
	Identification     string `json:"identification"`
	Sex                int `json:"sex"`
	Birthday           string `json:"birthday"`
	Bankcard           string `json:"bankcard"`
	Phonenumber        string `json:"phonenumber"`
	Token              string `json:"token"`
}
```
###资金类
```
ID: 银行卡号 
Amount: 卡上剩余金额
```
###产品类
```
//产品
type Product struct {
	Productid      string  `json:"productid"`
	Productname    string  `json:"productname"`
	Producttype    int     `json:"producttype"`
	Organizationid string  `json:"organizationid"`
	Amount         float64 `json:"amount"`
	Price          float64 `json:"price"`
}
```
###机构类
```
//机构
type Organization struct {
	OrganizationID   string `json:"organizationid"`   //机构id
	OrganizationName string `json:"organizationname"` //机构名称
	OrganizationType int    `json:"organizationtype"` //机构类型
}
```
###账本信息
```
// 账本数据
type Transaction struct {

	//交易头部
	SID               string `json:"SID"`
	ReceiverSID       string `json:"ReceiverSID"`
	OriginSID         string `json:"OriginSID"`
	RequestSerial     string `json:"RequestSerial"`
	NextRequestSerial string `json:"NextRequestSerial"`
	Proposaltime      int64   `json:"Proposaltime"`
	//交易ID,区块链中的索引
	Transactionid     string `json:"transactionid"`
	Transactiondate   int64    `json:"transactiondate"`
	Parentorder       string `json:"parentorder"`
	Suborder          string `json:"suborder"`
	Payid             string `json:"payid"`
	//交易双方
	Transtype         string `json:"transtype"`
	Fromtype          int    `json:"fromtype"`
	Fromid            string `json:"fromid"`
	Totype            int    `json:"totype"`
	Toid              string `json:"toid"`
	//实际内容
	Productid         string `json:"productid"`
	Productinfo       string `json:"productinfo"`
	Organizationid    string `json:"organizationid"`
	Amount            float64    `json:"amount"`
	Price             float64    `json:"price"`
}
```

##接口设计
```
CreateUser #创建用户 
request 参数: 
args[0]:
{
    "id": "userid8",
    "Name": "JBYNCsmE",
    "identificationtype": 1,
    "identification": "23342",
    "sex": 1,
    "birthday": "20340912",
    "bankcard": "123243",
    "phonenumber": "999999",
    "token": "8"
  }
response:
    nil 
```

```
getUser #查询用户
request 参数: 
args[0] : "user.ID"
response:
 {
        "id": "userid6",
        "Name": "JBYNCsmE",
        "identificationtype": 1,
        "identification": "23342",
        "sex": 1,
        "birthday": "20340912",
        "bankcard": "123243",
        "phonenumber": "999999",
        "token": "6"
      }

  
```

```
CreateProduct #创建产品 
request 参数: 
args[0]: 
{
    "productid": "productid0",
    "productname": "zhaocaibao",
    "producttype": 1,
    "organizationid": "pingan",
    "amount": 3,
    "price": 33
  }
  
response 参数: 
    nil
```
```
getProduct #得到产品
request: 
args[0] : product.Productid
response:
    {
        "productid": "productid0",
        "productname": "zhaocaibao",
        "producttype": 1,
        "organizationid": "pingan",
        "amount": 3,
        "price": 33
      }
```
```
createOrganization #创建机构 
request 参数: 
args[0]:
{
    "organizationid": "pingan0",
    "organizationname": "pingan",
    "organizationtype": 1
  }
  
response 参数: 
    nil
```
```
getOrganization #创建机构 
request 参数: 
args[0]: "organizaitonid"

response 参数: 
    {
        "organizationid": "pingan0",
        "organizationname": "pingan",
        "organizationtype": 1
      }
      
```


```
getTransactionByUserID #根据UserID获取某个用户下的所有交易
request
args[0]："userid" 
response ： 
[
  {
    "transactionid": "0",
    "transactiondate": 1505887743,
    "parentorder": "0",
    "suborder": "0",
    "payid": "0",
    "transtype": "0",
    "fromtype": 1,
    "fromid": "1",
    "totype": 1,
    "toid": "VjIwPrHi",
    "productid": "0",
    "productinfo": "wegoodi%3",
    "organizationid": "pingan",
    "amount": 4,
    "price": 9
  },
  {
    "transactionid": "1",
    "transactiondate": 1505887743,
    "parentorder": "1",
    "suborder": "1",
    "payid": "1",
    "transtype": "1",
    "fromtype": 1,
    "fromid": "1",
    "totype": 1,
    "toid": "VjIwPrHi",
    "productid": "1",
    "productinfo": "wegoodi%3",
    "organizationid": "pingan",
    "amount": 4,
    "price": 9
  },...
 ]


```
```
getUserAsset #获取某一用户的资产详情
request 参数： len(args) =1 
args[0]: "userid"
response:
{
    "statistic_date": "1505896172",
    "trading_entity_id": "1",
    "transaction_num": 3,
    "asset_type": "",
    "asset_info": "",
    "trade_start_time": 1505887743,
    "trade_end_time": 1505887743,
    "asset_balance": 108.12,
    "asset_income": 108.12,
    "asset_outcome": 0,
    "organization_Map": {
      "pingan": {
        "id": "pingan",
        "type": 0,
        "transactionnum": 3,
        "tradestarttime": 1505887743,
        "tradeendtime": 1505887743,
        "balance": 0,
        "outcome": 108.12,
        "income": 0,
        "productmap": {
          "0": {
            "id": "0",
            "tradestarttime": 1505887743,
            "tradeendtime": 1505887743,
            "transactionum": 1,
            "balance": 0,
            "outcome": 36,
            "income": 0
          },
          "1": {
            "id": "1",
            "tradestarttime": 1505887743,
            "tradeendtime": 1505887743,
            "transactionum": 1,
            "balance": 0,
            "outcome": 36,
            "income": 0
          },
          "2": {
            "id": "2",
            "tradestarttime": 1505887743,
            "tradeendtime": 1505887743,
            "transactionum": 1,
            "balance": 0,
            "outcome": 36.12,
            "income": 0
          }
          ...
        }
      }
    }
  }

```
```
getUserAllProduct #得到该用户购买的所有产品
request
args[0]:"userid"
response:
   [
     {
       "pingan": {
         "productid": "productid0",
         "productname": "zhaocaibao",
         "producttype": 1,
         "organizationid": "pingan",
         "amount": 3,
         "price": 33
       }
     {
       "productid": "productid1",
       "productname": "zhaocaibao",
       "producttype": 1,
       "organizationid": "pingan",
       "amount": 3,
       "price": 33
     },
     {
       "productid": "productid2",
       "productname": "zhaocaibao",
       "producttype": 1,
       "organizationid": "pingan",
       "amount": 3,
       "price": 33
     }
     }
   ]

```
```
getUserOrgProductid #获取某个机构下产品的所有购买情况
request
args[0]: "organizationid"
args[1]:"userid"

response
  [
   {
     "productid": "productid0",
     "productname": "zhaocaibao",
     "producttype": 1,
     "organizationid": "pingan",
     "amount": 3,
     "price": 33
   }
   {
     "productid": "productid1",
     "productname": "zhaocaibao",
     "producttype": 1,
     "organizationid": "pingan",
     "amount": 3,
     "price": 33
   }
   {
     "productid": "productid2",
     "productname": "zhaocaibao",
     "producttype": 1,
     "organizationid": "pingan",
     "amount": 3,
     "price": 33
   }
 ]
```
```
getUserFromOrganizationAsset #获取用户在某个机构下资产详情
 
request 
args[0]: "organizationid"
args[1]: "userid"

response 参数: 
{
  "id": "pingan",
  "statistic_date": "",
  "type": 0,
  "transactionnum": 3,
  "tradestarttime": 1506005289,
  "tradeendtime": 1506005289,
  "balance": 0,
  "outcome": 108,
  "income": 0,
  "productmap": {
    "productid0": {
      "id": "productid0",
      "statistic_date": "",
      "tradestarttime": 1506005289,
      "tradeendtime": 1506005289,
      "transactionum": 1,
      "balance": 0,
      "outcome": 36,
      "income": 0,
      "asset": null
    },
   ...
  },
  "asset": null
}

```
```
getProductTransactionByProductID #根据产品获取该产品的所有账本条目
request 参数: 
args[0]："productid"
response
 {
    "Key": "transactionid0",
    "Record": {
      "SID": "",
      "ReceiverSID": "",
      "OriginSID": "",
      "RequestSerial": "",
      "NextRequestSerial": "",
      "Proposaltime": 0,
      "transactionid": "transactionid0",
      "transactiondate": 1506005289,
      "parentorder": "0",
      "suborder": "0",
      "payid": "0",
      "transtype": "0",
      "fromtype": 1,
      "fromid": "userid0",
      "totype": 1,
      "toid": "JBYNCsmE",
      "productid": "productid0",
      "productinfo": "wegoodi%3",
      "organizationid": "pingan",
      "amount": 4,
      "price": 9
    }
  },
  {
    "Key": "transactionid6",
    "Record": {
      "SID": "",
      "ReceiverSID": "",
      "OriginSID": "",
      "RequestSerial": "",
      "NextRequestSerial": "",
      "Proposaltime": 0,
      "transactionid": "transactionid6",
      "transactiondate": 1506005289,
      "parentorder": "6",
      "suborder": "6",
      "payid": "6",
      "transtype": "6",
      "fromtype": 1,
      "fromid": "userid2",
      "totype": 1,
      "toid": "12324",
      "productid": "productid0",
      "productinfo": "wegoodi%3",
      "organizationid": "Account",
      "amount": 4,
      "price": 9
    }
    ...
  }



```
```
getProductAsset #得到产品的资产信息 
request 参数: 
args[0]："productid0" 

response 参数: 
{
  "id": "productid0",
  "statistic_date": "1506015539",
  "tradestarttime": 1506005289,
  "tradeendtime": 1506005289,
  "transactionum": 4,
  "balance": 144,
  "outcome": 0,
  "income": 0,
  "asset": null
}
```
```
getProductAllUser #得到产品下所有的用户购买详情 
request 参数:
args[0] : "productid0"
response 参数: 
{
  "id": "productid0",
  "statistic_date": "1506042797",
  "tradestarttime": 0,
  "tradeendtime": 0,
  "transactionum": 4,
  "balance": 144,
  "outcome": 0,
  "income": 0,
  "asset": {
    "12324": {
      "statistic_date": "",
      "trading_entity_id": "12324",
      "transaction_num": 1,
      "asset_type": "",
      "asset_info": "",
      "trade_start_time": 0,
      "trade_end_time": 0,
      "asset_balance": 36,
      "asset_income": 0,
      "asset_outcome": 0,
      "organization_Map": null,
      "productmap": null
    },
    "1234": {
      "statistic_date": "",
      "trading_entity_id": "1234",
      "transaction_num": 1,
      "asset_type": "",
      "asset_info": "",
      "trade_start_time": 0,
      "trade_end_time": 0,
      "asset_balance": 36,
      "asset_income": 0,
      "asset_outcome": 0,
      "organization_Map": null,
      "productmap": null
    } ...
  }
  
```

```
getProductOneUser #得到产品下某个用户购买详情 
request 参数:
args[1] : "userid0"
args[0] : "productid0"
response 参数: 
 {
  "statistic_date": "",
  "trading_entity_id": "12324",
  "transaction_num": 1,
  "asset_type": "",
  "asset_info": "",
  "trade_start_time": 0,
  "trade_end_time": 0,
  "asset_balance": 36,
  "asset_income": 0,
  "asset_outcome": 0,
  "organization_Map": null,
  "productmap": null
}
```
```
getTransactionByOrganizationid #得到机构的所有交易条目 
request 参数: 
args[0]: "organizationid"
response 参数: 
[
  {
    "Key": "transactionid0",
    "Record": {
      "SID": "",
      "ReceiverSID": "",
      "OriginSID": "",
      "RequestSerial": "",
      "NextRequestSerial": "",
      "Proposaltime": 0,
      "transactionid": "transactionid0",
      "transactiondate": 1506005289,
      "parentorder": "0",
      "suborder": "0",
      "payid": "0",
      "transtype": "0",
      "fromtype": 1,
      "fromid": "userid0",
      "totype": 1,
      "toid": "JBYNCsmE",
      "productid": "productid0",
      "productinfo": "wegoodi%3",
      "organizationid": "pingan",
      "amount": 4,
      "price": 9
    }
  },
  ...
]
```
```
getOrganizationProduct #得到机构下的所有产品 
request 参数: 
args[0]: "organizationid"
response 参数: 
{
  "id": "",
  "statistic_date": "1506043005",
  "type": 0,
  "transactionnum": 8,
  "tradestarttime": 0,
  "tradeendtime": 0,
  "balance": 288,
  "outcome": 0,
  "income": 0,
  "productmap": {
    "productid0": {
      "id": "productid0",
      "statistic_date": "",
      "tradestarttime": 0,
      "tradeendtime": 0,
      "transactionum": 3,
      "balance": 108,
      "outcome": 0,
      "income": 0,
      "asset": null
    },
    "productid1": {
      "id": "productid1",
      "statistic_date": "",
      "tradestarttime": 0,
      "tradeendtime": 0,
      "transactionum": 1,
      "balance": 36,
      "outcome": 0,
      "income": 0,
      "asset": null
    },
]
```


```
getOrganizationAsset #得到机构下所有的资产,与上一函数类似 
request 参数: 
args[0]: "organizationid"
response 参数: 
{
  "id": "pingan",
  "statistic_date": "1506043449",
  "type": 0,
  "transactionnum": 8,
  "tradestarttime": 0,
  "tradeendtime": 0,
  "balance": 288,
  "outcome": 0,
  "income": 0,
  "productmap": {
    "productid0": {
      "id": "productid0",
      "statistic_date": "",
      "tradestarttime": 0,
      "tradeendtime": 0,
      "transactionum": 3,
      "balance": 108,
      "outcome": 0,
      "income": 0,
      "asset": null
    },
    "productid1": {
      "id": "productid1",
      "statistic_date": "",
      "tradestarttime": 0,
      "tradeendtime": 0,
      "transactionum": 1,
      "balance": 36,
      "outcome": 0,
      "income": 0,
      "asset": null
    },
    
  "asset": null
}

]
```

```
getOrganizationUser #得到机构下所有下用户的购买信息 
request 参数: 
args[0]: "organizationid"
response 参数: 
{
  "id": "pingan",
  "statistic_date": "1506043449",
  "type": 0,
  "transactionnum": 8,
  "tradestarttime": 0,
  "tradeendtime": 0,
  "balance": 288,
  "outcome": 0,
  "income": 0,
  "productmap": null,
  "asset": {
    "userid0": {
      "statistic_date": "",
      "trading_entity_id": "1234",
      "transaction_num": 1,
      "asset_type": "",
      "asset_info": "",
      "trade_start_time": 0,
      "trade_end_time": 0,
      "asset_balance": 0,
      "asset_income": 36,
      "asset_outcome": 0,
      "organization_Map": null,
      "productmap": null
    },
    "userid1": {
      "statistic_date": "",
      "trading_entity_id": "userid0",
      "transaction_num": 3,
      "asset_type": "",
      "asset_info": "",
      "trade_start_time": 0,
      "trade_end_time": 0,
      "asset_balance": 0,
      "asset_income": 0,
      "asset_outcome": 108,
      "organization_Map": null,
      "productmap": null
    },
    ...
  }
}
```


```
Transaction # 入账 
request 参数 
args[0]:
 
{
    "SID": "txiddsf",
    "ReceiverSID": "234423",
    "OriginSID": "23423",
    "RequestSerial": "234",
    "NextRequestSerial": "243243",
    "Proposaltime": 1506005289,
    "transactionid": "transactionid7",
    "transactiondate": 1506005289,
    "parentorder": "7",
    "suborder": "7",
    "payid": "7",
    "transtype": "7",
    "fromtype": 1,
    "fromid": "userid2",
    "totype": 1,
    "toid": "1234",
    "productid": "productid0",
    "productinfo": "wegoodi%3",
    "organizationid": "pingan",
    "amount": 4,
    "price": 9
  }
  response:
        nil
```

```
getTransactionByID #根据交易ID获取数据
request
args[0]: "transactionid"

response 参数： 
  {
    "transactionid": "0",
    "transactiondate": 1505887743,
    "parentorder": "0",
    "suborder": "0",
    "payid": "0",
    "transtype": "0",
    "fromtype": 1,
    "fromid": "1",
    "totype": 1,
    "toid": "VjIwPrHi",
    "productid": "0",
    "productinfo": "wegoodi%3",
    "organizationid": "pingan",
    "amount": 4,
    "price": 9
  }
```
```
getTransactionByTransactionidRange #根据交易ID获取数据
request
args[0]: "transactionid0"
args[1]: "transactionid1"

response 参数： 
  [
    {
      "Key": "transactionid0",
      "Record": {
        "SID": "",
        "ReceiverSID": "",
        "OriginSID": "",
        "RequestSerial": "",
        "NextRequestSerial": "",
        "Proposaltime": 0,
        "transactionid": "transactionid0",
        "transactiondate": 1506005289,
        "parentorder": "0",
        "suborder": "0",
        "payid": "0",
        "transtype": "0",
        "fromtype": 1,
        "fromid": "userid0",
        "totype": 1,
        "toid": "JBYNCsmE",
        "productid": "productid0",
        "productinfo": "wegoodi%3",
        "organizationid": "pingan",
        "amount": 4,
        "price": 9
      }
      ...
  {
    "Key": "transactionid4",
     "Record": {
  ]
```
