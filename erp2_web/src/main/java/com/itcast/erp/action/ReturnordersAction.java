package com.itcast.erp.action;
import com.itcast.erp.biz.IReturnordersBiz;
import com.itcast.erp.entity.Emp;
import com.itcast.erp.entity.Returnorders;
import com.itcast.erp.exception.ErpException;

/**
 * 退货订单Action 
 * @author Administrator
 *
 */
public class ReturnordersAction extends BaseAction<Returnorders> {
	
	
	private IReturnordersBiz returnordersBiz;
	
	public void setReturnordersBiz(IReturnordersBiz returnordersBiz) {
		this.returnordersBiz = returnordersBiz;
		setBaseBiz(returnordersBiz);
	}
	
	public void add(){
		Emp user = getloginUser();
		if(user==null){
			ajaxReturn(false, "您還没有登录，请先登录");
			return;
		}
		
		Returnorders returnorders = getT();
		returnorders.setCreater(user.getUuid());
		
		try {
			returnordersBiz.add(returnorders);
			ajaxReturn(true, "退货登记成功");
		} catch (ErpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ajaxReturn(false, "退货登记失败,"+e.getMessage());
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ajaxReturn(false, "退货登记失败");
		}
	}
	
	public void doReturnCheck(){
		
		Emp emp=getloginUser();
		if(emp==null){
			ajaxReturn(false, "您还未登录，请先登录");
		}
		try {
			returnordersBiz.doReturnCheck(getId(), emp.getUuid());
			ajaxReturn(true, "审核成功");
		} 
		catch (ErpException e) {
			ajaxReturn(false, e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "审核失败");
		}
	
	}
	
	
}
