

$(function(){
	
	//表格数据初始化
	$('#grid').datagrid({
		url:'storeoper_listByPage.action',
		columns:[[
		  		    {field:'uuid',title:'编号',width:100},
		  		    {field:'empName',title:'操作员工',width:100},
		  		    {field:'opertime',title:'操作日期',width:100,formatter:formatDate},
		  		    {field:'storeName',title:'仓库',width:100},
		  		    {field:'goodsName',title:'商品',width:100},
		  		    {field:'num',title:'数量',width:100},
		  		    {field:'type',title:'类型',width:100,formatter:function(value){
		  		    	switch(value*1){
		  		    	case 1:return '入库';
		  		    	case 2:return '出库';
		  		    	default :return '';
		  		    	}
		  		    }},//1：入库 2：出库	    
		  		    ]],
		singleSelect:true,
		pagination:true,
		fitColumns:true,
		toolbar: [{
			iconCls: 'icon-add',
			text:'增加',
			handler: function(){				
				$('#editWindow').window('open');
				$('#editForm').form('clear');
				method="add";
			}
		}]

	});
	
	//条件查询
	$('#btnSearch').bind('click',function(){
		var formdata= $('#searchForm').serializeJSON();
		$('#grid').datagrid('load',formdata);		
	});
	
})

function formatDate(value){
	return (new Date(value)).Format("yyyy-MM-dd hh:mm:ss.S");
}