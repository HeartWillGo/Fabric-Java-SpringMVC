charset = "utf-8";
window.onload = function () {

    initChart();
    getUserAsset();
    getData();
    showData();
};
var myChart;
var orgList = [""];
var trans = new Array();
var dataGet = [];
var dateList = [];
var LastDate = [];
var y_axis = [0,0];
var userAsset;


function initChart() { //初始化图表

    myChart = echarts.init(document.getElementById('main'));
    // console.log("orgList", orgList);
    options = {
        title: {
            text: '',
            link: 'https://github.com/pissang/echarts-next/graphs/punch-card'
        },
        legend: {
            x: 'center',
            data: ['资产视图'],
            left: 'center',
            textStyle: {
                fontSize: 25 // 让字体变大
            }
        },
        tooltip: {},
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
            data: orgList,
            scale: false,
            boundaryGap: false,
            splitLine: {
                show: true,
                lineStyle: {
                    color: '#e5e5e5',
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
            type: 'time',
            scale: true,
            splitLine: {
                <!-- 去掉网格线 -->
                show: false,
                lineStyle: {
                    color: '#e5e5e5',
                    width: 2,
                    type: 'solid'


                }
            },
            axisLine: {
                show: false,
                lineStyle: {
                    color: '#48b',
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
    myChart.setOption(options);
}

function getData() {  //获取数据
    search = {};
    search["userid"] = "yaozhen";
    $.ajax({
        type: "POST",
        contentType: "application/json",
        async: false, //异步请求（同步请求将会锁住浏览器，用户其他操作必须等待请求完成才可以执行） true：异步，false：同步。
        url: "/blockchain/transaction/getTransactionByUserID",
        data: JSON.stringify(search),//自定义查询字段
        dataType: 'json',
        cache: false,
        timeout: 60000000,
        success: function (data) {
            for (var i = 0; i < data.length; i++) {
                trans[i] = data[i].Record;
                console.log("trans",trans[i]);
            }
        },
        error: function (e) {

            console.log("ERROR : ", e);

        }
    });

}
function getUserAsset() {
    search = {};
    search["id"] = "yaozhen";
    $.ajax({
        type: "POST",
        contentType: "application/json",
        async: false, //异步请求（同步请求将会锁住浏览器，用户其他操作必须等待请求完成才可以执行） true：异步，false：同步。
        url: "/blockchain/user/getUserAsset",
        data: JSON.stringify(search),//自定义查询字段
        dataType: 'json',
        cache: false,
        timeout: 60000000,
        success: function (data) {
            userAsset = data;
            console.log("userAsset",userAsset);

        },
        error: function (e) {

            console.log("ERROR : ", e);

        }
    });

}
function showData() {
    // var dataGet = [];

    for (var i = 0; i < trans.length; i++) {
        if (orgList.indexOf(trans[i].organizationid) == -1) {
            orgList.push(trans[i].organizationid);
        }
    }
    orgList.push("");

    for (var i = 0; i < trans.length; i++) {

        var date = trans[i].transactiondate;
        console.log("date",date);
        var dateLast =timeConverter(date);
        var column = orgList.indexOf(trans[i].organizationid);
        var size = trans[i].amount * trans[i].price;
        if (size > 10) {
            size = 10;
        }
        var index = i;
        var tran = [date*1000, column, 5, index];
        dataGet.push(tran);
    }
    console.log("sfd",dataGet);

    dataGet = dataGet.map(function (item) {
        return [item[1], item[0], item[2], item[3]];
    });

    console.log("baba is son's father", dataGet);

    myChart = echarts.init(document.getElementById('main'));
    options = {
        tooltip: {
            position: 'top',
            formatter: function (params) {
                var index = params.data[3];
                // var formattime = timeConverter(trans[index].transactiondate);
                //  console.log("formattime", formattime);
                return '<div style="border-bottom: 1px solid rgba(255,255,255,.3); font-size: 18px;padding-bottom: 7px;margin-bottom: 7px">'

                    + '</div>'
                    + '交易ID' + ':' + trans[index].transactionid + '<br>'
                    + '交易时间' + '：' + timeConverter(trans[index].transactiondate) + '<br>'
                    + '数量' + ':' + trans[index].amount + '<br>'
                    + '价格' + ':' + trans[index].price + '<br>';

                // return res;
            }
        },
        xAxis: {
            axisLabel: {
                margin: 45,
                textStyle: {
                    fontSize: 25 // 让字体变大
                }
            },
            type: 'category',
            data: orgList,
            scale: false,
            boundaryGap: false,
            splitLine: {
                show: true,
                lineStyle: {
                    color: '#e5e5e5',
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
            type:'time' ,
            scale:true,
            axisLine: {
                show: false
            },
        },
        series: [{
            name: '资产视图',
            type: 'scatter',

            center: [50, 10],
            symbolSize: function (val) {
                return val[2] * 4.2;
            },
            //  data: [],
            data: dataGet,
            animationDelay: function (idx) {
                return idx * 5;
            }
        }]
    };
    myChart.setOption(options);
}


function timeConverter(UNIX_timestamp){
    var a = new Date(UNIX_timestamp * 1000);
    var months = ['01','02','03','04','05','06','07','08','09','10','11','12'];
    var year = a.getFullYear();
    var month = months[a.getMonth()];

    var date = a.getDate().toString();
    if (date.length == 1) {
        console.log(date);
        date = "0" + date;
    }

    var hour = a.getHours().toString();
    if (hour.length == 1) {
        hour = "0" +hour;
    }
    var min = a.getMinutes().toString();
    if (min.length == 1) {
        min = "0" +min;
    }
    var sec = a.getSeconds().toString();
    if (sec.length == 1) {
        sec = "0" +sec;
    }
    var time = year + month + date + hour  + min  + sec ;
    console.log(time);
    return time;
}

