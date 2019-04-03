package com.itcast.erp.action;
import com.itcast.erp.biz.IDepBiz;
import com.itcast.erp.entity.Dep;

/**
 * 部门Action 
 * @author Administrator
 *
 */
public class DepAction extends BaseAction<Dep> {

	private IDepBiz depBiz;
	
	public void setDepBiz(IDepBiz depBiz) {
		this.depBiz = depBiz;
		setBaseBiz(depBiz);
	}
	
	
	
}
