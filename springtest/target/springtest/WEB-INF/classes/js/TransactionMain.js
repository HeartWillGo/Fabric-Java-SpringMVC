window.onload = function () {

    //fire_ajax_submit();
    initChart();
   setInterval("getData()",1000);


  //  getData(); //动态获取数据

};

var myChart;
//var MydataGet;
var hours;

var transactionid=[];//定义数组
var myShiftDateA=[];
var parentorder=[];
var suborder=[];
var payid=[];
var transtype=[];
var fromtype=[];
var fromid=[];
var totype=[];
var toid=[];
var productid=[];
var productinfo=[];
var organizationid=[];
var amount=[];
var price=[];
var myDate=[];
var search=[];
function initChart() { //初始化图表
    var dataGet;
    // 基于准备好的dom，初始化echarts实例
  //  getData();
    myChart = echarts.init(document.getElementById('main'));
    hours = ['', '公司A', '公司B', '公司C', ''];
    options = {

        title: {
            text: '',
            link: 'https://github.com/pissang/echarts-next/graphs/punch-card'
        },
        legend: {
            x: 'center',
            data: ['交易情况'],
            left: 'center',
            textStyle: {
                fontSize: 25 // 让字体变大
            }
        },
        tooltip: {
        },
        grid: {
            left: 2,
            bottom: 10,
            right: 10,
            containLabel: true
        },
        xAxis: {
            axisLabel: {
                margin: 45,
                textStyle: {
                    fontSize: 25 // 让字体变大
                }
            },
            type: 'category',
            data: hours,
            scale: false,
            boundaryGap: false,
            splitLine: {
                show: true,
                lineStyle: {
                    color: '#48b',
                    width: 2,
                    type: 'solid'


                }
            },
            axisLine: {
                show: true,
                onZero: false,
                lineStyle: {
                    color: '#48b',
                    width: 2,
                    type: 'solid'

                }
            }
        },
        yAxis: {
            type: 'value',
            scale: false,
            splitLine: {show: false}, <!-- 去掉网格线 -->
            axisLine: {
                show: false,
                lineStyle: {
                    color: '#999',
                    type: 'dashed'
                }

            }

        },
        series: [
            {
            name: '交易情况',
            type: 'scatter',
            center: [50, 10],
            symbolSize: function (val) {
                return val[2] * 4.2;
            },
           data: [],
            animationDelay: function (idx) {
                return idx * 5;
            }
        }
        ]
    };
    console.log("hhhhuu : ", dataGet);
    myChart.setOption(options);
}

function getData() {  //获取数据
    console.log("eeee : ", "加一");
    search = {};
    search["organizationid"] = "pingan";

    $.ajax({
        type: "POST",
        contentType: "application/json",
        async: false,            //异步请求（同步请求将会锁住浏览器，用户其他操作必须等待请求完成才可以执行） true：异步，false：同步。
        url: "/admin/yz",
        data: JSON.stringify(search),//自定义查询字段
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            console.log("logggg",data);
            for (var i = 0; i < data.transactionsResult.length; i++) {　//数组的遍历
                transactionid[i] = data.transactionsResult[i].transactionid;
                myDate[i] = data.transactionsResult[i].transactiondate;
                parentorder[i] = data.transactionsResult[i].parentorder;
                suborder[i] = data.transactionsResult[i].suborder;
                payid[i] = data.transactionsResult[i].payid;
                transtype[i] = data.transactionsResult[i].transtype;
                fromtype[i] = data.transactionsResult[i].fromtype;
                fromid[i] = data.transactionsResult[i].fromid;
                totype[i] = data.transactionsResult[i].totype;
                toid[i] = data.transactionsResult[i].toid;
                productid[i] = data.transactionsResult[i].productid;
                productinfo[i] = data.transactionsResult[i].productinfo;
                organizationid[i] = data.transactionsResult[i].organizationid;
                amount[i] = data.transactionsResult[i].amount;
                price[i] = data.transactionsResult[i].price;


                console.log("GETTT : ", myDate[0] + productinfo[0]);
                //   alert(data.transactionsResult[i].amount);
            }
            for (var i = 0; i < data.transactionsResult.length; i++) {
                myShiftDateA[i] = shitDate(myDate[i]);
                // myShiftDateA[i] = ddd;
                console.log("gg : ", myShiftDateA[i]);
                console.log("hh : ", data.transactionsResult.length);
                <!-- alert(myShiftDateA[i]); -->
            }

            console.log("OO : ", myShiftDateA[1]);
            //myChart.setOption(options);
            // console.log("ff : ", options);
            console.log("SUCCESS : ", data);
            // console.log("SUCCESS : ", data.transactionsResult[0].amount);
            //  showEchaerts();

        },
        error: function (e) {

            console.log("ERROR : ", e);

        }
    });

    console.log("RR : ", myShiftDateA[1]);
    //将转换后的时间数据等加载到该数组然后在界面上显示
    dataGet = [
        [1, 0, 0, 0], [myShiftDateA[0], 1, 8, transactionid[0], parentorder[0], suborder[0], payid[0], transtype[0], fromtype[0], fromid[0], totype[0], toid[0], productid[0], productinfo[0], organizationid[0], amount[0], price[0]], [3, 2, 6, 9], [5, 3, 6, 1], [0, 4, 0, 0]

            [1, 0, 0, 0], [myShiftDateA[1], 1, 8, transactionid[1], parentorder[1], suborder[1], payid[1], transtype[1], fromtype[1], fromid[1], totype[1], toid[1], productid[1], productinfo[1], organizationid[1], amount[1], price[1]], [4, 2, 6, 8], [7, 3, 6, 33], [0, 4, 0, 0]

            [2, 0, 0, 0], [myShiftDateA[2], 1, 8, transactionid[2], parentorder[2], suborder[2], payid[2], transtype[2], fromtype[2], fromid[2], totype[2], toid[2], productid[2], productinfo[2], organizationid[2], amount[2], price[2]], [5, 2, 6, 7], [9, 3, 6, 777], [0, 4, 0, 0]

            [3, 0, 0, 0], [myShiftDateA[3], 1, 8, transactionid[3], parentorder[3], suborder[3], payid[3], transtype[3], fromtype[3], fromid[3], totype[3], toid[3], productid[3], productinfo[3], organizationid[3], amount[3], price[3]], [6, 2, 6, 6], [12, 3, 6, 66666], [0, 4, 0, 0]

            [4, 0, 0, 0], [myShiftDateA[4], 1, 8, transactionid[4], parentorder[4], suborder[4], payid[4], transtype[4], fromtype[4], fromid[4], totype[4], toid[4], productid[4], productinfo[4], organizationid[4], amount[4], price[4]], [7, 2, 6, 6], [9, 3, 6, 8888], [0, 4, 0, 0]
    ];
    // X：时间；Y：公司；Z：大小  H :myDate；transactionid；parentorder；suborder；payid；transtype；fromtype；fromid；totype；toid；productid；productinfo；organizationid；amount；price

    MydataGet = dataGet.map(function (item) {
        return [item[1], item[0], item[2], item[3], item[4], item[5], item[6], item[7], item[8], item[9], item[10], item[11], item[12], item[13], item[14], item[15], item[16], item[17]];
    });
    myChart = echarts.init(document.getElementById('main'));
    options = {
        tooltip: {
            position: 'top',
            formatter: function (params) {
                res = '<div style="border-bottom: 1px solid rgba(255,255,255,.3); font-size: 18px;padding-bottom: 7px;margin-bottom: 7px">'

                    + '</div>'
                    + 'transactionid' + ':' + params.data[4] + '<br>'
                    + 'transactiondate' + '：' + params.data[3] + '<br>'
                    + 'parentorder' + ':' + params.data[5] + '<br>'
                    + 'suborder' + ':' + params.data[6] + '<br>'
                    + 'payid' + ':' + params.data[7] + '<br>'
                    + 'transtype' + ':' + params.data[8] + '<br>'
                    + 'fromtype' + ':' + params.data[9] + '<br>'
                    + 'fromid' + ':' + params.data[10] + '<br>'
                    + 'toid' + ':' + params.data[11] + '<br>'
                    + 'productid' + ':' + params.data[12] + '<br>'
                    + 'productinfo' + '：' + params.data[13] + '</br>'
                    + 'organizationid' + '：' + params.data[14] + '</br>'
                    + 'amount' + '：' + params.data[15] + '</br>'
                    + 'price' + '：' + params.data[16] + '</br>';

                return res;
                <!--  return '在' + hours[params.value[0]] + '期间，' + days[params.value[1]] + '共发表了'+params.value[2] + '份论文'; -->
            }
        },
        series: [{
            name: '交易情况',
            type: 'scatter',
            center: [50, 10],
            symbolSize: function (val) {
                return val[2] * 4.2;
            },
            //  data: [],
            data: MydataGet,
            animationDelay: function (idx) {
                return idx * 5;
            }
        }]
    };
    console.log("hhhhuu : ", dataGet);
    myChart.setOption(options);
}

//将时间类型转换成数字形式
function shitDate(my) {
    //  var date1= '2017/09/01 00:00:00';  //开始时间
    var date1 = myDate[0];   //因为传输来的数据是最新的几条数据是按时间进行排列的
    var date3 = new Date(my).getTime() - new Date(date1).getTime();   //时间差的毫秒数
    //计算出相差天数
    var days = Math.floor(date3 / (24 * 3600 * 1000));
    //计算出小时数

    var leave1 = date3 % (24 * 3600 * 1000); //计算天数后剩余的毫秒数
    var hours = Math.floor(leave1 / (3600 * 1000));
    //计算相差分钟数
    var leave2 = leave1 % (3600 * 1000);       //计算小时数后剩余的毫秒数
    var minutes = Math.floor(leave2 / (60 * 1000));
    //计算相差秒数
    var leave3 = leave2 % (60 * 1000);     //计算分钟数后剩余的毫秒数
    var seconds = Math.round(leave3 / 1000);
    var ggg = days + hours / 24 + minutes / 60 + seconds / 3600;
    return ggg;
}