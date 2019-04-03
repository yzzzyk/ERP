$(function(){
	//初始化树
	$('#tree').tree({     
	    checkbox:true,
	    animate:true
	});
	
	
	$("#grid").datagrid({
		
		url:'emp_list',
		columns:[[
		          {field:'uuid',title:'用户id',width:100},
		          {field:'name',title:'用户名',width:100},
		          ]],
		singleSelect:true,
		 onDblClickRow:function(index,row){
			 
			//初始化树
				$('#tree').tree({    
				    url:'emp_readEmpRoles?id=' +row.uuid,				   
				});
			//显示保存按钮
				$("#saveBtn").show();
			}
	
	});
	
	//按钮添加事件
	$("#saveBtn").bind('click',function(){
		
		//获取被选中的节点
		var nodes = $('#tree').tree('getChecked');
		//alert(JSON.stringify(nodes));
		var chechedRole=new Array();
		$.each(nodes,function(i,node){
			chechedRole.push(node.id);
		});
		
		//将数组转换为逗号分隔的字符串
		chechedRole=chechedRole.join(",");
		
		var formData={};
		formData.id=$("#grid").datagrid("getSelected").uuid;
		formData.chechedRole=chechedRole;
		
		$.ajax({
			url:'emp_updateEmpRoles',
			type:'post',
			dataType:'json',
			data:formData,
			success:function(rtn){
				$.messager.alert('提示',rtn.message,'info')
			},
		})
	});

	
	
	
})