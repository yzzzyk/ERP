package com.itcast.erp.dao.impl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import com.itcast.erp.dao.IOrdersDao;
import com.itcast.erp.entity.Orders;
/**
 * 订单数据访问类
 * @author Administrator
 *
 */
public class OrdersDao extends BaseDao<Orders> implements IOrdersDao {

	
	/**
	 * 构建查询条件
	 * @param dep1
	 * @param dep2
	 * @param param
	 * @return
	 */
	public DetachedCriteria getDetachedCriteria(Orders orders1,Orders orders2,Object param){
		DetachedCriteria dc=DetachedCriteria.forClass(Orders.class);
		if(orders1!=null){
			if(orders1.getType()!=null &&  orders1.getType().trim().length()>0)
			{
				dc.add(Restrictions.eq("type", orders1.getType()));			
			}
			if(orders1.getState()!=null &&  orders1.getState().trim().length()>0)
			{
				dc.add(Restrictions.like("state", orders1.getState(), MatchMode.ANYWHERE));			
			}
			
			//根據登录用户（创建者）进行查询
			if(orders1.getCreater()!=null)
			{
				dc.add(Restrictions.eq("creater", orders1.getCreater()));			
			}
		
		}		
		return dc;
	}
	
	
}

