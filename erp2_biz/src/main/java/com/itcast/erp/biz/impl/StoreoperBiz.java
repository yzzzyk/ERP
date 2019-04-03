package com.itcast.erp.biz.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itcast.erp.biz.IStoreoperBiz;
import com.itcast.erp.dao.IEmpDao;
import com.itcast.erp.dao.IGoodsDao;
import com.itcast.erp.dao.IStoreDao;
import com.itcast.erp.dao.IStoreoperDao;
import com.itcast.erp.entity.Storeoper;

/**
 * 仓库操作记录业务逻辑类
 * 
 * @author Administrator
 *
 */
public class StoreoperBiz extends BaseBiz<Storeoper> implements IStoreoperBiz {

	private IStoreoperDao storeoperDao;
	private IGoodsDao goodsDao;
	private IEmpDao empDao;
	private IStoreDao storeDao;

	public void setStoreoperDao(IStoreoperDao storeoperDao) {
		this.storeoperDao = storeoperDao;
		setBaseDao(storeoperDao);
	}

	public List<Storeoper> getListByPage(Storeoper t1, Storeoper t2, Object param, int firstResult, int maxResults) {
		List<Storeoper> loglist = super.getListByPage(t1, t2, param, firstResult, maxResults);

		Map<Long, String> empNameMap = new HashMap<>();
		Map<Long, String> storeNameMap = new HashMap<>();
		Map<Long, String> goodsNameMap = new HashMap<>();
		
		for (Storeoper storeoper : loglist) {
			storeoper.setEmpName(getEmpName(storeoper.getEmpuuid(), empNameMap, empDao));
			storeoper.setGoodsName(getGoodsName(storeoper.getGoodsuuid(), goodsNameMap, goodsDao));
			storeoper.setStoreName(getStoreName(storeoper.getStoreuuid(), storeNameMap, storeDao));

		}

		return loglist;
	}

	public void setGoodsDao(IGoodsDao goodsDao) {
		this.goodsDao = goodsDao;
	}

	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
	}

	public void setStoreDao(IStoreDao storeDao) {
		this.storeDao = storeDao;
	}

}
