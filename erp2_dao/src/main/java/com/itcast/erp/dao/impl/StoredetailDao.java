package com.itcast.erp.dao.impl;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import com.itcast.erp.dao.IStoredetailDao;
import com.itcast.erp.entity.StoreAlert;
import com.itcast.erp.entity.Storedetail;
/**
 * 仓库库存数据访问类
 * @author Administrator
 *
 */
public class StoredetailDao extends BaseDao<Storedetail> implements IStoredetailDao {

	
	/**
	 * 构建查询条件
	 * @param dep1
	 * @param dep2
	 * @param param
	 * @return
	 */
	public DetachedCriteria getDetachedCriteria(Storedetail storedetail1,Storedetail storedetail2,Object param){
		DetachedCriteria dc=DetachedCriteria.forClass(Storedetail.class);
		if(storedetail1!=null){
			//根据商品编号去查
			if(storedetail1.getGoodsuuid()!=null){
				dc.add(Restrictions.eq("goodsuuid", storedetail1.getGoodsuuid()));
			}
			
			//根据仓库编号去查
			if(storedetail1.getStoreuuid()!=null){
				dc.add(Restrictions.eq("storeuuid", storedetail1.getStoreuuid()));
			}
		}		
		return dc;
	}

	@Override
	public List<StoreAlert> getStoreAlertList() {
		String hql="from StoreAlert where outnum>storenum";	//获取需要预警的商品 预警信息
		return (List<StoreAlert>) getHibernateTemplate().find(hql);
	}
	
	
}

