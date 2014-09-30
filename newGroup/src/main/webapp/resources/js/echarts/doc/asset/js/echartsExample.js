var myChart;
var domMain = document.getElementById('main');
function refresh(opt){
    myChart.setOption(opt, true);
}

require.config({
    paths: {
        'js': '/resources/js/echarts/doc/asset/js/esl/js'
    },
    packages: [
        {
            name: 'echarts',
            location: '/resources/js/echarts/src',
            main: 'echarts'
        },
        {
            name: 'zrender',
            location: '/resources/zrender/src',
            main: 'zrender'
        }
    ]
});

var echarts;
require(
    ['echarts/echarts'],
    function(ec) {
        echarts = ec;
        if (myChart && myChart.dispose) {
            myChart.dispose();
        }
        myChart = echarts.init(domMain);
        myChart.showLoading({
        	text:"正在加载报表中，请稍候...",
        });
        refresh(option);
    }
)