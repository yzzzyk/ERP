
//全局常量记录正在被操作的行索引

var existEditIndex=-1;

$(function(){
	
	
	
	$("#ordersgrid").datagrid({
		columns:[[    
		            {field:'goodsuuid',title:'商品编号',width:100,editor:{type:'numberbox',options:{
		            	disabled:true
		            }}},
		  		    {field:'goodsname',title:'商品名称',width:100,editor:{type:'combobox',options:{
		  		    	url:'goods_list',textField:'name',valueField:'name',
		  		    	
		  		    	onSelect:function(goods){
		  		    		
		  					//获取goodsuuid编辑器
		  		    		var goodsuuidEditor=getEditor('goodsuuid');
		  		    		//获取编辑器所对应的对象
		  		    		$(goodsuuidEditor.target).val(goods.uuid);
		  		    		
		  		    		//获取价格编辑器
		  		    		var priceEditor=getEditor('price');
		  		    		if(Request['type']==1){//进货订单，进货价格
		  		    			//获取编辑器所对应的对象
		  		    			$(priceEditor.target).val(goods.inprice);	
		  		    		}if(Request['type']==2){//销售订单，销售价格
		  		    			$(priceEditor.target).val(goods.outprice);
		  		    		}
		  		    		
		  		    		//下拉选框改变，更新每行数据，以及小计
		  		    		cal();
		  		    		sum();
		  		    		//获得数量编辑器
		  		    		var numEditor = getEditor('num');
		  		    		//选中数量编辑器
		  		    		$(numEditor.target).select();
		  		    		
		  		    		
		  		    		bindGridEditor();
		  				}
		  		    }}},
		  		    {field:'price',title:'价格',width:100,editor:{type:'numberbox',options:{
		  		    	precision:2
		  		    }}},
		  		    {field:'num',title:'数量',width:100,editor:'numberbox'},
		  		    {field:'money',title:'金额',width:100,editor:{type:'numberbox',options:{
		  		    	//禁用编辑
		  		    	disabled:true,precision:2
		  		    }} 
		  		    },
		  		    {field:'-',title:'操作',width:100,formatter:function(value,row,index){
		  		    		if(row.num!='小计')
		  		    	 	return "<a href='javascript:void(0)' onclick='del("+index+ ")'>删除</a>"
		  		    }}
		      ]] ,
		      singleSelect:true,
		      rownumbers:true,
		      showFooter:true,
		      toolbar:[{
		    	title:'添加',
		  		iconCls: 'icon-add',
				handler: function(){
						if(existEditIndex > -1){//existEditIndex表示的为上一个可编辑行
							$("#ordersgrid").datagrid('endEdit',existEditIndex);
						}
						//添加新行
						$("#ordersgrid").datagrid('appendRow',{num:0,money:0});
						//获取新添加行的行索引
						existEditIndex=$("#ordersgrid").datagrid('getRows').length-1;
						//开启编辑新添加的行
						$("#ordersgrid").datagrid('beginEdit',existEditIndex);
						
				   }
		     },'-',
		     {
		    	 
		    	title:'保存',
				iconCls: 'icon-save',
				handler: function(){
	
					if(existEditIndex > -1){//存在可编辑框，先关闭编辑						
						$("#ordersgrid").datagrid('endEdit',existEditIndex);
					}
					
					//获取表单数据
					var dataForm=$("#orderForm").serializeJSON();//获得表单json对象
					if(dataForm['t.supplieruuid']==''){//供货商为空直接不允许保存
						$.messager.alert('提示',supplierType+'不能为空','info');
						return;
					}
										
					//获取所有的明细
		     		var rows=$('#ordersgrid').datagrid('getRows');
					
					if(rows.length == 0){//如果表格没有数据，直接跳出
						return;
					}
					 
					var jsonAll=JSON.stringify(rows);//将表格数据 row对象 转化为字符串
	
					dataForm.json=jsonAll;//将行字符串数据 赋值给dataForm json对象的json键上
					$.ajax({
						type:'post',
						url:'orders_add?t.type='+Request['type'],
						data:dataForm,
						dataType:'json',
						success:function(rtn){
					
						$.messager.alert('提示',rtn.message,'info',function(){
							
							if(rtn.success){//添加成功，清空
								//清空供应商，下拉表格
								$('#supplier').combogrid('clear');
								//清空数据表格
								$("#ordersgrid").datagrid('loadData',{total:0,rows:[],fotter:[{num:'小计',money:0}]});
								//清空页脚
								sum();
								$("#addOrdersDlg").dialog('close');
								$("#grid").datagrid('reload');
							}
							
							
						 });
						}
					});
					
					
					
					
				
				}
			}],
			onClickRow:function(index, row){
				//index：点击的行的索引值，该索引值从0开始。
				//row：对应于点击行的记录。
				if(existEditIndex > -1){//existEditIndex表示的为上一个可编辑行,关闭上一个可编辑行
					$("#ordersgrid").datagrid('endEdit',existEditIndex);
				}
				existEditIndex=index;
				
				//开启编辑新添加的行
				$("#ordersgrid").datagrid('beginEdit',existEditIndex);
				//获得数量编辑器
		    	var numEditor = getEditor('num');
		    	//选中数量编辑器
		    	$(numEditor.target).select();
		    	bindGridEditor();	
				
			}		
	});
	//加载行脚
	$('#ordersgrid').datagrid('reloadFooter',[{num: '小计', money:0}]);
	
	
	var _url="";
	var supplierType="";
	if(Request['type']==1){//采购
		_url='supplier_list?t1.type=1';
		supplierType="供应商";
	}
	if(Request['type']==2){//销售
		_url='supplier_list?t1.type=2';
		supplierType="客户";
	}
	//加载下拉数据表格
	$('#supplier').combogrid({    
	    panelWidth:700,    
	   // value:'',    //初始值
	    idField:'uuid',    
	    textField:'name',    
	    url:_url,    
	    columns:[[    
			  {field:'uuid',title:'编号',width:100},
			  {field:'name',title:'名称',width:100},
			  {field:'address',title:'联系地址',width:100},
			  {field:'contact',title:'联系人',width:100},
			  {field:'tele',title:'联系电话',width:100},
			  {field:'email',title:'邮件地址',width:150},
	    ]],
	    mode:'remote'
	});  
	
	
	
	

});
//获取正在编辑行的，行编辑器
function getEditor(_field){
	return $("#ordersgrid").datagrid('getEditor',{index:existEditIndex,field:_field});
}

//计算金额
function cal(){
	//获取编辑器
	var priceEditor=getEditor('price');
	var numEditor=getEditor('num');
	var moneyEditor=getEditor('money');
	
	//获取price num
	var price=$(priceEditor.target).val();
	var num=$(numEditor.target).val();
	
	//计算和
	var total=price * num;
	//保留两位小数
	total=total.toFixed(2);
	//为价格添加值
	$(moneyEditor.target).val(total);
	
	//更新表格数据,再未关闭编辑状态下（编辑器中的数据未保存），手动赋值money
	$("#ordersgrid").datagrid('getRows')[existEditIndex].money=total;
}

//绑定事件

function bindGridEditor(){
	//数量绑定事件
	var numEditor = getEditor('num');
	$(numEditor.target).bind('keyup',function(){
		//计算金额
		cal();
		//计算合计金额
		sum();
	});
	
	var priceEditor = getEditor('price');
	$(priceEditor.target).bind('keyup',function(){
		//计算金额
		cal();
		//计算合计金额
		sum();
	});
}

//计算总和
 function sum(){
	 //得到所有行
	var rows =$("#ordersgrid").datagrid('getRows');
	var sum=0;
	$.each(rows,function(i,row){
		//返回的数据为json字符串，转换为数字
		sum+=parseFloat(row.money);
	});
	//保留两位小数
	sum=sum.toFixed(2);
	$('#ordersgrid').datagrid('reloadFooter',[{num: '小计', money:sum}]);
}

 
 //删除行
 function del(index){
	 
	 //编辑器未提交的数据，不会被获取，删除前，关闭上一个编辑器
	 $("#ordersgrid").datagrid('endEdit',existEditIndex);
	 
	 //获取表格数据
	 var data=$("#ordersgrid").datagrid('getData');
	 //重新加载
	 $("#ordersgrid").datagrid('loadData',data);
	 //删除
	 $("#ordersgrid").datagrid('deleteRow',index);
	 //删除完，调用求和
	 sum();
 }
