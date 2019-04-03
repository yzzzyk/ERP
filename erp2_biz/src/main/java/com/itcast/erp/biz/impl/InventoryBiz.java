package com.itcast.erp.biz.impl;
import com.itcast.erp.biz.IInventoryBiz;
import com.itcast.erp.dao.IInventoryDao;
import com.itcast.erp.entity.Inventory;
/**
 * 盘盈盘亏业务逻辑类
 * @author Administrator
 *
 */
public class InventoryBiz extends BaseBiz<Inventory> implements IInventoryBiz {

	private IInventoryDao inventoryDao;
	
	public void setInventoryDao(IInventoryDao inventoryDao) {
		this.inventoryDao = inventoryDao;
		setBaseDao(inventoryDao);
	}

	
}
