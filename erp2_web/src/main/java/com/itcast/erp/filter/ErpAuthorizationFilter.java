package com.itcast.erp.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

/**
 * 自定义授权过滤器
 * @author Administrator
 *
 */
public class ErpAuthorizationFilter extends AuthorizationFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		
		//获得主题
		Subject subject = getSubject(request, response);
		//mappedValue  ["我的采购订单","采购订单录入","采购订单审核","采购订单查询","采购订单确认","采购订单入库","销售订单查询","销售订单录入","销售订单出库"]
		String[] perms = (String[]) mappedValue;
		
		//若果为空或长度为零，直接放行
		if(perms==null||perms.length==0){	
			return true;
		}
		//权限检查
		for (String string : perms) {
			//只要有一个符合就放行
			if(subject.isPermitted(string))	
				return true;
		}
        return false;
	}

}
