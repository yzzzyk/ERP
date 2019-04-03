$(function() {

	var listParam = "";
	var _url = name + '_listByPage.action';

	var toolBar = new Array();

	// 初始化订单明细数据表格
	$("#itemgrid").datagrid({
		singleSelect : true,// 唯一选择
		pagination : true,// 底部分页栏
		fitColumns : true,// 真正的自动展开/收缩列的大小，以适应网格的宽度，防止水平滚动。
		columns : [ [ {
			field : 'uuid',
			title : '编号',
			width : 100
		}, {
			field : 'goodsuuid',
			title : '商品编号',
			width : 100
		}, {
			field : 'goodsname',
			title : '商品名称',
			width : 100
		}, {
			field : 'price',
			title : '价格',
			width : 100
		}, {
			field : 'num',
			title : '数量',
			width : 100
		}, {
			field : 'money',
			title : '金额',
			width : 90
		}, {
			field : 'endtime',
			title : '结束日期',
			width : 160,
			formatter : FormatDate
		}, {
			field : 'state',
			title : '状态',
			width : 100,
			formatter : getOrderDetailState
		}, ] ]

	});
	// 初始化出库
	$("#itemDlg").dialog({
		title : '出库',
		width : 250,
		height : 180
	})

	if (Request['type'] == 1) {// 原订单为采购

		if (Request['oper'] == 'ReturnCheck') {
			document.title = "采购退货订单审核";
			_url += "?t1.type=1&t1.state=0";
			// 审核操作添加审核按钮
			toolBar.push({
				text : '审核订单',
				iconCls : 'icon-search',
				handler : function() {
					doCheck();
				}
			});
		}

		// 如果是出库操作
		if (Request['oper'] == 'ReturnOutStore') {
			document.title = "采购退货订单出库";
			_url += "?t1.type=1&t1.state=1";

			// 添加双击事件
			$("#itemgrid")
					.datagrid(
							{
								onClickRow : function(index, row) {
									// alert(JSON.stringify(row));
									$("#goodsuuid").html(row.goodsuuid);
									$("#goodsname").html(row.goodsname);
									$("#num").html(row.num);
									$("#itemuuid").val(row.uuid);
									

									$("#itemDlg")
											.dialog(
													{
														buttons : [ {
															text : '保存',
															handler : function() {

																var FormData = $(
																		"#itemForm")
																		.serializeJSON();
																// alert(2);
																$.ajax({
																			url : 'returnorderdetail_doReturnOutStore',
																			type : 'post',
																			dataType : 'json',
																			data : FormData,
																			success : function(rtn) {
																			$.messager.alert("提示",rtn.message,'info',function() {
																				//关闭窗口
																				$("#itemDlg").dialog('close');
																				if (rtn.success) {
																					//修改出库后的明细状态
																					$("#itemgrid").datagrid('getSelected').state=1;
																					//刷新明细列表，更新显示状态、
																					var data= $("#itemgrid").datagrid('getData');
																					$("#itemgrid").datagrid('loadData',data);
																					
																					//判断所有明细是否已将出库
																					 var allOut=true;
																					 $.each(data.rows,function(i,row){
																						 if(row.state*1==0)
																							 {allOut=false;
																							 return;//有一个没出库直接返回
																							 }
																					 })
																					if(allOut)
																						{
																							$("#ROrdersDlg").dialog('close');
																							$("#grid").datagrid('reload');
																						}
																				} 
																				});

																			}
																		});
															}
														} ]
													});
									$("#itemDlg").dialog('open');
								}
							})
		}
	}

	// 表格数据初始化
	$('#grid').datagrid({
		url : _url + listParam,
		columns : [ [ {
			field : 'uuid',
			title : '编号',
			width : 80
		}, {
			field : 'createtime',
			title : '生成日期',
			width : 120,
			formatter : FormatDate
		}, {
			field : 'checktime',
			title : '检查日期',
			width : 120,
			formatter : FormatDate
		}, {
			field : 'endtime',
			title : '结束日期',
			width : 120,
			formatter : FormatDate
		}, {
			field : 'type',
			title : '订单类型',
			width : 100
		}, {
			field : 'creater',
			title : '下单员',
			width : 100
		}, {
			field : 'checker',
			title : '审核员',
			width : 100
		}, {
			field : 'ender',
			title : '库管员',
			width : 100
		},

		{
			field : 'totalmoney',
			title : '合计金额',
			width : 100
		}, {
			field : 'state',
			title : '订单状态',
			width : 100,
			formatter : getOrderState
		}, {
			field : 'waybillsn',
			title : '运单号',
			width : 100
		}, {
			field : 'ordersuuid',
			title : '原订单编号',
			width : 100
		} ] ],
		fitColumns : true,
		singleSelect : true,
		pagination : true,
		onDblClickRow : function(index, row) {

			$("#ROrdersDlg").dialog({
				toolbar : toolBar
			})

			// 加载表头
			$("#uuid").html(row.uuid);
			$("#totalmoney").html(row.totalmoney);
			$("#ordersuuid").html(row.ordersuuid);
			$("#state").html(getOrderState(row.state));

			$("#createrName").html(row.creater);
			$("#checkerName").html(row.checker);
			$("#enderName").html(row.ender);

			$("#createtime").html(FormatDate(row.createtime));
			$("#checktime").html(FormatDate(row.checktime));
			$("#starttime").html(FormatDate(row.starttime));
			$("#endtime").html(FormatDate(row.endtime));
			// 运单号
			$("#waybillsn").html(row.waybillsn);

			// 添加

			$("#itemgrid").datagrid('loadData', row.returnorderdetailList);
			$("#ROrdersDlg").dialog('open');

		}

	});

})

function FormatDate(_date) {
	return new Date(_date).Format("yyyy-MM-dd hh:mm");
}
function getOrderState(state) {

	if (state == 0) {
		return '未审核';
	}
	if (state == 1) {
		return '已审核';
	}
	if (state == 2) {
		return '已出库';
	}
}
function getOrderDetailState(state) {
	
	if (state == 0) {
		return '未出库';
	}
	if (state == 1) {
		return '已出库';
	}
}

/**
 * 购买订单退货审核
 * 
 * @returns
 */
function doCheck() {
	$.messager.confirm('提示', '確認审核吗', function(yes) {
		if (yes)
			$.ajax({
				url : 'returnorders_doReturnCheck?id=' + $("#uuid").html(),
				type : 'post',
				dataType : 'json',
				success : function(rtn) {
					$.messager.alert('提示', rtn.message, 'info', function() {
						if (rtn.success) {
							// 关闭 订单详情弹窗
							$("#ROrdersDlg").dialog('close');
							// 重新加载待审核列表
							$("#grid").datagrid('reload');
						}
					});
				}
			});
	})
}
