package com.itcast.erp.biz;
import java.util.List;

import com.itcast.erp.entity.Menu;
/**
 * 菜单业务逻辑层接口
 * @author Administrator
 *
 */
public interface IMenuBiz extends IBaseBiz<Menu>{
	
	/**
	 * 根据员工编号获得(二级)菜单 列表
	 * @param empuuid
	 * @return 
	 */
	List<Menu> getMenusByEmpuuid(Long empuuid);
	
	/**
	 * 根据员工编号获得对应(Menu型)菜单
	 * @param empuuid
	 * @return 
	 */
	Menu readMenusByEmpuuid(Long empuuid);
}

