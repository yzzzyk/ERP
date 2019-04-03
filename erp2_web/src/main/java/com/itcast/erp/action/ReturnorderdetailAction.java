package com.itcast.erp.action;
import com.itcast.erp.biz.IReturnorderdetailBiz;
import com.itcast.erp.entity.Emp;
import com.itcast.erp.entity.Returnorderdetail;
import com.itcast.erp.exception.ErpException;

/**
 * 退货订单明细Action 
 * @author Administrator
 *
 */
public class ReturnorderdetailAction extends BaseAction<Returnorderdetail> {

	private IReturnorderdetailBiz returnorderdetailBiz;
	private Long storeuuid;
	
	
	/**
	 * 退貨订单明细出库
	 * @param uuid
	 * @param storeuuid
	 * @param empid
	 */
	public void doReturnOutStore(){ 
		
		Emp emp=getloginUser();
		if(emp==null){
			throw new ErpException("请先登录");
		}
		try {
			returnorderdetailBiz.doReturnOutStore(getId(), storeuuid, emp.getUuid());
			ajaxReturn(true, "购买订单退货出库成功");
		} 
		catch (ErpException e) {
			ajaxReturn(false, e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "购买订单退货出库失败");
		}
	}

	public Long getStoreuuid() {
		return storeuuid;
	}

	public void setStoreuuid(Long storeuuid) {
		this.storeuuid = storeuuid;
	}
	
	public void setReturnorderdetailBiz(IReturnorderdetailBiz returnorderdetailBiz) {
		this.returnorderdetailBiz = returnorderdetailBiz;
		setBaseBiz(returnorderdetailBiz);
	}
	
}
