package com.itcast.erp.entity;

import java.util.List;

/**
 * 退货订单实体类
 * @author Administrator *
 */
public class Returnorders {	
	private Long uuid;//编号
	private java.util.Date createtime;//生成日期
	private java.util.Date checktime;//检查日期
	private java.util.Date endtime;//结束日期
	
	/**
	 * 1  采購退货
	 * 2  销售退货
	 */
	private String type;//订单类型
	
	/**
	 * 1  采購退货
	 */
	public static final String TYPE_RETURN_IN="1";
	/**
	 *2  销售退货
	 */
	public static final String TYPE_RETURN_OUT="2";
	
	private Long creater;//下单员
	private Long checker;//审核员工编号
	private Long ender;//库管员
	private Long supplieruuid;//供应商及客户编号
	private Double totalmoney;//合计金额
	private String state;//订单状态
	
	/**
	 * 0=未审核
	 */
	public static final String STATE__UNCHECK="0";
	/**
	 * 1=已审核
	 */
	public static final String STATE_CHECKED="1";
	/**
	 * 2=已出/入库
 	 */
	public static final String STATE_IN_OUT="2";
	
	private Long waybillsn;//运单号
	
	
	private Long ordersuuid;//原订单编号
	
	//表达一对多关系
	private List<Returnorderdetail> returnorderdetailList;//退货订单明细

	public Long getUuid() {		
		return uuid;
	}
	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}
	public java.util.Date getCreatetime() {		
		return createtime;
	}
	public void setCreatetime(java.util.Date createtime) {
		this.createtime = createtime;
	}
	public java.util.Date getChecktime() {		
		return checktime;
	}
	public void setChecktime(java.util.Date checktime) {
		this.checktime = checktime;
	}
	public java.util.Date getEndtime() {		
		return endtime;
	}
	public void setEndtime(java.util.Date endtime) {
		this.endtime = endtime;
	}
	public String getType() {		
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getCreater() {		
		return creater;
	}
	public void setCreater(Long creater) {
		this.creater = creater;
	}
	public Long getChecker() {		
		return checker;
	}
	public void setChecker(Long checker) {
		this.checker = checker;
	}
	public Long getEnder() {		
		return ender;
	}
	public void setEnder(Long ender) {
		this.ender = ender;
	}
	public Long getSupplieruuid() {		
		return supplieruuid;
	}
	public void setSupplieruuid(Long supplieruuid) {
		this.supplieruuid = supplieruuid;
	}
	public Double getTotalmoney() {		
		return totalmoney;
	}
	public void setTotalmoney(Double totalmoney) {
		this.totalmoney = totalmoney;
	}
	public String getState() {		
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Long getWaybillsn() {		
		return waybillsn;
	}
	public void setWaybillsn(Long waybillsn) {
		this.waybillsn = waybillsn;
	}
	public Long getOrdersuuid() {		
		return ordersuuid;
	}
	public void setOrdersuuid(Long ordersuuid) {
		this.ordersuuid = ordersuuid;
	}
	public List<Returnorderdetail> getReturnorderdetailList() {
		return returnorderdetailList;
	}
	public void setReturnorderdetailList(List<Returnorderdetail> returnorderdetailList) {
		this.returnorderdetailList = returnorderdetailList;
	}
	

}
