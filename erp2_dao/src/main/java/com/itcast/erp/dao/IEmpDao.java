package com.itcast.erp.dao;

import com.itcast.erp.entity.Emp;
/**
 * 员工数据访问接口
 * @author Administrator
 *
 */
public interface IEmpDao extends IBaseDao<Emp>{
	
	/** 
	 * 根据用户名和密码查询用户
	 * @param username
	 * @param password
	 * @return
	 */
	Emp findByUsernameAndPassword(String username,String password);
	
	/**
	 * 根据id修改密码
	 * @param uuid 
	 * @param newPwd 新密码
	 */
	void updatePwd(Long uuid,String newPwd);
}
