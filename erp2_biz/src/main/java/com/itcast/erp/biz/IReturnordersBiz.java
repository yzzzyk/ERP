package com.itcast.erp.biz;
import com.itcast.erp.entity.Returnorders;
/**
 * 退货订单业务逻辑层接口
 * @author Administrator
 *
 */
public interface IReturnordersBiz extends IBaseBiz<Returnorders>{
	
	/**
	 * 采购退货审核订单
	 * @param uuid  订单id
	 * @param checkeruuid  审核员id
	 */
	 void doReturnCheck(Long uuid,Long checkeruuid);
	 
	
}

