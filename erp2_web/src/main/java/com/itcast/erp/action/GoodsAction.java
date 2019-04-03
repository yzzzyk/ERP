package com.itcast.erp.action;
import com.itcast.erp.biz.IGoodsBiz;
import com.itcast.erp.entity.Goods;

/**
 * 商品Action 
 * @author Administrator
 *
 */
public class GoodsAction extends BaseAction<Goods> {

	private IGoodsBiz goodsBiz;
	
	public void setGoodsBiz(IGoodsBiz goodsBiz) {
		this.goodsBiz = goodsBiz;
		setBaseBiz(goodsBiz);
	}
	
	
}
