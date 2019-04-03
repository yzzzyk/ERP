package com.itcast.erp.dao.impl;

import java.util.List;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import com.itcast.erp.dao.IEmpDao;
import com.itcast.erp.entity.Emp;

/**
 * 员工数据访问类
 * 
 * @author Administrator
 *
 */
public class EmpDao extends BaseDao<Emp> implements IEmpDao {

	

	/**
	 * 构建查询条件
	 * 
	 * @param dep1
	 * @param dep2
	 * @param param
	 * @return
	 */
	public DetachedCriteria getDetachedCriteria(Emp emp1, Emp emp2, Object param) {
		DetachedCriteria dc = DetachedCriteria.forClass(Emp.class);

		if (emp1 != null) {
			// 登录名
			if (emp1.getUsername() != null && emp1.getUsername().trim().length() > 0) {
				dc.add(Restrictions.like("username", emp1.getUsername(), MatchMode.ANYWHERE));
			} // 真实名
			if (emp1.getName() != null && emp1.getName().trim().length() > 0) {
				dc.add(Restrictions.like("name", emp1.getName(), MatchMode.ANYWHERE));
			}
			// email
			if (emp1.getEmail() != null && emp1.getEmail().trim().length() > 0) {
				dc.add(Restrictions.like("email", emp1.getEmail(), MatchMode.ANYWHERE));
			} // 电话
			if (emp1.getTele() != null && emp1.getTele().trim().length() > 0) {
				dc.add(Restrictions.like("tele", emp1.getTele(), MatchMode.ANYWHERE));
			} // 地址
			if (emp1.getAddress() != null && emp1.getAddress().trim().length() > 0) {
				dc.add(Restrictions.like("address", emp1.getAddress(), MatchMode.ANYWHERE));
			}
			// 性别
			if (emp1.getGender() != null) {
				dc.add(Restrictions.eq("gender", emp1.getGender()));
			}
			// 部门
			if (emp1.getDep() != null) {
				// 直接选的,具体查询
				if (emp1.getDep().getUuid() != null) {
					dc.add(Restrictions.eq("dep", emp1.getDep()));
				}
			}
			// 日期区间查询
			if (emp1.getBirthday() != null) {
				dc.add(Restrictions.ge("birthday", emp1.getBirthday()));
			}
		}

		//System.out.println(emp2);
		if (emp2 != null && null != emp2.getBirthday()) {
			dc.add(Restrictions.le("birthday", emp2.getBirthday()));
		}
		return dc;
	}

	@Override
	public Emp findByUsernameAndPassword(String username, String password) {
		String hql = "from Emp where username = ? and pwd = ?";
		List<Emp> list = (List<Emp>) getHibernateTemplate().find(hql, username, password);
		if (list != null && list.size() > 0) {

			return list.get(0);
		}
		return null;
	}

	
	@Override
	public void updatePwd(Long uuid, String newPwd) {
		getHibernateTemplate().bulkUpdate("update Emp set pwd=? where uuid=?",newPwd, uuid);
	}


	

}
