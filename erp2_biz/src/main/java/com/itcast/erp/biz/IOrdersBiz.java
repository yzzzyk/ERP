package com.itcast.erp.biz;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.itcast.erp.entity.Orders;
import com.itcast.redsun.bos.ws.Waybilldetail;

/**
 * 订单业务逻辑层接口
 * @author Administrator
 *
 */
public interface IOrdersBiz extends IBaseBiz<Orders>{
	
	
	/**
	 * 审核订单
	 * @param uuid  订单id
	 * @param checkeruuid  审核员id
	 */
	public void doCheck(Long uuid,Long checkeruuid);

	
	/**
	 * 确认订单
	 * @param uuid  订单id
	 * @param starteruuid  审核员id
	 */
	public void doStart(Long uuid,Long starteruuid);
	
	/**
	 * 按照订单编号导出Excel文件
	 * @param os 输出流
	 * @param id 订单编号
	 */
	void exportById(OutputStream os,long id)throws IOException;
	
	/**
	 * 根据运单号查询 运单详情列表
	 * @param sn  运单号
	 * @return
	 */
	List<Waybilldetail> waybilldetailList(Long sn);

	
}

