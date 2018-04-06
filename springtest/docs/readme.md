##区块链后台：
###结构
####1.控制器-controller
1.MainController 定义了开启服务器打开服务的接口，查询和部署代码的接口。
主要是在已经创建了的区块链网络中部署智能合约代码。区块链网络各个节点的配置信息见myutil里的Config.
2.OrgController 定义了跟机构相关的接口
3.ProductController 定义了和产品有关的接口
4.TransactionController 定义了和交易相关的接口
5.UserController 定义了和用户有关的接口
####2.myutil

