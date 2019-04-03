package com.itcast.erp.biz.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.itcast.erp.biz.IMenuBiz;
import com.itcast.erp.dao.IMenuDao;
import com.itcast.erp.entity.Menu;

import redis.clients.jedis.Jedis;

/**
 * 菜单业务逻辑类
 * 
 * @author Administrator
 *
 */
public class MenuBiz extends BaseBiz<Menu> implements IMenuBiz {

	private IMenuDao menuDao;
	private Jedis jedis;

	public void setMenuDao(IMenuDao menuDao) {
		this.menuDao = menuDao;
		setBaseDao(menuDao);
	}

	@Override
	public List<Menu> getMenusByEmpuuid(Long empuuid) {

		List<Menu> menuList = null;

		// 先尝试从缓存中取出
		String menuListJson = jedis.get("menuList_" + empuuid);
		
		if (menuListJson != null) {
			System.out.println("从缓存中取");
			// 将字符串转成对象
			menuList = JSON.parseArray(menuListJson, Menu.class);
		} else {//说明缓存中没有
			System.out.println("在数据库中拿");
			menuList = menuDao.getMenusByEmpuuid(empuuid);
			//将数据写入缓存
			jedis.set("menuList_"+empuuid, JSON.toJSONString(menuList));
		}
		return menuList;
	}

	@Override
	public Menu readMenusByEmpuuid(Long empuuid) {

		// 获得所有主菜单
		Menu menu = menuDao.get("0");
		// 获得子菜单
		List<Menu> menus = menu.getMenus();
		// 获得用户的菜单
		List<Menu> menusByEmpuuid = menuDao.getMenusByEmpuuid(empuuid);

		// 克隆0级根菜单
		Menu rtnMenu = cloneMenu(menu);
		// 克隆1,2级菜单
		Menu cloneMenu1 = null;
		Menu cloneMenu2 = null;
		for (Menu m1 : menus) {

			cloneMenu1 = cloneMenu(m1);
			// 遍历所有的二级菜单
			for (Menu m2 : m1.getMenus()) {
				if (menusByEmpuuid.contains(m2)) {
					cloneMenu2 = cloneMenu(m2);
					cloneMenu1.getMenus().add(cloneMenu2);
				}
			}
			// 如果有该二级菜单添加对应的一级菜单到根菜单
			if (cloneMenu1.getMenus().size() > 0)
				rtnMenu.getMenus().add(cloneMenu1);
		}
		return rtnMenu;
	}

	private Menu cloneMenu(Menu menu) {
		Menu _menu = new Menu();
		_menu.setIcon(menu.getIcon());
		_menu.setMenuid(menu.getMenuid());
		_menu.setMenuname(menu.getMenuname());
		_menu.setUrl(menu.getUrl());
		_menu.setMenus(new ArrayList<Menu>());
		return _menu;

	}

	public void setJedis(Jedis jedis) {
		this.jedis = jedis;
	}

}
