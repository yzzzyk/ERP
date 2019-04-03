package com.itcast.erp.action;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.itcast.erp.biz.IRoleBiz;
import com.itcast.erp.entity.Role;
import com.itcast.erp.entity.Tree;

/**
 * 角色Action
 * 
 * @author Administrator
 *
 */
public class RoleAction extends BaseAction<Role> {

	private IRoleBiz roleBiz;
	private String checkedStr;

	public String getCheckedStr() {
		return checkedStr;
	}

	public void setCheckedStr(String checkedStr) {
		this.checkedStr = checkedStr;
	}

	public void setRoleBiz(IRoleBiz roleBiz) {
		this.roleBiz = roleBiz;
		setBaseBiz(roleBiz);
	}

	public void readRoleMenus() {
		List<Tree> roleMenus = roleBiz.readRoleMenus(getId());
		
		write(JSON.toJSONString(roleMenus));
	}
	public void updateRoleMenus(){
		
		try {
			roleBiz.updateRoleMenus(getId(), checkedStr);
			ajaxReturn(true, "更新角色权限成功");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(true, "更新角色权限失败");
		}
	}
}
