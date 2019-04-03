package com.itcast.erp.biz.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;

import com.itcast.erp.biz.IOrdersBiz;
import com.itcast.erp.dao.IDepDao;
import com.itcast.erp.dao.IEmpDao;
import com.itcast.erp.dao.IOrdersDao;
import com.itcast.erp.dao.ISupplierDao;
import com.itcast.erp.entity.Orderdetail;
import com.itcast.erp.entity.Orders;
import com.itcast.erp.exception.ErpException;
import com.itcast.redsun.bos.ws.Waybilldetail;
import com.itcast.redsun.bos.ws.impl.IWaybillWs;

/**
 * 订单业务逻辑类
 * 
 * @author Administrator
 *
 */
public class OrdersBiz extends BaseBiz<Orders> implements IOrdersBiz {

	private IOrdersDao ordersDao;
	private IEmpDao empDao;
	private ISupplierDao supplierDao;
	private IWaybillWs waybillWs;

	public void setOrdersDao(IOrdersDao ordersDao) {
		this.ordersDao = ordersDao;
		setBaseDao(ordersDao);
	}

	@Override
	public void add(Orders orders) {
		
		Subject subject = SecurityUtils.getSubject();
		//购买订单
		if(Orders.TYPR_IN.equals(orders.getType())){
			if(!subject.isPermitted("采购订单录入")){
				throw new ErpException("您没有录入新采购订单的权限");
			}
			// 订单创建时默认为 未审核
			orders.setState(orders.STATE__UNCHECK);
			
		}else if (Orders.TYPR_OUT.equals(orders.getType())){
			if(!subject.isPermitted("销售订单录入")){
				throw new ErpException("您没有录入新销售订单的权限");
			}
			// 订单创建时默认为 未出库
			orders.setState(orders.STATE_NOT_OUT);
		}else{
			throw new ErpException("参数违规");
		}

		

		// 通过页面传参设置订单类型
		/*
		 * // 设置订单类型 进货 orders.setType(orders.TYPR_IN);
		 */
		// 设置时间为当前创建时间
		orders.setCreatetime(new Date());
		// 计算总金额
		double total = 0;
		for (Orderdetail orderdetail : orders.getOrderdetail()) {

			total += orderdetail.getMoney();
			// 设置明细状态为 未入库
			orderdetail.setState(orderdetail.STATE__NOT_IN);
			// ============================================================================
			// 设置明细对应的订单，原因 ：orders采用级联更新，且外键有Orderdetail维护
			orderdetail.setOrders(orders);
			// ============================================================================
		}
		// 设置该订单总金额
		orders.setTotalmoney(total);
		// 保存订单
		super.add(orders);
	}

	/**
	 * 分页条件查询
	 */
	public List<Orders> getListByPage(Orders orders1, Orders orders2, Object param, int firstResult, int maxResults) {

		// 获得原始分页数据
		List<Orders> list = super.getListByPage(orders1, orders2, param, firstResult, maxResults);

		Map<Long, String> empNameMap = new HashMap<>();
		Map<Long, String> SupplierNameMap = new HashMap<>();
		for (Orders orders : list) {

			orders.setCreaterName(getEmpName(orders.getCreater(), empNameMap, empDao));// 設置下单员姓名
			orders.setCheckerName(getEmpName(orders.getChecker(), empNameMap, empDao));// 設置审核员姓名
			orders.setStarterName(getEmpName(orders.getStarter(), empNameMap, empDao));// 設置采购员姓名
			orders.setEnderName(getEmpName(orders.getEnder(), empNameMap, empDao));// 設置库管员员姓名

			// 设置供货商姓名
			orders.setSupplieruuidName(getSurpplierName(orders.getSupplieruuid(), SupplierNameMap));
		}

		return list;
	}

	private String getSurpplierName(Long uuid, Map<Long, String> nameMap) {
		//
		if (uuid == null) {
			return null;
		}
		//
		String surpplierName = nameMap.get(uuid);
		if (surpplierName == null) {// 集合中没有,需要查询
			surpplierName = supplierDao.get(uuid).getName();
		}
		return surpplierName;
	}

	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
	}

	public void setSupplierDao(ISupplierDao supplierDao) {
		this.supplierDao = supplierDao;
	}


	@RequiresPermissions("采购订单审核")
	public void doCheck(Long uuid, Long empuuid) {
		// 获取订单id
		Orders orders = ordersDao.get(uuid);
		if (!orders.getState().equals(Orders.STATE__UNCHECK)) {// 订单不是未审核状态
			throw new ErpException("该订单已经审核过了");
		}
		// 当前时间为审核时间
		orders.setChecktime(new Date());
		// 设置审核人的id
		orders.setChecker(empuuid);
		// 更新订单状态为已审核
		orders.setState(Orders.STATE_CHECKED);

	}

	@RequiresPermissions("采购订单确认")
	public void doStart(Long uuid, Long starteruuid) {
		// 获取订单id
		Orders orders = ordersDao.get(uuid);
		if (!orders.getState().equals(Orders.STATE_CHECKED)) {// 只允许状态为已审核的订单 确认
			throw new ErpException("该订单确认过了");
		}

		orders.setStarttime(new Date());
		orders.setStarter(starteruuid);
		orders.setState(Orders.STATE_START);// 更新状态为已确认
	}

	@Override
	public void exportById(OutputStream os, long id) throws IOException {
		
		String type = ordersDao.get(id).getType();
		
		// 获取订单信息
				Orders orders = ordersDao.get(id);
				List<Orderdetail> orderdetails = orders.getOrderdetail();

		// 创建工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		// 创建工作表
		HSSFSheet sheet=null;
		if(Orders.TYPR_IN.equals(type)){
			sheet = wb.createSheet("采购订单");			
		}
		if(Orders.TYPR_OUT.equals(type)){
			sheet = wb.createSheet("销售订单");			
		}

		// 创建内容样式（因为样式可以使用该工作簿的各个工作表，所以由工作簿创建）
		HSSFCellStyle cellStyle = wb.createCellStyle();

		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

		// 内容设置对齐方式和字体
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		// 内容部分字体
		HSSFFont font_content = wb.createFont();
		font_content.setFontName("宋体");
		font_content.setFontHeightInPoints((short) 11);// 设置字体大小
		cellStyle.setFont(font_content);

		// 设置日期格式
		HSSFDataFormat dataformat = wb.createDataFormat();
		HSSFCellStyle style_date = wb.createCellStyle();
		style_date.cloneStyleFrom(cellStyle);// 复制 内容单元格的其他格式
		style_date.setDataFormat(dataformat.getFormat("yyyy-MM-dd hh:mm:ss"));

		// 根据导出的订单样本创建10行4列

		for (int i = 2; i <=9+orderdetails.size(); i++) {
			HSSFRow row = sheet.createRow(i);
			for (int j = 0; j < 4; j++) {
				HSSFCell cell = row.createCell(j);
				cell.setCellStyle(cellStyle);
			}

		}
		// 应用日期样式
		for (int i = 3; i <= 6; i++) {
			sheet.getRow(i).getCell(1).setCellStyle(style_date);
		}

		// 合并单元格
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 3));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 3));
		sheet.addMergedRegion(new CellRangeAddress(7, 7, 0, 3));

		// 设置固定文本值
		sheet.getRow(2).getCell(0).setCellValue("供应商");

		sheet.getRow(3).getCell(0).setCellValue("下单日期");
		sheet.getRow(3).getCell(2).setCellValue("经办人");

		sheet.getRow(4).getCell(0).setCellValue("审核日期");
		sheet.getRow(4).getCell(2).setCellValue("经办人");

		sheet.getRow(5).getCell(0).setCellValue("采购日期");
		sheet.getRow(5).getCell(2).setCellValue("经办人");

		sheet.getRow(6).getCell(0).setCellValue("入库日期");
		sheet.getRow(6).getCell(2).setCellValue("经办人");
		
		if(Orders.TYPR_IN.equals(type)){
			sheet.createRow(0).createCell(0).setCellValue("采购单");		
		}
		if(Orders.TYPR_OUT.equals(type)){
			sheet.createRow(0).createCell(0).setCellValue("销售单");	
		}
		
		sheet.getRow(7).createCell(0).setCellValue("订单明细");

		HSSFRow row8 = sheet.getRow(8);
		row8.getCell(0).setCellValue("商品名称");
		row8.getCell(1).setCellValue("数量");
		row8.getCell(2).setCellValue("价格");
		row8.getCell(3).setCellValue("金额");

		// 标题样式
		HSSFCellStyle style_title = wb.createCellStyle();
		style_title.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style_title.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFFont font_title = wb.createFont();
		font_title.setFontName("黑体");
		font_title.setFontHeightInPoints((short) 18);
		font_title.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		style_title.setFont(font_title);
		sheet.getRow(0).getCell(0).setCellStyle(style_title);

		// 设置
		sheet.getRow(7).getCell(0).setCellStyle(cellStyle);

		// 设置首行高和列宽
		sheet.getRow(0).setHeight((short) 1000);
		// 设置内容部分行高
		for (int i = 2; i <= 9+orderdetails.size(); i++) {
			sheet.getRow(i).setHeight((short) 500);
		}
		// 设置列宽
		for (int i = 0; i < 4; i++) {
			sheet.setColumnWidth(i, 5000);
		}

		
		
		//将id转化为姓名
		Map<Long, String> empNameMap = new HashMap<>();
		Map<Long, String> SupplierNameMap = new HashMap<>();
		//设置订单基本信息
		sheet.getRow(2).getCell(1).setCellValue(getSurpplierName(orders.getSupplieruuid(), SupplierNameMap));
		sheet.getRow(3).getCell(1).setCellValue(orders.getCreatetime());
		sheet.getRow(3).getCell(3).setCellValue(getEmpName(orders.getCreater(), empNameMap, empDao));
		
		if (orders.getChecktime() != null)
			sheet.getRow(4).getCell(1).setCellValue(orders.getChecktime());
		sheet.getRow(4).getCell(3).setCellValue(getEmpName(orders.getChecker(), empNameMap, empDao));
		
		if (orders.getStarttime() != null)
		sheet.getRow(5).getCell(1).setCellValue(orders.getStarttime());
		sheet.getRow(5).getCell(3).setCellValue(getEmpName(orders.getStarter(), empNameMap, empDao));
		
		if (orders.getEndtime() != null)
		sheet.getRow(6).getCell(1).setCellValue(orders.getEndtime());
		sheet.getRow(6).getCell(3).setCellValue(getEmpName(orders.getEnder(), empNameMap, empDao));
		
		int rowIndex=9;
		HSSFRow row=null;
		for (Orderdetail orderdetail : orderdetails) {
			row = sheet.getRow(rowIndex);
			row.getCell(0).setCellValue(orderdetail.getGoodsname());
			row.getCell(1).setCellValue(orderdetail.getNum());
			row.getCell(2).setCellValue(orderdetail.getPrice());
			row.getCell(3).setCellValue(orderdetail.getMoney());
			
			rowIndex++;
		}
		//合计
		sheet.getRow(rowIndex).getCell(0).setCellValue("小计");
		sheet.getRow(rowIndex).getCell(3).setCellValue(orders.getTotalmoney());
		
		
		if(Orders.TYPR_OUT.equals(type)){
			sheet.removeRow(sheet.getRow(4));
			sheet.removeRow(sheet.getRow(5));
		}
		
		//保存工作簿到输出流
		wb.write(os);
	}

	@Override
	public List<Waybilldetail> waybilldetailList(Long sn) {
	
		return waybillWs.waybilldetailList(sn);
	}


	public void setWaybillWs(IWaybillWs waybillWs) {
		this.waybillWs = waybillWs;
	}

}
