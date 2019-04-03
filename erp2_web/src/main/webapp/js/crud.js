
	var width;
	var height;
	var method="";//保存提交的方法名称 
	var listParam="";
	var saveParam='';
$(function(){
	
	var w=300;
	var h=150;
	
	
	if(width!=null){
		w=width;
	}
	//或者使用if(typeof(width)=="undefine") 判断width是否未定义
	
	if(height!=null){
		h=height;
	}
	
	//初始化编辑窗口
	$("#editWindow").window({
		width:w,
		height:h,
		closed:true
	});
	
	
	//表格数据初始化
	$('#grid').datagrid({
		url:name+'_listByPage.action'+listParam,
		columns:columns,
		singleSelect:true,
		pagination:true,
		toolbar: [
		   {
			iconCls: 'icon-add',
			text:'增加',
			handler: function(){				
				$('#editWindow').window('open');
				$('#editForm').form('clear');
				method="add";
		   }},'-',
		   {
				iconCls: 'icon-excel',
				text:'导出',
				handler: function(){				
				var formdata=$('#searchForm').serializeJSON();
				$.download(name+'_export'+listParam,formdata);}
		   },'-',
		   {
				iconCls: 'icon-save',
				text:'导入',
				handler: function(){				
					$("#inportDlg").dialog('open');
				}
		   }
		]

	});
	//判断是否有上传功能
	var inportForm=document.getElementById("inportForm");
	if(inportForm){
		//文件上传框初始化
		$("#inportDlg").dialog({
			title:'导入数据',
			width:330,
			height:106,
			closed:true,
			model:true,
			buttons:[{
				text:'导入',
				iconCls:'icon-save',
				handler:function(){
					$.ajax({
						url:name+'_inport',
						type:"post",
						data:new FormData($('#inportForm')[0]),
						dataType:'json',
						processData:false,
						contentType:false,
						success:function(rtn){
							$.messager.alert('提示',rtn.message,'info',function(){
								if(rtn.success){
									$("#inportDlg").dialog('close');
									$("#grid").datagrid('reload');
								}
							})
						}
					})
				}
			
			}]
		});
	}
	
	
	
	//条件查询
	$('#btnSearch').bind('click',function(){
		var formdata= $('#searchForm').serializeJSON();
		$('#grid').datagrid('load',formdata);		
	});
	
	
	
	//保存编辑（修改、添加）
	$('#btnSave').bind('click',function(){
		
		var isValid=$("#editForm").form('validate');
		
		if(!isValid){
			rerturn;
		}
		
		var formdata= $('#editForm').serializeJSON();	
		
		$.ajax({
			url:name+'_'+method+'.action'+saveParam,
			data:formdata,
			dataType:'json',
			type:'post',
			success:function(value){
				
				if(value.success){
					$('#editWindow').window('close');
					$('#grid').datagrid('reload');
				}
				$.messager.alert('提示',value.message);				
			}
			
		});
		
		
	});
	
	
});

/**
 * 删除 
 */
function dele(id){
	
	$.messager.confirm('提示','确定要删除此记录吗？',function(r){
		if(r)
		{
			$.ajax({
				url:name+'_delete.action?id='+id,
				dataType:'json',
				success:function(value){
					if(value.success){
						$('#grid').datagrid('reload');
					}
					$.messager.alert('提示',value.message);
				}
			});		
		}	
	});	
}

/**
 * 编辑
 */
function edit(id){
	
	$('#editWindow').window('open');
	$('#editForm').form('clear');
	$('#editForm').form('load',name+'_get.action?id='+id);	
	method="update";
}