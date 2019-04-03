package com.itcast.erp.biz.impl;
import com.itcast.erp.biz.IGoodstypeBiz;
import com.itcast.erp.dao.IGoodstypeDao;
import com.itcast.erp.entity.Goodstype;
/**
 * 商品分类业务逻辑类
 * @author Administrator
 *
 */
public class GoodstypeBiz extends BaseBiz<Goodstype> implements IGoodstypeBiz {

	private IGoodstypeDao goodstypeDao;
	
	public void setGoodstypeDao(IGoodstypeDao goodstypeDao) {
		this.goodstypeDao = goodstypeDao;
		setBaseDao(goodstypeDao);
	}

	
}
