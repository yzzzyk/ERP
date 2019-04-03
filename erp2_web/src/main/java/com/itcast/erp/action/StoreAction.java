package com.itcast.erp.action;
import com.itcast.erp.biz.IStoreBiz;
import com.itcast.erp.entity.Emp;
import com.itcast.erp.entity.Store;

/**
 * 仓库Action 
 * @author Administrator
 *
 */
public class StoreAction extends BaseAction<Store> {

	private IStoreBiz storeBiz;
	
	public void setStoreBiz(IStoreBiz storeBiz) {
		this.storeBiz = storeBiz;
		setBaseBiz(storeBiz);
	}
	
	public void mylist(){
		Emp user = getloginUser();

		if (user == null) {
			ajaxReturn(false, "您还没有登录，请先登录");
			return;
		}
		if(getT1()==null){
			setT1(new Store());
		}
		
		getT1().setEmpuuid(user.getUuid());
		
		super.list();
	}
	
}
