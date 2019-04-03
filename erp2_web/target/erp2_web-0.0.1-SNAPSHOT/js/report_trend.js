

$(function(){
	

	//表格数据初始化
	$('#grid').datagrid({
		url:"report_trendReport?year=" +$("#year").val(),
		columns:[[
		          {field:'month',title:'年份',width:100},
		          {field:'y',title:'销售额',width:100}
		          ]],
		          
		singleSelect:true,
		onLoadSuccess:function(data){
			//alert(JSON.stringify(data));
		
			showLineChart();
		}
		
	});
	
	//条件查询
	$('#btnSearch').bind('click',function(){
		var formdata= $('#searchForm').serializeJSON();
		
	
	//动态添加url
	$('#grid').datagrid({
			url:'report_trendReport'
		});
		
	$('#grid').datagrid('load',formdata);
	
	});
	
})

function showLineChart(){
	
	var monthData=new Array();
	for(var i=1;i<=12;i++){
		monthData[i-1]=i+'月';
	}

	Highcharts.chart('linechart', {

	    title: {
	        text: '销售趋势'
	    },

	    subtitle: {
	        text:$("#year").combobox('getValue')+'年销售趋势'
	    },

	    yAxis: {
	        title: {
	            text: '销售额（元）'
	        }
	    },
	    xAxis: {
	    	categories:monthData,
	    	title: {
	    		text: '月份'
	    	}
	    },
	    legend: {
	        layout: 'vertical',
	        align: 'right',
	        verticalAlign: 'middle'
	    },

	  

	    series: [{
	        name: '全部商品',
	        data:$("#grid").datagrid('getRows'),
	    }],

	    responsive: {
	        rules: [{
	            condition: {
	                maxWidth: 500
	            },
	            chartOptions: {
	                legend: {
	                    layout: 'horizontal',
	                    align: 'center',
	                    verticalAlign: 'bottom'
	                }
	            }
	        }]
	    }

	});
}