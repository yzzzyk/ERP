package com.itcast.erp.action;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.itcast.erp.biz.IEmpBiz;
import com.itcast.erp.entity.Emp;
import com.itcast.erp.entity.Tree;

/**
 * 员工Action
 * 
 * @author Administrator
 *
 */
public class EmpAction extends BaseAction<Emp> {

	private IEmpBiz empBiz;
	private String oldPwd;
	private String newPwd;
	
	//接收选中的角色拼接字符串
	private String chechedRole;

	public void setEmpBiz(IEmpBiz empBiz) {
		this.empBiz = empBiz;
		setBaseBiz(empBiz);
	}

	// 修改密码
	public void updatePwd() {
		Emp emp = getloginUser();

		if(emp==null){
			ajaxReturn(false, "登录超时，请重新登录");
			return ;
		}
		
		try {
			empBiz.updatePwd(emp.getUuid(),oldPwd, newPwd);
			ajaxReturn(true, "密码修改成功,请重新登录");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "密码修改失败,旧密码错误");
		}
		
	}
	
	
	//重置密码
	public void updatePwd_Reset(){
		
		try {
			empBiz.updatePwd_Reset(getId(), newPwd);
			ajaxReturn(true, "密码重置成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ajaxReturn(false, "密码重置失败");
		}
	}
	
	//获得树形格式 用户角色列表
	public void readEmpRoles(){
		
		List<Tree> empRoles = empBiz.readEmpRoles(getId());
		write(JSON.toJSONString(empRoles));

	}
	
	//更新用户 角色
	public void updateEmpRoles(){
		
		try {
			empBiz.updateEmpRoles(getId(), chechedRole);
			ajaxReturn(true, "更新用户角色成功");
		} catch (Exception e) {
			
			e.printStackTrace();
			ajaxReturn(true, "更新用户角色失败");
		}
	}
	
	public String getOldPwd() {
		return oldPwd;
	}

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

	public String getChechedRole() {
		return chechedRole;
	}

	public void setChechedRole(String chechedRole) {
		this.chechedRole = chechedRole;
	}
	
}
