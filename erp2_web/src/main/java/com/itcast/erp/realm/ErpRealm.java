package com.itcast.erp.realm;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.itcast.erp.biz.IEmpBiz;
import com.itcast.erp.biz.IMenuBiz;
import com.itcast.erp.entity.Emp;
import com.itcast.erp.entity.Menu;

public class ErpRealm extends AuthorizingRealm {
	
	private IEmpBiz empBiz;
	private IMenuBiz menuBiz;
	
	/* *
	 * 授权方法
	 */
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection Principals) {
		System.out.println("授权方法");
		SimpleAuthorizationInfo info =new SimpleAuthorizationInfo();
		
		//获得主角
		Emp emp = (Emp) Principals.getPrimaryPrincipal();
		
		List<Menu> menuList = menuBiz.getMenusByEmpuuid(emp.getUuid());
		for (Menu menu : menuList) {
			
			//添加授权
			info.addStringPermission(menu.getMenuname());
		}
		
		
		return info;
	}
	/**
	 *认证方法
	 *@return null 认证失败  AuthenticationInfo 实现类 认证成功
	 */
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		
		System.out.println("认证方法");
		UsernamePasswordToken  upt=(UsernamePasswordToken) token;
		 String pwd=new String(upt.getPassword());
		//调用登录查询
		Emp emp = empBiz.findByUsernameAndPassword(upt.getUsername(),pwd);
		
		if(null!=emp){
			//构造函数 1 主角 =登录用户 2  授权码  3realm 名 
			SimpleAuthenticationInfo info =new SimpleAuthenticationInfo(emp, pwd, getName());
			return info;
		}
		return null;
	}
	public void setEmpBiz(IEmpBiz empBiz) {
		this.empBiz = empBiz;
	}
	public void setMenuBiz(IMenuBiz menuBiz) {
		this.menuBiz = menuBiz;
	}
	
}
