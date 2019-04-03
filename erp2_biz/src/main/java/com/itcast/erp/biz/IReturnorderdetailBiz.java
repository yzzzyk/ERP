package com.itcast.erp.biz;
import com.itcast.erp.entity.Returnorderdetail;
/**
 * 退货订单明细业务逻辑层接口
 * @author Administrator
 *
 */
public interface IReturnorderdetailBiz extends IBaseBiz<Returnorderdetail>{
	
	 /**
	  * 采购 退货出库
	 * @param 该订单明细的uuid
	 * @param storeUuid 出库仓库的UUID
	 * @param empid 操作的人的uuID
	 */
	void doReturnOutStore(Long uuid, Long storeuuid, Long empid);
}

