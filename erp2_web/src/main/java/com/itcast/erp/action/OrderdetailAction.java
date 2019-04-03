package com.itcast.erp.action;

import com.itcast.erp.biz.IOrderdetailBiz;
import com.itcast.erp.entity.Emp;
import com.itcast.erp.entity.Orderdetail;
import com.itcast.erp.exception.ErpException;

/**
 * 订单明细Action
 * 
 * @author Administrator
 *
 */
public class OrderdetailAction extends BaseAction<Orderdetail> {

	private IOrderdetailBiz orderdetailBiz;
	private Long storeuuid;

	public void setOrderdetailBiz(IOrderdetailBiz orderdetailBiz) {
		this.orderdetailBiz = orderdetailBiz;
		setBaseBiz(orderdetailBiz);
	}

	//入库
	public void doInStore() {

		Emp loginedUser = getloginUser();

		if (loginedUser == null) {
			ajaxReturn(false, "您还没有登录，请先登录");
			return;
		}

		try {
			orderdetailBiz.doInstore(getId(), storeuuid, loginedUser.getUuid());
			ajaxReturn(true, "入库成功");
		} catch (ErpException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ajaxReturn(false, "入库失败,该商品已入库");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ajaxReturn(false, "入库失败");
		}

	}
	
	//出库
	public void doOutStore() {
		
		Emp loginedUser = getloginUser();
		
		if (loginedUser == null) {
			ajaxReturn(false, "您还没有登录，请先登录");
			return;
		}
		
		try {
			orderdetailBiz.doOutstore(getId(), storeuuid, loginedUser.getUuid());
			ajaxReturn(true, "出库成功");
		} catch (ErpException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ajaxReturn(false, "出库失败"+e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ajaxReturn(false, "出库失败，");
		}
		
	}

	public Long getStoreuuid() {
		return storeuuid;
	}

	public void setStoreuuid(Long storeuuid) {
		this.storeuuid = storeuuid;
	}
	
}
