package com.itcast.erp.dao.impl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import com.itcast.erp.dao.IDepDao;
import com.itcast.erp.entity.Dep;
/**
 * 部门数据访问类
 * @author Administrator
 *
 */
public class DepDao extends BaseDao<Dep> implements IDepDao {

	
	/**
	 * 构建查询条件
	 * @param dep1
	 * @param dep2
	 * @param param
	 * @return
	 */
	public DetachedCriteria getDetachedCriteria(Dep dep1,Dep dep2,Object param){
		DetachedCriteria dc=DetachedCriteria.forClass(Dep.class);
		if(dep1!=null){
			if(dep1.getName()!=null &&  dep1.getName().trim().length()>0)
			{
				dc.add(Restrictions.like("name", dep1.getName(), MatchMode.ANYWHERE));			
			}
			if(dep1.getTele()!=null &&  dep1.getTele().trim().length()>0)
			{
				dc.add(Restrictions.like("tele", dep1.getTele(), MatchMode.ANYWHERE));			
			}
		
		}		
		return dc;
	}
	
	
}

