<%--
  Created by IntelliJ IDEA.
  User: 张鹏
  Date: 2017/9/23
  Time: 22:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>

    <title>ECharts</title>

    <!-- 引入 echarts.js -->
    <!-- 这里是加载刚下好的echarts.min.js，注意路径 -->






    <!--<script type="text/javascript">-->

    <!--var myShiftDateA=new Array(100);   //存放时间类型转换后的数据-->
    <!--var myShiftDateB=new Array(100);   //存放时间类型转换后的数据-->

    <!--//构造函数将时间类型转换成数据类型-->
    <!--var myDate=['2017/09/06 00:00:00','2017/09/09 02:00:00','2017/09/12 04:00:00','2017/09/15 06:00:00','2017/09/18 08:00:00'];  //测试A公司-->
    <!--var detailData=['交易信息为A','交易信息为B','交易信息为C','交易信息为D','交易信息为E'];    //A公司各个时间对应的信息-->

    <!--var myDateB=['2017/09/01 00:00:00','2017/09/09 02:00:00','2017/09/12 04:00:00','2017/09/15 06:00:00','2017/09/20 08:00:00'];  //测试A公司-->
    <!--var detailDataB=['交易信息为11','交易信息为22','交易信息为33','交易信息为44','交易信息为55'];    //A公司各个时间对应的信息-->


    <!--//将时间类型转换成数字形式-->
    <!--function shitDate(my){-->
    <!--//  var date1= '2017/09/01 00:00:00';  //开始时间-->
    <!--var date1= myDate[0];   //因为传输来的数据是最新的几条数据是按时间进行排列的-->
    <!--var date3 = new Date(my).getTime() - new Date(date1).getTime();   //时间差的毫秒数-->

    <!--//计算出相差天数-->
    <!--var days=Math.floor(date3/(24*3600*1000))-->

    <!--//计算出小时数-->

    <!--var leave1=date3%(24*3600*1000)    //计算天数后剩余的毫秒数-->
    <!--var hours=Math.floor(leave1/(3600*1000))-->
    <!--//计算相差分钟数-->
    <!--var leave2=leave1%(3600*1000)        //计算小时数后剩余的毫秒数-->
    <!--var minutes=Math.floor(leave2/(60*1000))-->
    <!--//计算相差秒数-->
    <!--var leave3=leave2%(60*1000)      //计算分钟数后剩余的毫秒数-->
    <!--var seconds=Math.round(leave3/1000)-->
    <!--var ggg=days+hours/24+minutes/60+seconds/3600-->
    <!--return  ggg;-->
    <!--}-->
    <!--</script>-->


    <script type="text/javascript" src="webjars/jquery/2.2.4/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/main.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/echarts.min.js"  charset="utf-8"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/jsfile.js"  charset="utf-8"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/TransactionMain.js"  charset="utf-8"></script>
</head>
<body>
<%--<h1>hello world色都是符合</h1>--%>
<div id="main" style="width: 80%;height:600px;padding-top:80px;padding-left:130px;padding-right:130px"></div>


<!--<script type="text/javascript">-->
<!--console.log('111111111111');-->
<!--// 基于准备好的dom，初始化echarts实例-->
<!--var myChart = echarts.init(document.getElementById('main'));-->


<!--var hours = ['', '公司A', '公司B','公司C','',];-->
<!--for(var i=0;i<myDate.length;i++)-->
<!--{-->
<!--var ddd=shitDate(myDate[i]);-->
<!--myShiftDateA[i]=ddd;-->
<!--&lt;!&ndash; alert(myShiftDateA[i]); &ndash;&gt;-->
<!--}-->

<!--for(var i=0;i<myDateB.length;i++)-->
<!--{-->
<!--var ddd=shitDate(myDateB[i]);-->
<!--myShiftDateB[i]=ddd;-->
<!--&lt;!&ndash; alert(myShiftDateA[i]); &ndash;&gt;-->
<!--}-->
<!--将转换后的时间数据等加载到该数组然后在界面上显示-->
<!--var data = [-->
<!--[1,0,0,0], [myShiftDateA[0],1,8,myDate[0],detailData[0]],             [myShiftDateB[0],2,6,myDate[0],detailDataB[0]],    [5,3,6,1] ,      [0,4,0,0]-->
<!--[1,0,0,0], [myShiftDateA[1],1,8,myDate[1],detailData[1]],             [myShiftDateB[1],2,6,myDate[1],detailDataB[1]],    [7,3,6,33] ,     [0,4,0,0]-->
<!--[2,0,0,0], [myShiftDateA[2],1,8,myDate[2],detailData[2]],             [myShiftDateB[2],2,6,myDate[2],detailDataB[2]],    [9,3,6,777],     [0,4,0,0]-->
<!--[3,0,0,0], [myShiftDateA[3],1,8,myDate[3],detailData[3]],             [myShiftDateB[3],2,6,myDate[3],detailDataB[3]],    [12,3,6,66666],  [0,4,0,0]-->
<!--[4,0,0,0], [myShiftDateA[4],1,8,myDate[4],detailData[4]],             [myShiftDateB[4],2,6,myDate[4],detailDataB[4]],   [9,3,6,8888]  ,   [0,4,0,0]               ];-->
<!--data = data.map(function (item) {-->
<!--return [item[1], item[0], item[2],item[3],item[4]];-->
<!--});-->

<!--option = {-->

<!--title: {-->
<!--text: '',-->
<!--link: 'https://github.com/pissang/echarts-next/graphs/punch-card'-->
<!--},-->
<!--legend: {-->
<!--x: 'center',-->
<!--data: ['交易情况'],-->
<!--left: 'center',-->
<!--textStyle:{ fontSize:25 // 让字体变大-->
<!--}-->
<!--},-->
<!--tooltip: {-->
<!--position: 'top',-->
<!--formatter: function (params) {-->
<!--return '<div style="border-bottom: 1px solid rgba(255,255,255,.3); font-size: 18px;padding-bottom: 7px;margin-bottom: 7px">'-->

<!--+ '</div>'-->
<!--+'交易时间为' + '：' + params.data[3]+'<br>'-->
<!--+'交易信息为'+ '：'+params.data[4]+'</br>'-->
<!--+'入链时间' + '：' +'</br>'-->
<!--+'交易状态' + '：' +'</br>'-->
<!--+'交易标识号' + '：'+'</br>'-->
<!--+'业务类型' + '：'  +'</br>'-->
<!--+'父订单号' + '：'  +'</br>';-->
<!--&lt;!&ndash;  return '在' + hours[params.value[0]] + '期间，' + days[params.value[1]] + '共发表了'+params.value[2] + '份论文'; &ndash;&gt;-->
<!--}-->
<!--},-->
<!--grid: {-->
<!--left: 2,-->
<!--bottom: 10,-->
<!--right: 10,-->
<!--containLabel: true-->
<!--},-->
<!--xAxis: {-->
<!--axisLabel:{-->
<!--margin:45,-->
<!--textStyle:{ fontSize:25 // 让字体变大-->
<!--}-->
<!--},-->
<!--type: 'category',-->
<!--data: hours,-->
<!--scale:false,-->
<!--boundaryGap: false,-->
<!--splitLine: {-->
<!--show: true,-->
<!--lineStyle: {-->
<!--color: '#48b',-->
<!--width:2,-->
<!--type: 'solid'-->


<!--}-->
<!--},-->
<!--axisLine: {-->
<!--show:true,-->
<!--onZero:false,-->
<!--lineStyle: {-->
<!--color: '#48b',-->
<!--width:2,-->
<!--type: 'solid'-->

<!--}-->
<!--}-->
<!--},-->
<!--yAxis: {-->
<!--type: 'value',-->
<!--scale:false,-->
<!--splitLine:{show:false},    &lt;!&ndash; 去掉网格线 &ndash;&gt;-->
<!--axisLine: {-->
<!--show: false,-->
<!--lineStyle: {-->
<!--color: '#999',-->
<!--type: 'dashed'-->
<!--}-->

<!--}-->

<!--},-->
<!--series: [{-->
<!--name: '交易情况',-->
<!--type: 'scatter',-->
<!--center:[50,10],-->
<!--symbolSize: function (val) {-->
<!--return val[2] * 4.2;-->
<!--},-->
<!--data: data,-->
<!--animationDelay: function (idx) {-->
<!--return idx * 5;-->
<!--}-->
<!--}]-->
<!--};-->


<!--// 使用刚指定的配置项和数据显示图表。-->
<!--myChart.setOption(option);-->
<!--</script>-->
</body>
</html>