function random(){
    var r = Math.round(Math.random() * 100);
    return (r * (r % 2 == 0 ? 1 : -1));
}

function randomDataArray() {
    var d = [];
    var len = 100;
    while (len--) {
        d.push([
            random(),
            random(),
            Math.abs(random()),
        ]);
    }
    return d;
}

option = {
    tooltip : {
        trigger: 'axis',
        showDelay : 0,
        axisPointer:{
            show: true,
            type : 'cross',
            lineStyle: {
                type : 'dashed',
                width : 1
            }
        }
    },
    legend: {
        data:['scatter1','scatter2']
    },
    toolbox: {
        show : true,
        feature : {
            mark : {show: true},
            dataZoom : {show: true},
            dataView : {show: true, readOnly: false},
            restore : {show: true},
            saveAsImage : {show: true}
        }
    },
    xAxis : [
        {
            type : 'value',
            splitNumber: 4,
            scale: true
        }
    ],
    yAxis : [
        {
            type : 'value',
            splitNumber: 4,
            scale: true
        }
    ],
    series : [
        {
            name:'scatter1',
            type:'scatter',
            symbolSize: function (value){
                return Math.round(value[2] / 5);
            },
            data: randomDataArray()
        },
        {
            name:'scatter2',
            type:'scatter',
            symbolSize: function (value){
                return Math.round(value[2] / 5);
            },
            data: randomDataArray()
        }
    ]
};
                    