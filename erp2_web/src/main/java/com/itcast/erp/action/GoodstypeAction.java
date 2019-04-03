package com.itcast.erp.action;
import com.itcast.erp.biz.IGoodstypeBiz;
import com.itcast.erp.entity.Goodstype;

/**
 * 商品分类Action 
 * @author Administrator
 *
 */
public class GoodstypeAction extends BaseAction<Goodstype> {

	private IGoodstypeBiz goodstypeBiz;
	
	public void setGoodstypeBiz(IGoodstypeBiz goodstypeBiz) {
		this.goodstypeBiz = goodstypeBiz;
		setBaseBiz(goodstypeBiz);
	}
	
	
}
