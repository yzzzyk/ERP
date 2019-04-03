package com.itcast.erp.biz;
import java.util.List;

import com.itcast.erp.entity.Role;
/**
 * 角色业务逻辑层接口
 * @author Administrator
 *
 */
import com.itcast.erp.entity.Tree;
public interface IRoleBiz extends IBaseBiz<Role>{
	
	
	/**
	 * 角色权限菜单
	 * @return
	 */
	List<Tree> readRoleMenus(Long uuid);
	
	
	/**
	 *  更新角色权限菜单
	 * @param uuid 角色id
	 * @param checkedStr 被选中的权限菜单
	 */
	void updateRoleMenus(Long uuid,String checkedStr);
}

