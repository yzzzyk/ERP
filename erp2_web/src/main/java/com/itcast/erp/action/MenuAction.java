package com.itcast.erp.action;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.itcast.erp.biz.IMenuBiz;
import com.itcast.erp.entity.Menu;

/**
 * 菜单Action 
 * @author Administrator
 *
 */
public class MenuAction extends BaseAction<Menu> {

	private IMenuBiz menuBiz;
	
	public void setMenuBiz(IMenuBiz menuBiz) {
		this.menuBiz = menuBiz;
		setBaseBiz(menuBiz);
	}
	
	
	//获取所有菜单数据
	
	public void getMenuTree(){
		
		//获取主菜单，自关联会获取其下子菜单，子子菜单的数据
		Menu menu = menuBiz.get("0");
		
		write(JSON.toJSONString(menu));
		
	}
	
	//获取用户下的菜单列表
	public void getMenusByEmpuuid(){
		if(getloginUser()!=null){
			List<Menu> menusByEmpuuid = menuBiz.getMenusByEmpuuid(getloginUser().getUuid());
			write(JSON.toJSONString(menusByEmpuuid));
		}
		
	}
	//获取用户下的菜单Menu格式
	public void readgetMenusByEmpuuid(){
			Menu readMenusByEmpuuid = menuBiz.readMenusByEmpuuid(getloginUser().getUuid());
			write(JSON.toJSONString(readMenusByEmpuuid));
		}
}
