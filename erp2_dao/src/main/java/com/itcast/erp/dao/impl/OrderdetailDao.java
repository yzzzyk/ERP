package com.itcast.erp.dao.impl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import com.itcast.erp.dao.IOrderdetailDao;
import com.itcast.erp.entity.Orderdetail;
/**
 * 订单明细数据访问类
 * @author Administrator
 *
 */
public class OrderdetailDao extends BaseDao<Orderdetail> implements IOrderdetailDao {

	
	/**
	 * 构建查询条件
	 * @param dep1
	 * @param dep2
	 * @param param
	 * @return
	 */
	public DetachedCriteria getDetachedCriteria(Orderdetail orderdetail1,Orderdetail orderdetail2,Object param){
		DetachedCriteria dc=DetachedCriteria.forClass(Orderdetail.class);
		if(orderdetail1!=null){
			if(orderdetail1.getGoodsname()!=null &&  orderdetail1.getGoodsname().trim().length()>0)
			{
				dc.add(Restrictions.eq("goodsname", orderdetail1.getGoodsname()));			
			}
			if(orderdetail1.getState()!=null &&  orderdetail1.getState().trim().length()>0)
			{
				dc.add(Restrictions.eq("state", orderdetail1.getState()));			
			}
			
			if(orderdetail1.getOrders()!=null )
			{
				dc.add(Restrictions.eq("orders", orderdetail1.getOrders()));			
			}
		
		}		
		return dc;
	}
	
	
}

