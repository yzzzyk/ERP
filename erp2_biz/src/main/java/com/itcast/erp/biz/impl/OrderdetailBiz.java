package com.itcast.erp.biz.impl;

import java.util.Date;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.itcast.erp.biz.IOrderdetailBiz;
import com.itcast.erp.dao.IOrderdetailDao;
import com.itcast.erp.dao.IOrdersDao;
import com.itcast.erp.dao.IStoreDao;
import com.itcast.erp.dao.IStoredetailDao;
import com.itcast.erp.dao.IStoreoperDao;
import com.itcast.erp.dao.ISupplierDao;
import com.itcast.erp.entity.Orderdetail;
import com.itcast.erp.entity.Orders;
import com.itcast.erp.entity.Storedetail;
import com.itcast.erp.entity.Storeoper;
import com.itcast.erp.entity.Supplier;
import com.itcast.erp.exception.ErpException;
import com.itcast.redsun.bos.ws.impl.IWaybillWs;

/**
 * 订单明细业务逻辑类
 * 
 * @author Administrator
 *
 */

public class OrderdetailBiz extends BaseBiz<Orderdetail> implements IOrderdetailBiz {

	private IOrderdetailDao orderdetailDao;
	private IStoredetailDao storedetailDao;
	private IStoreoperDao storeoperDao;
	private IWaybillWs waybillWs;
	private ISupplierDao supplierDao;

	
	
	public void setWaybillWs(IWaybillWs waybillWs) {
		this.waybillWs = waybillWs;
	}

	@RequiresPermissions("采购订单入库")
	public void doInstore(Long uuid, Long storeUuid, Long empid) {
		// =========第一步
		// 获得该订单明细的持久化对象
		Orderdetail orderdetail = orderdetailDao.get(uuid);
		// 修改订单明细状态 为已入库
		if (!orderdetail.getState().equals(Orderdetail.STATE__NOT_IN)) {// 不是未入库状态，不能重复入库
			throw new ErpException("该商品已入库");
		}
		orderdetail.setState(Orderdetail.STATE_IN);
		// 记录操作人
		orderdetail.setEnder(empid);
		// 记录操作时间
		orderdetail.setEndtime(new Date());
		// 记录仓库
		orderdetail.setStoreuuid(storeUuid);

		// 第二步 更新库存信息

		// 拼装查询条件，查询出 对应仓库的对应商品库存信息
		Storedetail storedetail = new Storedetail();
		storedetail.setGoodsuuid(orderdetail.getGoodsuuid());
		storedetail.setStoreuuid(storeUuid);

		List<Storedetail> list = storedetailDao.getList(storedetail, null, null);
		long num=0;
		if (list != null && list.size() > 0) {// 存在该商品信息，更新
			if(list.get(0).getNum()!=null){
				 num =list.get(0).getNum().longValue();
			}
			list.get(0).setNum(num + orderdetail.getNum());
			
		} else {
			// 不存在，添加
			storedetail.setNum(orderdetail.getNum());
			storedetailDao.add(storedetail);
		}

		// 第三步 ：在增加商品仓库库存更新记录
		Storeoper operlog = new Storeoper();
		// 设置操作人
		operlog.setEmpuuid(empid);
		// 设置入库那个商品
		operlog.setGoodsuuid(orderdetail.getGoodsuuid());
		// 入库数量
		operlog.setNum(orderdetail.getNum());
		// 入库时间
		operlog.setOpertime(orderdetail.getEndtime());
		// 入在哪个仓库
		operlog.setStoreuuid(storeUuid);
		// 操作类型为 入库
		operlog.setType(Storeoper.TYPE_IN);

		storeoperDao.add(operlog);

		// 第四步 判断是否需要更新订单状态

		Orders orders = orderdetail.getOrders();
		//拼装条件 ： 该订单的所有未入库的订单详情
		Orderdetail countParam =new Orderdetail();
		countParam.setState(Orderdetail.STATE__NOT_IN);
		countParam.setOrders(orders);
		long count = orderdetailDao.getCount(countParam, null, null);
		if(count==0){//没有未入库的订单详细
			orders.setState(Orders.STATE_END);
			orders.setEndtime(orderdetail.getEndtime());
			orders.setEnder(empid);
		}
	}
	
	@RequiresPermissions("销售订单出库")
	public void doOutstore(Long uuid, Long storeUuid, Long empid) {
		// =========第一步
		// 获得该订单明细的持久化对象
		Orderdetail orderdetail = orderdetailDao.get(uuid);
		// 修改订单明细状态 为已出库
		if (!orderdetail.getState().equals(Orderdetail.STATE__NOT_OUT)) {// 不是未出库状态，不能重复出库
			throw new ErpException("该商品已出库");
		}
		orderdetail.setState(Orderdetail.STATE__OUT);
		// 记录操作人
		orderdetail.setEnder(empid);
		// 记录操作时间
		orderdetail.setEndtime(new Date());
		// 记录仓库
		orderdetail.setStoreuuid(storeUuid);
		
		// 第二步 更新库存信息
		
		// 拼装查询条件，查询出 对应仓库的对应商品库存信息
		Storedetail storedetail = new Storedetail();
		storedetail.setGoodsuuid(orderdetail.getGoodsuuid());
		storedetail.setStoreuuid(storeUuid);
		
		List<Storedetail> list = storedetailDao.getList(storedetail, null, null);
		long num=-1;
		if (list != null && list.size() > 0) {// 存在该商品信息
			if(list.get(0).getNum()!=null){
				num =list.get(0).getNum().longValue();//获得库存
			}	
			
			if(num -orderdetail.getNum()<0){//库存不够，
				throw new ErpException("库存不足，剩余"+num);
			}
			else {
				//库存足够，更新库存
				list.get(0).setNum(num -orderdetail.getNum());
			}
			
		} else{
			throw new ErpException("该仓库不存在该商品");
		}
		
		
		
		
		// 第三步 ：在增加商品仓库库存更新记录
		Storeoper operlog = new Storeoper();
		// 设置操作人
		operlog.setEmpuuid(empid);
		// 设置出库那个商品
		operlog.setGoodsuuid(orderdetail.getGoodsuuid());
		// 出数量
		operlog.setNum(orderdetail.getNum());
		// 出库时间
		operlog.setOpertime(orderdetail.getEndtime());
		// 出自哪个仓库
		operlog.setStoreuuid(storeUuid);
		// 操作类型为出库
		operlog.setType(Storeoper.TYPE_OUT);
		
		storeoperDao.add(operlog);
		
		// 第四步 判断是否需要更新订单状态
		
		Orders orders = orderdetail.getOrders();
		//拼装条件 ： 该订单的所有未入库的订单详情
		Orderdetail countParam =new Orderdetail();
		countParam.setState(Orderdetail.STATE__NOT_OUT);
		countParam.setOrders(orders);
		long count = orderdetailDao.getCount(countParam, null, null);
		if(count==0){//没有未出库的订单详细
			orders.setState(Orders.STATE_OUT);
			orders.setEndtime(orderdetail.getEndtime());
			orders.setEnder(empid);
			
			//如果所有订单明细已出库，自动创建物流订单，并更新订单 运单号
			Supplier supplier = supplierDao.get(orders.getSupplieruuid());
			Long waybillsn= waybillWs.addWaybill((long) 1, supplier.getAddress(), supplier.getContact(),supplier.getTele(),"--");

			//更新订单号,
			orders.setWaybillsn(waybillsn);
		}
	}
	
	
	

	public void setOrderdetailDao(IOrderdetailDao orderdetailDao) {
		this.orderdetailDao = orderdetailDao;
		setBaseDao(orderdetailDao);
	}

	public void setStoredetailDao(IStoredetailDao storedetailDao) {
		this.storedetailDao = storedetailDao;
	}

	public void setStoreoperDao(IStoreoperDao storeoperDao) {
		this.storeoperDao = storeoperDao;
	}


	public void setSupplierDao(ISupplierDao supplierDao) {
		this.supplierDao = supplierDao;
	}
	
}
