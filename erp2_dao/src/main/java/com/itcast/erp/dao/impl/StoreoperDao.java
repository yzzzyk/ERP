package com.itcast.erp.dao.impl;

import java.util.Calendar;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import com.itcast.erp.dao.IStoreoperDao;
import com.itcast.erp.entity.Storeoper;

/**
 * 仓库操作记录数据访问类
 * 
 * @author Administrator
 *
 */
public class StoreoperDao extends BaseDao<Storeoper> implements IStoreoperDao {

	/**
	 * 构建查询条件
	 * 
	 * @param dep1
	 * @param dep2
	 * @param param
	 * @return
	 */
	public DetachedCriteria getDetachedCriteria(Storeoper storeoper1, Storeoper storeoper2, Object param) {
		DetachedCriteria dc = DetachedCriteria.forClass(Storeoper.class);
		if (storeoper1 != null) {
			// 操作类型
			if (storeoper1.getType() != null && storeoper1.getType().trim().length() > 0) {
				dc.add(Restrictions.eq("type", storeoper1.getType()));
			}
			// 员工
			if (storeoper1.getEmpuuid() != null) {
				dc.add(Restrictions.eq("empuuid", storeoper1.getEmpuuid()));
			}

			// 商品编号
			if (storeoper1.getGoodsuuid() != null) {
				dc.add(Restrictions.eq("goodsuuid", storeoper1.getGoodsuuid()));
			}
			// 仓库
			if (storeoper1.getStoreuuid() != null) {
				dc.add(Restrictions.eq("storeuuid", storeoper1.getStoreuuid()));
			}
			// 操作起始时间 起
			if (storeoper1.getOpertime() != null) {
				Calendar c = Calendar.getInstance();
				c.setTime(storeoper1.getOpertime());
				c.set(Calendar.HOUR, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				c.set(Calendar.MILLISECOND, 0);
				// 从 2018 0：0：0
				dc.add(Restrictions.gt("opertime",c.getTime()));
			}

		}

		if (storeoper2 != null) {
			// 操作起始时间 止
			if (storeoper2.getOpertime() != null) {
				
				Calendar c = Calendar.getInstance();
				c.setTime(storeoper2.getOpertime());
				c.set(Calendar.HOUR,23);
				c.set(Calendar.MINUTE, 59);
				c.set(Calendar.SECOND, 59);
				c.set(Calendar.MILLISECOND, 99);
				
				dc.add(Restrictions.le("opertime", c.getTime()));
			}
		}
		return dc;
	}

}
