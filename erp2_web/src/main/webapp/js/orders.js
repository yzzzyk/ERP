$(function(){
	
	if(Request['type']==2){
		$("#outindate").html("出库日期");
	}

	var _url='orders_listByPage'   
	var inOutTitle='';
	var inOutBtn='';	
	//初始化 数据表格
		$("#grid").datagrid({
			columns:getColunms(),
			
	});
	
	//订单明细表格初始化
	$("#itemgrid").datagrid({
		singleSelect:true,//唯一选择
		pagination:true,//底部分页栏
		fitColumns:true,//真正的自动展开/收缩列的大小，以适应网格的宽度，防止水平滚动。
		columns:[[
		    {field:'uuid',title:'编号',width:100},
		    {field:'goodsuuid',title:'商品编号',width:100},
		    {field:'goodsname',title:'商品名称',width:100},
		    {field:'price',title:'价格',width:100},
		    {field:'num',title:'数量',width:100},
		    {field:'money',title:'金额',width:100},
		    {field:'endtime',title:'结束日期',width:100,formatter:FormatDate},
		    {field:'state',title:'状态',width:100,formatter:getOrderDetailState},    
		    ]]
		
	});
	
	
	
	//添加进货订单初始化，
	$("#addOrdersDlg").dialog({
		 title: '增加订单',    
		 width: 700,    
		 height: 400,    
		 closed: true,    
		 modal: true
	});
	
	//创建数组存放弹窗的工具栏
	var toolBar=new Array();
	
	
	//?t1.type=1  采购订单
	//所有采购订单查询
	if(Request['oper']=='orders'){
		
		if(Request['type']==1){
			_url+="?t1.type=1";
			document.title="采购订单";
		}
		
		else if(Request['type']==2){
			_url+="?t1.type=2";
			document.title="銷售订单";
			
		}else
			document.title="所有订单";
	}	
	
	//如果是创建新退货订单  
	if(Request['oper']=="returnadd"){
		
		//如果原订单是采购订单
		if(Request['type']==1){//采购订单
			document.title="采购订单退货";
			_url+='?t1.state=3&t1.type=1'				
		}
			
		//如果原订单是销售订单
		if(Request['type']==2){//销售订单
			document.title="销售订单退货";
		}
		
	}
	//当前登录用户的，所有订货订单
	if(Request['oper']=='myorders'){
		
		$("#grid").datagrid({
			toolbar:[{
				text:btnText,
				iconCls:'icon-add',
				handler:function(){
					$("#addOrdersDlg").dialog('open');
				}
			}]
		})
		var btnText='';
		_url="orders_myListByPage?t1.type="+Request['type'];//动态获取 订单类型
		
		
		if(Request['type']==1){//采购订单
			btnText='新增进货订单';
			$("#ordersupplier").html('供应商：');
			document.title="我的进货订单";
			$("#grid").datagrid({
				 title:'采购订单列表',
			});
		}
		
		if(Request['type']==2){//销售订单
			btnText='新增销售订单';
			$("#ordersupplier").html('客户：');
			document.title="我的销售订单";
			$("#grid").datagrid({
				 title:'销售订单列表',
			});
		}
		
	
		
		
	}
	
	
	//如果是审核方法，添加审核按钮,只显示未审核的订单
	if(Request['oper']=='doCheck'){
		document.title="采购订单审核";
		_url+="?t1.type=1&t1.state=0";
		toolBar.push({
			text:'审核',
			iconCls:'icon-search',
			handler:doCheck
		});
	}
	
	
	//如果是     确认  方法，添加确认按钮,只显示已审核，未确认的订单
	if(Request['oper']=='doStart'){
		
		document.title="采购订单确认";
		_url+="?t1.type=1&t1.state=1";
		toolBar.push({
			text:'确认订单',
			iconCls:'icon-search',
			handler:doStart
		});	
	}
	
	toolBar.push(
			{
		text:'导出',
		iconCls:'icon-excel',
		handler:exportById
	});
	
	//加工具栏
	$("#ordersDlg").dialog({
		toolbar:toolBar
	});
	
	

	//如果是     入库  方法，添加双击显示事件,只显示已确认，未入库的订单
	if(Request['oper']=='doInStore'){
		document.title="采购订单入库";
		inOutTitle='入库';
		inOutBtn='入库';
		_url+="?t1.type=1&t1.state=2";
			
			$("#itemgrid").datagrid({
				onDblClickRow:function(index, row){
					
					$("#itemuuid").val(row.uuid);
					
					$("#goodsuuid").html(row.goodsuuid);
					$("#goodsname").html(row.goodsname);
					$("#num").html(row.num);
					$("#itemDlg").dialog('open');
					
				}
			});
	}
	
	//如果是     出库方法，添加双击显示事件,且只显已确认，未出库的订单
	if(Request['oper']=='doOutStore'){
		document.title="销售订单出库";
		_url+="?t1.type=2&t1.state=0";
		inOutTitle='出库';
		inOutBtn='出库';
		$("#itemgrid").datagrid({
			onDblClickRow:function(index, row){
				
				if(row.state*1 ==1){
					return;
				}
				$("#itemuuid").val(row.uuid);
				
				$("#goodsuuid").html(row.goodsuuid);
				$("#goodsname").html(row.goodsname);
				$("#num").html(row.num);
				$("#itemDlg").dialog('open');
				
			}
		});
	}
	//订单明细,入库确认信息显示表格，初始化
	$("#itemDlg").dialog({
		 title: inOutTitle,    
		 width: 300,    
		 height: 165,    
		 closed: true,    
		 modal: true,
		 buttons:[{
			text:inOutBtn,
			iconCls:'icon-save',
			handler:doInOutStore

		 }]

	});
	
	//加载订单列表 数据表格
	$("#grid").datagrid({
		  
		   url:_url,    
		   columns:getColunms(),
			singleSelect:true,//唯一选择
			pagination:true,//底部分页栏
			fitColumns:true,//真正的自动展开/收缩列的大小，以适应网格的宽度，防止水平滚动。
			onDblClickRow:function(index, row){
				//index：点击的行的索引值，该索引值从0开始。 row：对应于点击行的记录
				//alert(JSON.stringify(row));
				//加载表头
				$("#uuid").html(row.uuid);
				$("#supplieruuidName").html(row.supplieruuidName);
				$("#state").html(getOrderState(row.state));
				$("#createrName").html(row.createrName);
				$("#checkerName").html(row.checkerName);
				$("#starterName").html(row.starterName);
				$("#enderName").html(row.enderName);
				$("#createtime").html(FormatDate(row.createtime));
				$("#checktime").html(FormatDate(row.checktime));
				$("#starttime").html(FormatDate(row.starttime));
				$("#endtime").html(FormatDate(row.endtime));
				//运单号
				$("#waybillsn").html(row.waybillsn);
				var tool=new Array();
				for(var i=0;i<toolBar.length;i++){
					tool.push(toolBar[i]);
				}
				if(row.state*1==1 && row.type*1==2){//销售订单且已出库
					
					//添加运单详情按钮
					var options=$("#ordersDlg").dialog('options');
					//alert(JSON.stringify(options));

					tool.push({
						text:'物流信息',
						iconCls:'icon-search',
						handler:function (){
							//打开弹窗
							$("#waybillDlg").dialog('open');
							
							//加载 运单详细信息的 数据表eg 
							$("#waybillGrid").datagrid({
								url:'orders_waybillDetailList?waybillsn='+$("#waybillsn").html(),
								columns:[[
								          {field:'exedate',title:'执行日期',width:100},
								          {field:'exetime',title:'执行时间',width:100},
								          {field:'info',title:'执行信息',width:100}
								    ]],   
								rownumbers:true
							});
						}
					
					});
				}
		
				$("#ordersDlg").dialog({
					toolbar:tool
				});
				
				$("#ordersDlg").dialog('open');
				$("#itemgrid").datagrid('loadData',row.orderdetail);			
			}	
	});	
	
	
	//如果是创建新退货订单 ,数据加载完成后 显示退货按钮
	if(Request['oper']=="returnadd"){
		
		//显示隐藏的操作栏
		$("#grid").datagrid('showColumn','heh');
		$("#grid").datagrid('reload');
	}
	
	
	
	
	 
	
	
	
});

/**
 * 格式化日期
 * @param date
 * @returns
 */
function FormatDate(value){
	
	return new Date(value).Format('yyyy-MM-dd ');
}

/**
 * 
 * 采购: 0:未审核 1:已审核, 2:已确认, 3:已入库；销售：0:未出库 1:已出库
 * @returns
 */
function getOrderState(value){
	if(Request['type']==1){//如果是入库，四种状态
		switch(value * 1)
		{
		case 0:return '未审核';
		case 1:return '已审核';
		case 2:return '已确认';
		case 3:return '已入库';
		case 8:return '已退貨';
		case 9:return '退货中';
		default: return '';
		}
	}
	
	if(Request['type']==2){//如果是出库，0:未出库 1:已出库
		switch(value * 1)
		{
		case 0:return '未出库';
		case 1:return '已出库';

		
		}
	}
	

}

/**
* 订单明细状态
* 采购：0=未入库，1=已入库  销售：0=未出库，1=已出库
*/
function getOrderDetailState(value){
	if(Request['type']==1){//如果是入库
		switch(value * 1)
		{
			case 0:return '未入库';
		
			case 1:return '已入库';
		}
	}
	if(Request['type']==2){//如果是入库，0:未出库 1:已出库
		switch(value * 1)
		{
			case 0:return '未出库';
			
			case 1:return '已出库';
		}
	}
}


/**
 * 审核订单
 * @returns
 */
function doCheck(){
	$.messager.confirm('提示','确认审核吗？',function(yes){

		if(yes){
			$.ajax({
				type:'post',
				url:"orders_doCheck?id="+$('#uuid').html(),//获取表格中添加的订单id
				dataType:'json',
				success:function(rtn){
					$.messager.alert('提示',rtn.message,'info',function(){
						if(rtn.success){
							//关闭详情页
							$("#ordersDlg").dialog('close');
							//刷新订单页面
							$("#grid").datagrid('reload');
						}
					});
				}
			});
			
		}
	})
	
}

/**
 * 确认订单
 * @returns
 */
function doStart(){
	$.messager.confirm('提示','确定确认订单吗？',function(yes){
		
		if(yes){
			$.ajax({
				type:'post',
				url:"orders_doStart?id="+$('#uuid').html(),//获取表格中添加的订单id
				dataType:'json',
				success:function(rtn){
					$.messager.alert('提示',rtn.message,'info',function(){
						if(rtn.success){
							//关闭详情页
							$("#ordersDlg").dialog('close');
							//刷新订单页面
							$("#grid").datagrid('reload');
						}
					});
				}
			});
			
		}
	})
	
}

/**
 * 根據页面参数动态设置行
 * @returns
 */
function getColunms(){
	if(Request['type']==1 || Request['type']==null){//采购订单
		return [[
		  		    {field:'uuid',title:'编号',width:100},
		  		    {field:'createtime',title:'生成日期',width:100,formatter:FormatDate},
		  		    {field:'checktime',title:'审核日期',width:100,formatter:FormatDate},
		  		    {field:'starttime',title:'确认日期',width:100,formatter:FormatDate},
		  		    {field:'endtime',title:'入库或出库日期',width:100,formatter:FormatDate},
		  		    {field:'type',title:'1:采购 2:销售',width:100},
		  		    {field:'createrName',title:'下单员',width:100},
		  		    {field:'checkerName',title:'审核员',width:100},
		  		    {field:'starterName',title:'采购员',width:100},
		  		    {field:'enderName',title:'库管员',width:100},
		  		    {field:'supplieruuidName',title:'供应商',width:100},
		  		    {field:'totalmoney',title:'合计金额',width:100},
		  		    {field:'state',title:'采购状态',width:100,formatter:getOrderState},
		  		    {field:'waybillsn',title:'运单号',width:100},
		  		    {field:'heh',title:'操作',width:100,hidden:true,formatter:function(value,row,index)
		  		    	{
		  		    		if(state*1==9){
		  		    			return "<a href='#' onclick=''>正在退货</a>"
		  		    		}
		  		    		return "<a href='#' onclick='addReturn("+row.uuid+")'>申请退货</a>";
		  		    	}
		  		    }   
				]];
	}
	
	if(Request['type']==2){//销售订单
		return [[
		         {field:'uuid',title:'编号',width:100},
		         {field:'createtime',title:'生成日期',width:100,formatter:FormatDate},
		        
		         {field:'endtime',title:'出库日期',width:100,formatter:FormatDate},
		         {field:'type',title:'1:采购 2:销售',width:100},
		         {field:'createrName',title:'下单员',width:100},
		         
		         {field:'enderName',title:'库管员',width:100},
		         {field:'supplieruuidName',title:'客户',width:100},
		         {field:'totalmoney',title:'合计金额',width:100},
		         {field:'state',title:'状态',width:100,formatter:getOrderState},
		         {field:'waybillsn',title:'运单号',width:100},  
		         {field:'heh',title:'操作',width:100,hidden:true,formatter:function(value,row,index)
				    {
			    		return "<a href='#' onclick='addReturn("+row.uuid+")'>申请退货</a>";
			    	}
		         }
		         ]];
	}
}

function doInOutStore(){
	
		//获取表单数据的json对象
		var dataForm=$("#itemForm").serializeJSON();
		if(dataForm.storeuuid==null||dataForm.storeuuid==''){
			$.messager.alert('提示','请选择仓库','info');
			return;
		}
		var message='';
		var url='';
		
		if(Request['type']==1){//采购
			message="入库";url='orderdetail_doInStore';
		}
		
		if(Request['type']==2){//销售
			message="出库";url='orderdetail_doOutStore';
		}
		
		
		$.messager.confirm('提示','确认'+message+'吗',function(yes){
			if(yes){
				$.ajax({
					type:'post',
					url:url,
					data:dataForm,
					dataType:'json',
					success:function(rtn){
						$.messager.alert('提示',rtn.message,'info',function(){
							if(rtn.success){
								//关闭入库窗口
								$("#itemDlg").dialog('close');
								//修改入库后的明细状态
								$('#itemgrid').datagrid('getSelected').state=1;
								//刷新明细列表，更新显示状态、
								var data= $("#itemgrid").datagrid('getData');
								 $("#itemgrid").datagrid('loadData',data);
								 
								 //判断所有明细是否已将入库
								 var allIn=true;
							   $.each(data.rows,function(i,row){
								   if(row.state*1==0){
									   allIn=false;
									   return;
								   }
							
							   });
							   if(allIn==true){
								   //关闭明细窗口
								   $("#ordersDlg").dialog('close');
								   //刷新订单表格
								   $("#grid").datagrid('reload');
							   }
								
							}
							
						});
					}
					
				});
			}
		});
	
}

/**
 * d
 * @returns
 */
function exportById(){
	
	$.download('orders_exportById',{id:$('#uuid').html()});
}

/**
 * 添加退货订单
 * @returns
 */
function addReturn(uuid){
	$.messager.confirm('提示',"确认申请退货吗？",function(yes){
	$.ajax({
		url:'returnorders_add?t.ordersuuid='+uuid,
		type:'post',
		dataType:'json',
		success:function(rtn){
			$.messager.alert('提示',rtn.message,'info',function(){
				if(rtn.success){
					$("#grid").datagrid('reload');
				}
				$("#itemDlg").dialog('close');
			})
		}
		
	})
	})
}