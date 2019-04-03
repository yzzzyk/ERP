$(function() {

	$('#tree').tree({
		
	/*
	 * url : 'role_readRoleMenus?id=0',
	 */
		checkbox : true,
		animate : true

	});

	// 初始化数据表格
	$("#grid").datagrid({
		url : 'role_list',
		columns : [ [ 
		              {field:'uuid',title:'角色ID',width:150},
		              {field:'name',title:'角色名称',width:140},
		              ] ],
		singleSelect:true,
		onClickRow:function(index,row){
			
			
			$('#tree').tree({
				url : 'role_readRoleMenus?id='+row.uuid,
				checkbox : true,
				animate : true

			});
			$("#saveBtn").show();
		}
	});
	
	// 保存按钮绑定事件
	$("#saveBtn").bind('click',function(){
		var checkednodes=$("#tree").tree('getChecked');
		var checkedStr=new Array();
		
		$.each(checkednodes,function(i,node){
			checkedStr.push(node.id)
		})
		
		// 将数组转成以逗号分隔的字符串
		checkedStr=checkedStr.join(",");
		var formData={};
		formData.checkedStr=checkedStr;
		formData.id=$("#grid").datagrid('getSelected').uuid;
		
		
		$.ajax({
			url:'role_updateRoleMenus',
			type:'post',
			dataType:'json',
			data:formData,
			success:function(rtn){
				$.messager.alert('提示',rtn.message,'info');
			}		
		});
	})

})


