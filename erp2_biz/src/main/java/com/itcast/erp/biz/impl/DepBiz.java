package com.itcast.erp.biz.impl;
import com.itcast.erp.biz.IDepBiz;
import com.itcast.erp.dao.IDepDao;
import com.itcast.erp.entity.Dep;
/**
 * 部门业务逻辑类
 * @author Administrator
 *
 */
public class DepBiz extends BaseBiz<Dep> implements IDepBiz {

	private IDepDao depDao;
	
	public void setDepDao(IDepDao depDao) {
		this.depDao = depDao;
		setBaseDao(depDao);
	}

	
}
