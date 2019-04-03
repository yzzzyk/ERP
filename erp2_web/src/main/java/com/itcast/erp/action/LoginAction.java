package com.itcast.erp.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.itcast.erp.biz.IEmpBiz;
import com.itcast.erp.biz.impl.EmpBiz;
import com.itcast.erp.entity.Emp;
import com.opensymphony.xwork2.ActionContext;

public class LoginAction {

	private String username;
	private String password;
	

	/**
	 * 登录校验
	 */
	public void checkUser() {
		try {
			/*Emp emp = empBiz.findByUsernameAndPassword(username, password);
			if (emp != null) {// 登录成功

				// 将用户放入session
				ActionContext.getContext().getSession().put("user", emp);
				write(ajaxReturn(true, "登录成功"));

			} else {
				write(ajaxReturn(false, "用户名或者密码错误"));
			}*/		
			
			//创建令牌,身份证明
			UsernamePasswordToken upt=new UsernamePasswordToken(username,password);
			//获得主题subject：封装当前用户的一些操作
			Subject subject = SecurityUtils.getSubject();
			//执行login方法
			subject.login(upt);
			write(ajaxReturn(true, "登录成功"));
			
			} catch (Exception e) {
			e.printStackTrace();
			write(ajaxReturn(false, "登录失败"));
		}

	}

	/**
	 * 获得登录用户的用户名
	 */
	public void getName() {
		
		//Emp user = (Emp) ActionContext.getContext().getSession().get("user");
		
		//获得主题
		Subject subject = SecurityUtils.getSubject();
		//获得主角
		Emp user = (Emp) subject.getPrincipal();
		
		if (user != null)
			write(ajaxReturn(true, user.getName()));
		else
			write(ajaxReturn(false, "您还未登录，请登录"));

	}

	/**
	 * 退出登录
	 */
	public void logout() {
		SecurityUtils.getSubject().logout();
	}

	public void write(String jsonString) {

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		try {
			response.getWriter().print(jsonString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 标准结构返回体
	 * 
	 * @param success
	 * @param message
	 * @return
	 */
	public String ajaxReturn(boolean success, String message) {

		Map map = new HashMap();
		map.put("success", success);
		map.put("message", message);
		return JSON.toJSONString(map);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	

}
