package com.itcast.erp.biz;
import com.itcast.erp.entity.Orderdetail;
/**
 * 订单明细业务逻辑层接口
 * @author Administrator
 *
 */
public interface IOrderdetailBiz extends IBaseBiz<Orderdetail>{
	
	/**
	 * @param uuid  订单明细 的 uuid
	 * @param storeUuid  入库仓库 id 
	 * @param empid 当前登录的员工id
	 */
	public void doInstore(Long  uuid,Long  storeUuid,Long empid);
	
	/**
	 * @param uuid  订单明细 的 uuid
	 * @param storeUuid  出库仓库 id 
	 * @param empid 当前登录的员工id
	 */
	public void doOutstore(Long uuid, Long storeUuid, Long empid);
	
	
}

