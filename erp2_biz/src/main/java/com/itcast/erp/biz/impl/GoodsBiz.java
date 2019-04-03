package com.itcast.erp.biz.impl;
import com.itcast.erp.biz.IGoodsBiz;
import com.itcast.erp.dao.IGoodsDao;
import com.itcast.erp.entity.Goods;
/**
 * 商品业务逻辑类
 * @author Administrator
 *
 */
public class GoodsBiz extends BaseBiz<Goods> implements IGoodsBiz {

	private IGoodsDao goodsDao;
	
	public void setGoodsDao(IGoodsDao goodsDao) {
		this.goodsDao = goodsDao;
		setBaseDao(goodsDao);
	}

	
}
