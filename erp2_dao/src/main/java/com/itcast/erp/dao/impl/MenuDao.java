package com.itcast.erp.dao.impl;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.itcast.erp.dao.IMenuDao;
import com.itcast.erp.entity.Menu;
/**
 * 菜单数据访问类
 * @author Administrator
 *
 */
public class MenuDao extends BaseDao<Menu> implements IMenuDao {

	
	/**
	 * 构建查询条件
	 * @param dep1
	 * @param dep2
	 * @param param
	 * @return
	 */
	public DetachedCriteria getDetachedCriteria(Menu menu1,Menu menu2,Object param){
		DetachedCriteria dc=DetachedCriteria.forClass(Menu.class);
		return dc;
	}

	@Override
	public List<Menu> getMenusByEmpuuid(Long empuuid) {
		
		String hql="select m from Emp e join  e.roles r join r.menus m where e.uuid=?";
		
		return (List<Menu>) getHibernateTemplate().find(hql, empuuid);
	}
	
	
}

