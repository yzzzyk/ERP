package com.itcast.erp.biz.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.crypto.hash.Md5Hash;

import com.itcast.erp.biz.IEmpBiz;
import com.itcast.erp.dao.IEmpDao;
import com.itcast.erp.dao.IRoleDao;
import com.itcast.erp.entity.Emp;
import com.itcast.erp.entity.Role;
import com.itcast.erp.entity.Tree;
import com.itcast.erp.exception.ErpException;

import redis.clients.jedis.Jedis;

/**
 * 员工业务逻辑类
 * 
 * @author Administrator
 *
 */
public class EmpBiz extends BaseBiz<Emp> implements IEmpBiz {

	private IEmpDao empDao;
	private IRoleDao roleDao;
	private int hashIterations = 2;
	private Jedis jedis;

	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
		setBaseDao(empDao);
	}

	@Override
	public Emp findByUsernameAndPassword(String username, String password) {

		String newpwd = myMd5(password, username, hashIterations);
		return empDao.findByUsernameAndPassword(username, newpwd);
	}

	private String myMd5(String oldPwd, String salt, int hashIterations) {

		// source 需要加密的字段
		// salt 盐=》扰乱码
		// hashIterations 散列(加密)次数
		Md5Hash md5 = new Md5Hash(oldPwd, salt, hashIterations);
		return md5.toString();
	}

	// 复写 添加方法，加密(并设置默认密码，登录名就是登陆密码)
	public void add(Emp emp) {

		String newpwd = myMd5(emp.getUsername(), emp.getUsername(), hashIterations);

		emp.setPwd(newpwd);
		super.add(emp);

	}

	@Override
	public void updatePwd(Long uuid, String oldPwd, String newPwd) {
		// 取出登录员工的信息
		Emp emp = get(uuid);
		// 加密用户输入的未加密旧密码
		String oldPwdMd5 = myMd5(oldPwd, emp.getUsername(), hashIterations);
		// 判断旧密码是否相同
		if (!oldPwdMd5.equals(emp.getPwd())) {
			throw new ErpException("旧密码不正确");
		}
		String newPwdMd5 = myMd5(newPwd, emp.getUsername(), hashIterations);
		empDao.updatePwd(uuid, newPwdMd5);
	}

	@Override
	public void updatePwd_Reset(Long uuid, String newPwd) {

		// 取出登录员工的信息
		Emp emp = get(uuid);
		// 加密新密码
		String newPwdMd5 = myMd5(newPwd, emp.getUsername(), hashIterations);

		empDao.updatePwd(uuid, newPwdMd5);

	}

	@Override
	public List<Tree> readEmpRoles(Long empid) {

		List<Tree> list = new ArrayList<>();
		// 获得所有的 角色列表
		List<Role> allRole = roleDao.getList(null, null, null);

		// 获得用户的角色列表
		Emp emp = empDao.get(empid);
		List<Role> roles = emp.getRoles();

		// 创建菜单树
		Tree t = null;

		for (Role role : allRole) {
			t = new Tree();
			t.setId(role.getUuid() + "");
			t.setText(role.getName());
			if (roles.contains(role)) {
				t.setChecked(true);
			}
			list.add(t);
		}

		return list;
	}

	public void setRoleDao(IRoleDao roleDao) {
		this.roleDao = roleDao;
	}

	@Override
	public void updateEmpRoles(Long empuuid, String chechedRole) {

		Emp emp = empDao.get(empuuid);

		// 清空用户的所有角色
		emp.setRoles(new ArrayList<Role>());
		// 获得所有角色列表
		List<Role> allRole = roleDao.getList(null, null, null);

		Role role = null;
		for (String checked : chechedRole.split(",")) {
			// 通过id获得 角色
			role = roleDao.get(Long.valueOf(checked));
			emp.getRoles().add(role);
		}
		//每次更新用户角色后清除缓存的 用户拥有的菜单信息
		try {
			jedis.del("menuList_"+empuuid);
		} catch (Exception e) {
			throw new ErpException("每次更新用户角色后清除缓存的 用户拥有的菜单信息 shibai");
 		}
	}

	public void setJedis(Jedis jedis) {
		this.jedis = jedis;
	}
	

}
