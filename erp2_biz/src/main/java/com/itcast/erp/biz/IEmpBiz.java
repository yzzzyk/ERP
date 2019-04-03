package com.itcast.erp.biz;
import java.util.List;

import com.itcast.erp.entity.Emp;
import com.itcast.erp.entity.Tree;
/**
 * 员工业务逻辑层接口
 * @author Administrator
 *
 */
public interface IEmpBiz extends IBaseBiz<Emp>{
	
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
	 * @param oldPwd 旧密码
	 * @param newPwd 新密码
	 */
	void updatePwd(Long uuid,String oldPwd,String newPwd);
	
	/**
	 * 重置密码
	 * @param uuid
	 * @param newPwd
	 */
	void updatePwd_Reset(Long uuid,String newPwd);
	
	
	/**
	 * 读取用户角色（为菜单树格式）
	 * @param empid  用户id
	 * @return 用户角色列表，菜单树
	 */
	List<Tree> readEmpRoles(Long empid);
	
	
	/**
	 * 更新用户的角色
	 * @param empuuid 用户id 
	 * @param chechedRole 前台传回来的选中的角色
	 */
	void updateEmpRoles(Long empuuid,String chechedRole);
}

