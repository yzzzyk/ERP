$(function(){
	
	
	//表格数据初始化
	$('#grid').datagrid({
		url:'emp_listByPage',
		columns:[[
		  		    {field:'uuid',title:'编号',width:100},
		  		    {field:'username',title:'登陆名',width:100},
		  		    {field:'name',title:'真实姓名',width:100},
		  		    {field:'gender',title:'性别',width:100,formatter:function(value,row,index){
		  		    	if(value*1 ==1){/*value 字段值为字符串     */
		  		    		return '男';
		  		    	}
		  		    	if(value*1 ==0){
		  		    		return '女';
		  		    	}
		  		    }},
		  		    {field:'email',title:'邮件地址',width:100},
		  		    {field:'tele',title:'联系电话',width:100},
		  		    {field:'address',title:'联系地址',width:100},
		  		    {field:'birthday',title:'出生年月日',width:100,formatter:function(value){
		  		    	return new Date(value).Format("yyyy-MM-dd");
		  		    }},
		  		    
		  		    {field:'dep',title:'部门编号',width:100,formatter:function(value,row,index){
		  		    	return value.name;
		  		    }},

				    {field:'-',title:'操作',width:200,formatter:function(value,row,index)
				    	{
				    		return "<a href='#' onclick='updatePwd_reset("+row.uuid+")'>重置密码</a>";
				    	}}		    
				          ]],
		singleSelect:true,
		pagination:true,//显示分页工具栏
	});
	
	//初始化编辑窗口
	$("#editWindow").dialog({
		
		title:'重置密码',
		width:200,
		height:150,
		closed:true,
		modal:true,//窗口模式
		iconCls:'icon-save',
		buttons:[{
			text:'保存',
			iconCls:'icon-save',
			handler:function(){
				
				var dataForm=$("#editForm").serializeJSON();
				$.ajax({
					type:'post',
					url:'emp_updatePwd_Reset',
					dataType:'json',
					data:dataForm,
					success:function(btn){
						if(btn.success){
							
							$.messager.alert('提示','用户'+dataForm["t.name"]+btn.message,'info');
							$("#editForm").form('clear');
							$("#editWindow").dialog('close');
						}else{
							
							$.messager.alert('提示',btn.message,'info');
						}
					}
				});
			}
			
		}
		]
		
	});
	
	//条件查询
	$('#btnSearch').bind('click',function(){
		var formdata= $('#searchForm').serializeJSON();
		$('#grid').datagrid('load',formdata);		
	});
	
});
//重置密码
	function updatePwd_reset(uuid){
		
		$("#editForm").form('clear');
		$("#editWindow").dialog('open');
		$('#editForm').form('load','emp_get?id='+uuid);
		$('#uuid').val(uuid);
		
	}

