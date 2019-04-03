package com.itcast.erp.biz.impl;

import java.util.Date;
import java.util.List;

import com.itcast.erp.biz.IReturnorderdetailBiz;
import com.itcast.erp.dao.IReturnorderdetailDao;
import com.itcast.erp.dao.IStoredetailDao;
import com.itcast.erp.dao.IStoreoperDao;
import com.itcast.erp.dao.ISupplierDao;
import com.itcast.erp.dao.impl.SupplierDao;
import com.itcast.erp.entity.Returnorderdetail;
import com.itcast.erp.entity.Returnorders;
import com.itcast.erp.entity.Storedetail;
import com.itcast.erp.entity.Storeoper;
import com.itcast.erp.entity.Supplier;
import com.itcast.erp.exception.ErpException;
import com.itcast.redsun.bos.ws.impl.IWaybillWs;

/**
 * 退货订单明细业务逻辑类
 * 
 * @author Administrator
 *
 */
public class ReturnorderdetailBiz extends BaseBiz<Returnorderdetail> implements IReturnorderdetailBiz {

	private IReturnorderdetailDao returnorderdetailDao;
	private IStoredetailDao storedetailDao;
	private IStoreoperDao storeoperDao;
	private ISupplierDao supplierDao;
	private IWaybillWs waybillWs;

	public void setReturnorderdetailDao(IReturnorderdetailDao returnorderdetailDao) {
		this.returnorderdetailDao = returnorderdetailDao;
		setBaseDao(returnorderdetailDao);
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

	public void setWaybillWs(IWaybillWs waybillWs) {
		this.waybillWs = waybillWs;
	}

	@Override
	public void doReturnOutStore(Long uuid, Long storeuuid, Long empid) {

		// =========第一步
		// 获得该订单明细
		Returnorderdetail returnorderdetail = returnorderdetailDao.get(uuid);

		if (!returnorderdetail.getState().equals(Returnorderdetail.STATE__NOT_OUT)) {
			throw new ErpException("该条记录已出库");
		}
		
		// 设置该订单明细的 状态为已出库
		returnorderdetail.setState(Returnorderdetail.STATE__OUT);
		// 记录仓库
		returnorderdetail.setStoreuuid(storeuuid);
		// 记录时间
		returnorderdetail.setEndtime(new Date());
		// 记录操作人
		returnorderdetail.setEnder(empid);

		// 第二步 更新库存

		// 拼装查询条件 仓库编号为storeuuid 且放的为 对应商品的仓库明细
		Storedetail storedetail = new Storedetail();
		storedetail.setStoreuuid(storeuuid);
		storedetail.setGoodsuuid(returnorderdetail.getGoodsuuid());

		List<Storedetail> list = storedetailDao.getList(storedetail, null, null);
		long num = -1;
		if (list != null && list.size() > 0) {// 仓库存在该商品
			// 再次判断,并获得库存
			if (list.get(0).getNum() != null) {
				num = list.get(0).getNum();
			}
			if (num - returnorderdetail.getNum() < 0) {
				throw new ErpException("库存不足，剩余：" + num);
			} else
				// 更新库存
				list.get(0).setNum(num - returnorderdetail.getNum());

		} else {
			throw new ErpException("该仓库不存在该商品");
		}
		
		//第三步 增加商品仓库库存变更记录
		Storeoper storeoper = new Storeoper();
		storeoper.setEmpuuid(empid);
		storeoper.setGoodsuuid(returnorderdetail.getGoodsuuid());
		storeoper.setNum(returnorderdetail.getNum());
		storeoper.setOpertime(returnorderdetail.getEndtime());
		storeoper.setStoreuuid(returnorderdetail.getStoreuuid());
		storeoper.setType(Storeoper.TYPE_OUT);
		//将库存变更记录对象转为持久化对象
		storeoperDao.add(storeoper);
		
		//第四部，判断是否全部出库
		
		//拼装条件  得到 该订单的所有未出库订单明细
		Returnorders returnorders = returnorderdetail.getReturnorders();
		Returnorderdetail countParam = new Returnorderdetail();
		
		countParam.setReturnorders(returnorders);
		countParam.setState(Returnorderdetail.STATE__NOT_OUT);

		
		//属于该退货订单的且
		long count = returnorderdetailDao.getCount(countParam, null, null);
		//没有查到
		
		if(count==0){
			//更改订单的状态
			returnorders.setState(Returnorders.STATE_IN_OUT);
			returnorders.setEnder(empid);
			returnorders.setEndtime(returnorderdetail.getEndtime());
			
			//订单全部为出库,自动创建订单
			//System.out.println(returnorders.getSupplieruuid());
			Supplier supplier = supplierDao.get(returnorders.getSupplieruuid());
			Long addWaybill = waybillWs.addWaybill(1l, supplier.getAddress(), supplier.getContact(), supplier.getTele(), "--");
			
			
			returnorders.setWaybillsn(addWaybill);
			
			
			
		}
	}

}

