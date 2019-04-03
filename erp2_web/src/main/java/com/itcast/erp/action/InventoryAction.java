package com.itcast.erp.action;
import com.itcast.erp.biz.IInventoryBiz;
import com.itcast.erp.entity.Inventory;

/**
 * 盘盈盘亏Action 
 * @author Administrator
 *
 */
public class InventoryAction extends BaseAction<Inventory> {

	private IInventoryBiz inventoryBiz;
	
	public void setInventoryBiz(IInventoryBiz inventoryBiz) {
		this.inventoryBiz = inventoryBiz;
		setBaseBiz(inventoryBiz);
	}
	
	
}
