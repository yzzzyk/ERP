package com.itcast.erp.dao;

import java.util.List;

import com.itcast.erp.entity.StoreAlert;
import com.itcast.erp.entity.Storedetail;
/**
 * 仓库库存数据访问接口
 * @author Administrator
 *
 */
public interface IStoredetailDao extends IBaseDao<Storedetail>{
	
	
	/**
	 * 获取商品库存预警信息
	 * @return
	 */
	List<StoreAlert> getStoreAlertList();
}
