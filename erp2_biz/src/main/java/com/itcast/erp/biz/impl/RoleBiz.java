package com.itcast.erp.biz.impl;

import java.util.ArrayList;
import java.util.List;

import com.itcast.erp.biz.IRoleBiz;
import com.itcast.erp.dao.IMenuDao;
import com.itcast.erp.dao.IRoleDao;
import com.itcast.erp.entity.Emp;
import com.itcast.erp.entity.Menu;
import com.itcast.erp.entity.Role;
import com.itcast.erp.entity.Tree;
import com.itcast.erp.exception.ErpException;

import redis.clients.jedis.Jedis;

/**
 * 角色业务逻辑类
 * 
 * @author Administrator
 *
 */
public class RoleBiz extends BaseBiz<Role> implements IRoleBiz {

	private IRoleDao roleDao;
	private IMenuDao menuDao;
	private Jedis jedis;

	public void setRoleDao(IRoleDao roleDao) {
		this.roleDao = roleDao;
		setBaseDao(roleDao);
	}

	public void setMenuDao(IMenuDao menuDao) {
		this.menuDao = menuDao;
	}

	@Override
	public List<Tree> readRoleMenus(Long uuid) {

		List<Tree> treeList = new ArrayList<>();
		// 获得根目录
		Menu root = menuDao.get("0");

		// 获得角色对应的权限列表
		List<Menu> menus = roleDao.get(uuid).getMenus();

		// 構建树结构
		Tree t1 = null;
		Tree t2 = null;
		for (Menu m1 : root.getMenus()) {// 获得所有的一级菜单

			t1 = new Tree();// 一级菜单树

			t1.setId(m1.getMenuid());
			t1.setText(m1.getMenuname());

			for (Menu m2 : m1.getMenus()) {

				t2 = new Tree();// 二级菜单树

				t2.setText(m2.getMenuname());
				t2.setId(m2.getMenuid());

				if (menus.contains(m2)) {// 角色权限菜单中包含该二级菜单
					t2.setChecked(true);
				}

				t1.getChildren().add(t2);// 将二级菜单树添加到一级菜单树中
			}

			treeList.add(t1);
		}
		return treeList;
	}

	@Override
	public void updateRoleMenus(Long uuid, String checkedStr) {

		// 清空该角色的权限菜单
		Role role = roleDao.get(uuid);
		role.setMenus(new ArrayList<Menu>());

		for (String checkedid : checkedStr.split(",")) {
			// 获得新角色权限菜单并添加
			role.getMenus().add(menuDao.get(checkedid));
		}

		try {
			// 得到拥有该角色 的所有用户
			List<Emp> list = role.getEmps();
			for (Emp emp : list) {
				//清空拥有该角色的用户的 缓存菜单
				jedis.del("menuList_" + emp.getUuid());
			}
		} catch (Exception e) {
			throw new ErpException("清空拥有该角色的用户的缓存菜单失败");
		}

	}

	public void setJedis(Jedis jedis) {
		this.jedis = jedis;
	}

}
