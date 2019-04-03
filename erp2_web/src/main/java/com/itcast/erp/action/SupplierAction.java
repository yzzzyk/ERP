package com.itcast.erp.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.itcast.erp.biz.ISupplierBiz;
import com.itcast.erp.entity.Supplier;
import com.itcast.erp.exception.ErpException;

/**
 * 供应商Action
 * 
 * @author Administrator
 *
 */
public class SupplierAction extends BaseAction<Supplier> {

	private ISupplierBiz supplierBiz;
	// q 联想输入，输入框改变就会将里边的值封装到q 中
	private String q;
	private File file;
	private String fileFileName;
	private String fileContentType;

	public void setSupplierBiz(ISupplierBiz supplierBiz) {
		this.supplierBiz = supplierBiz;
		setBaseBiz(supplierBiz);
	}

	/**
	 * 查询全部列表
	 */
	public void list() {

		Supplier supplier = getT1();
		if (supplier == null) {
			setT1(new Supplier());
		}
		getT1().setName(q);
		super.list();

	}

	/**
	 * 供应商/客户信息导出
	 */
	public void export() {

		String filename = null;
		if (Supplier.TYPE_CUSTOMER.equals(getT1().getType())) {
			filename = "客户列表.xls";
		}

		if (Supplier.TYPE_SUPPLIER.equals(getT1().getType())) {
			filename = "供应商列表.xls";
		}

		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String(filename.getBytes(), "iso-8859-1"));
			supplierBiz.export(response.getOutputStream(), getT1());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 供应商/客户信息导入
	 */
	public void inport() {
		// 判断文件类型，必须为Excel
		if (!"application/vnd.ms-excel".equals(fileContentType)) {
			ajaxReturn(false, "导入文件格式不正确，必须为.xls");
			return;
		}
		try {
			//读取文件，并导入
			supplierBiz.doInport(new FileInputStream(file));
			ajaxReturn(true, "文件导入成功");
			
		} catch (ErpException e) {
			ajaxReturn(false, e.getMessage());
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			ajaxReturn(false, "上传文件失败");
		}

	}

	public void setQ(String q) {
		this.q = q;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

}
