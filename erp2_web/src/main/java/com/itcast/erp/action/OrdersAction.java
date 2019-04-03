package com.itcast.erp.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.itcast.erp.biz.IOrdersBiz;
import com.itcast.erp.entity.Emp;
import com.itcast.erp.entity.Orderdetail;
import com.itcast.erp.entity.Orders;
import com.itcast.erp.exception.ErpException;
import com.itcast.redsun.bos.ws.Waybilldetail;

/**
 * 订单Action
 * 
 * @author Administrator
 *
 */
public class OrdersAction extends BaseAction<Orders> {

	private IOrdersBiz ordersBiz;

	// 接收订单明细里的每一个json格式的字符串 每一行的json字符串格式 name:' sds'
	private String json;

	private Long waybillsn;

	public Long getWaybillsn() {
		return waybillsn;
	}

	public void setWaybillsn(Long waybillsn) {
		this.waybillsn = waybillsn;
	}

	public void setOrdersBiz(IOrdersBiz ordersBiz) {
		this.ordersBiz = ordersBiz;
		setBaseBiz(ordersBiz);
	}

	// 添加订单
	public void add() {
		// 获得登录用户
		Emp loginedUser = getloginUser();

		if (loginedUser == null) {
			ajaxReturn(false, "您还没有登录，请先登录");
			return;
		}
		try {
			
			// 获得封装数据的orders对象，其中封装的有供应商
			Orders orders = getT();
			// 添加下单员
			orders.setCreater(loginedUser.getUuid());

			// 将订单明细字符串转换为 订单明细 对象
			List<Orderdetail> OrderdetaiList = JSON.parseArray(json, Orderdetail.class);

			// 添加订单明细
			orders.setOrderdetail(OrderdetaiList);

			ordersBiz.add(orders);
			ajaxReturn(true, "添加成功");
		} catch (Exception e) {

			e.printStackTrace();
			ajaxReturn(false, "添加失败");
		}
	}

	// 审核订单
	public void doCheck() {
		Emp loginedUser = getloginUser();

		if (loginedUser == null) {
			ajaxReturn(false, "您还没有登录，请先登录");
			return;
		}
		try {
			ordersBiz.doCheck(getId(), loginedUser.getUuid());
			ajaxReturn(true, "审核成功");
		} catch (ErpException e) {
			ajaxReturn(false, e.getMessage());

		} catch (UnauthorizedException u) {

			ajaxReturn(false, "审核失败,權限不足");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "审核失败");
		}
	}

	// 确认
	public void doStart() {
		Emp loginedUser = getloginUser();

		if (loginedUser == null) {
			ajaxReturn(false, "您还没有登录，请先登录");
			return;
		}
		try {
			ordersBiz.doStart(getId(), loginedUser.getUuid());
			ajaxReturn(true, "订单确认成功");
		} catch (ErpException e) {
			ajaxReturn(false, e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "订单确认失败");
		}
	}

	// 我的订单
	public void myListByPage() {
		Emp loginedUser = getloginUser();

		if (loginedUser == null) {
			ajaxReturn(false, "您还没有登录，请先登录");
			return;
		}

		if (getT1() == null) {
			setT1(new Orders());
		}
		getT1().setCreater(loginedUser.getUuid());
		super.listByPage();

	}

	public void exportById() {

		HttpServletResponse response = ServletActionContext.getResponse();

		try {
			response.setHeader("Content-Disposition", "attachment;filename=orders_" + getId() + ".xls");

			ordersBiz.exportById(response.getOutputStream(), getId());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void waybillDetailList() {

		List<Waybilldetail> waybilldetailList = ordersBiz.waybilldetailList(waybillsn);

		write(JSON.toJSONString(waybilldetailList));
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

}
