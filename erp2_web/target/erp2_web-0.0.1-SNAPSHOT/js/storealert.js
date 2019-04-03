
$(function(){
	//初始化数据表格
	
	$("#grid").datagrid({
		url:'storedetail_storeAlertList',
		columns:[[
		          {field:'uuid',title:'商品编号',width:100},
		          {field:'name',title:'商品名称',width:100},
		          {field:'storenum',title:'库存数量',width:100},
		          {field:'outnum',title:'代发货数量',width:100}, 
		]],
		singleSelect:true,
		pagination:true,

		toolbar:[
		    {	
		    	text:'发送预警邮件',
		    	iconCls: 'icon-add',
		    	handler: function(){
		    		
		    		$.ajax({
		    			type:'post',
		    			url:'storedetail_sendStoreAlertMail',
		    			dataType:'json',
		    			success:function(rtn){
		    				
		    				$.messager.alert('提示',rtn.message,'info');
		    			}
		    		});
		    		
		    	}
		    }
		    ],
		
	});
})