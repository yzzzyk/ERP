package com.itcast.erp.dao.impl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import com.itcast.erp.dao.IReturnordersDao;
import com.itcast.erp.entity.Returnorders;
/**
 * 退货订单数据访问类
 * @author Administrator
 *
 */
public class ReturnordersDao extends BaseDao<Returnorders> implements IReturnordersDao {

	
	/**
	 * 构建查询条件
	 * @param dep1
	 * @param dep2
	 * @param param
	 * @return
	 */
	public DetachedCriteria getDetachedCriteria(Returnorders returnorders1,Returnorders returnorders2,Object param){
		DetachedCriteria dc=DetachedCriteria.forClass(Returnorders.class);
		if(returnorders1!=null){
			if(returnorders1.getType()!=null &&  returnorders1.getType().trim().length()>0)
			{
				dc.add(Restrictions.eq("type", returnorders1.getType()));			
			}
			if(returnorders1.getState()!=null &&  returnorders1.getState().trim().length()>0)
			{
				dc.add(Restrictions.eq("state", returnorders1.getState()));			
			}
		
		}		
		return dc;
	}
	
	
}

