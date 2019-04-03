package com.itcast.erp.biz;
import java.io.InputStream;
import java.io.OutputStream;

import com.itcast.erp.entity.Supplier;
/**
 * 供应商业务逻辑层接口
 * @author Administrator
 *
 */
public interface ISupplierBiz extends IBaseBiz<Supplier>{
	

	/**
	 * 供应商信息导出
	 * @param os 输出流
	 * @param t1 查询条件
	 */
	void export(OutputStream os,Supplier t1);
	
	/**
	 * 供应商信息导入
	 * @param os 输入流
	 */
	void doInport(InputStream is);
	
}

