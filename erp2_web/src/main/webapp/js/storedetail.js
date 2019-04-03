

$(function(){
	$("#grid").datagrid({
		url:'storedetail_listByPage',
		columns:[[
		  		    {field:'uuid',title:'编号',width:100},
		  		    {field:'storeName',title:'仓库',width:100},
		  		    {field:'goodsName',title:'商品',width:100},
		  		    {field:'num',title:'剩余数量',width:100},
	    
				  ]],
		singleSelect:true,
		pagination:true,
		onDblClickRow:function(index, row){
			$("#storeOperDlg").dialog('open');
			$("#goodsname").html(row.goodsName);
			$("#storename").html(row.storeName);
			
			$("#storeOpergrid").datagrid({
				url:'storeoper_listByPage?t1.storeuuid='+row.storeuuid+'&t1.goodsuuid='+row.goodsuuid,
			});
		}
	});
	
	//条件查询
	$('#btnSearch').bind('click',function(){
		var formdata= $('#searchForm').serializeJSON();
		$('#grid').datagrid('load',formdata);		
	});
	
	//初始化弹窗
	$("#storeOperDlg").dialog({
		 title: '库存变动记录',    
		 width: 500,    
		 height: 400,    
		 closed: true,    
		 modal: true   

	});
	
	//
	$("#storeOpergrid").datagrid({
		  
	    columns:[[    
	        {field:'uuid',title:'编号',width:100},    
	        {field:'empName',title:'员工',width:100},    
	        {field:'opertime',title:'操作日期',width:130,align:'right',formatter:dateFormat},    
	        {field:'num',title:'数量',width:100},    
	        {field:'type',title:'类型',width:50,formatter:formatType}   
	    ]],
	    singleSelect:true,
	    pagination:true

	});
})
function dateFormat(date){
	
	return new Date(date).Format('yyyy-MM-dd hh:mm:ss');
}

function formatType(type){
	
	if(type*1==1){
		return '入库';
	}
	if(type*1==2){
		return '出库';
	}
	
}
