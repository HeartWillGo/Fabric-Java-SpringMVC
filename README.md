
# Fabric-Java-SpringMVC

Spring MVC和Fabric整合起来的一个区块链后台框架，通过这个后台访问区块链网络，需要自己先创建区块链网络，需要在config里面配置好你创建的区块链网络信息，在创建好区块链网络之后可以通过这个后台去部署智能合约，部署智能合约主要在MainContoller里面的install，智能合约部署之后，这个后台可以跟区块链网络进行交互。

##创建区块链网络##

https://github.com/HeartWillGo/Fabric-Java-SpringMVC/blob/master/springtest/docs/简单入门.md



##区块链后台： 

###结构 ####

1.控制器-controller 

1.MainController 定义了开启服务器打开服务的接口，查询和部署代码的接口。 主要是在已经创建了的区块链网络中部署智能合约代码。区块链网络各个节点的配置信息见myutil里的Config. 

2.OrgController 定义了跟机构相关的接口

3.ProductController 定义了和产品有关的接口

4.TransactionController 定义了和交易相关的接口

5.UserController 定义了和用户有关的接口 

####2.myutil

Config主要定义智能合约代码路径，区块链节点配置信息。

####3.respository

这个里面主要是封装了Invoke,ConstructChannel,DeployChaincode.

###相关解释###

https://github.com/HeartWillGo/Fabric-Java-SpringMVC/tree/master/springtest/docs

可以看看我写的相关文档，看了之后你应该会慢慢懂得。
