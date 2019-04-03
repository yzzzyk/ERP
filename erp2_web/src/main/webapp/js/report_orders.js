

$(function(){
	//初始化数据表格
	$("#grid").datagrid({
		url:'report_orderReport',
		columns:[[
		        {field:'name',title:'商品类型',width:100},
		        {field:'y',title:'销售额',width:100}		          
		        ]],
		singleSelect:true,
		onLoadSuccess:function(data){
			//alert(JSON.stringify(data));//{"total":3,"rows":[{"name":"儿童食品","y":6786.51},{"name":"调味品","y":63.3},{"name":"水果","y":649.8}]}
			showChart(data.rows);
		}
		
		
	});
	
	//条件查询
	$('#btnSearch').bind('click',function(){
		var formdata= $('#searchForm').serializeJSON();
		if(formdata.endDate!=null){
			formdata.endDate=formdata.endDate+" 23:59:59";
		}
		$('#grid').datagrid('load',formdata);		
	});
	
	
	
	
});

//显示饼图
function showChart(_data){

	var formdata= $('#searchForm').serializeJSON();
	var hehe="全部";
	if(formdata.startDate!=""&&formdata.endDate!=""){
		hehe=formdata.startDate+"——"+formdata.endDate;
	}
	Highcharts.chart('piechart', {
	    chart: {
	        plotBackgroundColor: null,
	        plotBorderWidth: null,
	        plotShadow: false,
	        type: 'pie'
	    },
	    title: {
	        text: hehe+'<br>销售统计'
	    },
	    tooltip: {
	        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
	    },
	    //信用
	    credits:{enabled:false},
	    //可导出 
	    exporting:{enabled:true},
	    plotOptions: {
	        pie: {
	            allowPointSelect: true,
	            cursor: 'pointer',
	            dataLabels: {
	                enabled: false
	            },
	            showInLegend: true
	        }
	    },
	    series: [{
	        name: '比例',
	        colorByPoint: true,
	        
	        data: _data
	    }]
	});
}