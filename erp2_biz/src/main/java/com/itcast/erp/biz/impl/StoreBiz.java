package com.itcast.erp.biz.impl;
import com.itcast.erp.biz.IStoreBiz;
import com.itcast.erp.dao.IStoreDao;
import com.itcast.erp.entity.Store;
/**
 * 仓库业务逻辑类
 * @author Administrator
 *
 */
public class StoreBiz extends BaseBiz<Store> implements IStoreBiz {

	private IStoreDao storeDao;
	
	public void setStoreDao(IStoreDao storeDao) {
		this.storeDao = storeDao;
		setBaseDao(storeDao);
	}

	
}
