package com.itcast.erp.biz.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.itcast.erp.biz.IReturnordersBiz;
import com.itcast.erp.dao.IOrdersDao;
import com.itcast.erp.dao.IReturnordersDao;
import com.itcast.erp.entity.Orderdetail;
import com.itcast.erp.entity.Orders;
import com.itcast.erp.entity.Returnorderdetail;
import com.itcast.erp.entity.Returnorders;
import com.itcast.erp.exception.ErpException;

/**
 * 退货订单业务逻辑类
 * 
 * @author Administrator
 *
 */
public class ReturnordersBiz extends BaseBiz<Returnorders> implements IReturnordersBiz {

	private IReturnordersDao returnordersDao;
	private IOrdersDao ordersDao;

	public void setReturnordersDao(IReturnordersDao returnordersDao) {
		this.returnordersDao = returnordersDao;
		setBaseDao(returnordersDao);
	}

	public void setOrdersDao(IOrdersDao ordersDao) {
		this.ordersDao = ordersDao;
	}

	@Override
	public void add(Returnorders returnorders) {

		// 前端会传来原订单的 UUID,根据原订单id，查出原订单
		Orders orders = ordersDao.get(returnorders.getOrdersuuid());
		if (orders == null) {
			throw new ErpException("订单号无效");
		}

		// 判断该订单是否已经申请过退货
		if (Orders.STATE_RETURN.equals(orders.getState())) {
			throw new ErpException("该订单已被申请退货");
		}
		
		if (!orders.getState().equals(Orders.STATE_END)) {

			throw new ErpException("该订单未完成，不能退货");
		}
		// 将原订单状态设置为退货状态
		orders.setState("9");

		// 获得主题
		Subject subject = SecurityUtils.getSubject();

		if (Orders.TYPR_IN.equals(orders.getType())) {// 原订单为进货订单，设置退货订单类型为,采购退货

			if (!subject.isPermitted("采购退货登记")) {
				throw new ErpException("您没有采购退货登记权限");
			}

			returnorders.setType(Returnorders.TYPE_RETURN_IN);

		} else if (Orders.TYPR_OUT.equals(orders.getType())) {// 原订单为销售订单，销售退货

			if (!subject.isPermitted("销售退货登记")) {
				throw new ErpException("您没有销售退货登记权限");
			}

			returnorders.setType(Returnorders.TYPE_RETURN_OUT);
		} else {
			throw new ErpException("参数违规");
		}

		// 默认状态为未登记
		returnorders.setState(Returnorders.STATE__UNCHECK);
		returnorders.setCreatetime(new Date());
		returnorders.setSupplieruuid(orders.getSupplieruuid());
		List<Orderdetail> list = orders.getOrderdetail();

		Returnorderdetail returnorderdetail = null;
		if (returnorders.getReturnorderdetailList() == null) {
			returnorders.setReturnorderdetailList(new ArrayList<Returnorderdetail>());
		}
		for (Orderdetail orderdetail : list) {

			returnorderdetail = new Returnorderdetail();
			
			returnorderdetail.setState(Returnorderdetail.STATE__NOT_OUT);
			returnorderdetail.setGoodsuuid(orderdetail.getGoodsuuid());
			returnorderdetail.setGoodsname(orderdetail.getGoodsname());
			returnorderdetail.setPrice(orderdetail.getPrice());
			returnorderdetail.setNum(orderdetail.getNum());
			returnorderdetail.setMoney(orderdetail.getMoney());
			returnorderdetail.setReturnorders(returnorders);
			
			// 将该订单明细添加到订单
			returnorders.getReturnorderdetailList().add(returnorderdetail);
		}
		returnorders.setTotalmoney(orders.getTotalmoney());

		super.add(returnorders);
	}

	@Override
	public void doReturnCheck(Long uuid, Long checkeruuid) {
		// 获得该退货订单
		Returnorders returnorders = returnordersDao.get(uuid);

		// 判断该退货订单的状态为 未審核
		if (!returnorders.getState().equals(Returnorders.STATE__UNCHECK)) {
			throw new ErpException("该订单不满足采购退货审核条件");
		}
		// 设置审核人
		returnorders.setChecker(checkeruuid);
		// 设置审核日期
		returnorders.setChecktime(new Date());
		// 修改订单状态 为 已审核
		returnorders.setState(Returnorders.STATE_CHECKED);
	}

	
	
}
