package com.itcast.erp.entity;

/**
 * 库存预警
 * @author Administrator
 *
 */
public class StoreAlert {
	private Long uuid;//商品编号
	private String name;//商品名称
	private Long storenum;//商品库存
	private Long outnum;//代发货数量
	public Long getUuid() {
		return uuid;
	}
	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getStorenum() {
		return storenum;
	}
	public void setStorenum(Long storenum) {
		this.storenum = storenum;
	}
	public Long getOutnum() {
		return outnum;
	}
	public void setOutnum(Long outnum) {
		this.outnum = outnum;
	}
	
}
