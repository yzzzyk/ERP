package com.itcast.erp.biz.impl;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.itcast.erp.biz.ISupplierBiz;
import com.itcast.erp.dao.ISupplierDao;
import com.itcast.erp.entity.Supplier;
import com.itcast.erp.exception.ErpException;
/**
 * 供应商业务逻辑类
 * @author Administrator
 *
 */
public class SupplierBiz extends BaseBiz<Supplier> implements ISupplierBiz {

	private ISupplierDao supplierDao;
	
	public void setSupplierDao(ISupplierDao supplierDao) {
		this.supplierDao = supplierDao;
		setBaseDao(supplierDao);
	}

	@Override
	public void export(OutputStream os, Supplier t1) {
		
		//条件查询供应商信息
		List<Supplier> list = supplierDao.getList(t1, null, null);
		//创建工作薄
		HSSFWorkbook wb=new HSSFWorkbook();
		//创建工作表
		HSSFSheet sheet =null ;
		if(t1.TYPE_SUPPLIER.equals(t1.getType())){//供货商
			sheet =wb.createSheet("供货商");
		}
		if(t1.TYPE_CUSTOMER.equals(t1.getType())){//供货商
			sheet =wb.createSheet("客户");
		}
		
		
		//创建表头
		HSSFRow rowhead = sheet.createRow(0);
		String[] head={"名称","联系地址","联系人","联系电话","邮件地址"};
		int[] width={4000,8000,2000,3000,8000};
		HSSFCell cell=null; 
		for (int i = 0; i < head.length; i++) {
			cell= rowhead.createCell(i);
			cell.setCellValue(head[i]);
			//设置单元格的宽度
			sheet.setColumnWidth(i,width[i]);
		}
		
		
		//写入查询的内容,从第二行开始写
		int i=1;
		HSSFRow row =null;
		for (Supplier supplier : list) {
			row= sheet.createRow(i);
	
			row.createCell(0).setCellValue(supplier.getName());//名称
			row.createCell(1).setCellValue(supplier.getAddress());
			row.createCell(2).setCellValue(supplier.getContact());
			row.createCell(3).setCellValue(supplier.getTele());
			row.createCell(4).setCellValue(supplier.getEmail());
			
			i++;
		}
		//写入到输出流中
		try {
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		
		
	}

	@Override
	public void doInport(InputStream is) {
		
		HSSFWorkbook book;
		try {
			//使用流创建工作簿
			book = new HSSFWorkbook(is);
			//得到工作表
			HSSFSheet sheet = book.getSheetAt(0);
			String sheetName = sheet.getSheetName();
			String type=null;
			if("客户".equals(sheetName)){
				type=Supplier.TYPE_CUSTOMER;
			}
			else if("供货商".equals(sheetName)){
				type=Supplier.TYPE_SUPPLIER;
			}else
				throw new ErpException("工作表格式不正确");
			
			//读取数据
			//获得工作表的最后一行行号
			int lastRowNum = sheet.getLastRowNum();
			Supplier supplier=null;
			for(int row=1;row<=lastRowNum;row++){
				supplier=new Supplier();
				
				supplier.setName(sheet.getRow(row).getCell(0).getStringCellValue());//名称
				
				List<Supplier> list = supplierDao.getList(null, supplier, null);
				if(list.size()>0){//表示已经存在，更新
					supplier=list.get(0);
				}
				
				supplier.setAddress(sheet.getRow(row).getCell(1).getStringCellValue());//地址
				supplier.setContact(sheet.getRow(row).getCell(2).getStringCellValue());//联系人
				
				String tele=sheet.getRow(row).getCell(3).getStringCellValue();
				supplier.setTele(tele);//联系电话
				supplier.setEmail(sheet.getRow(row).getCell(4).getStringCellValue());//email
				
				if(list.size()==0){
					supplier.setType(type);
					supplierDao.add(supplier);
				}
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	
}
