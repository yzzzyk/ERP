package com.itcast.erp.biz;
import java.util.List;

import javax.mail.MessagingException;

import com.itcast.erp.entity.StoreAlert;
import com.itcast.erp.entity.Storedetail;
/**
 * 仓库库存业务逻辑层接口
 * @author Administrator
 *
 */
public interface IStoredetailBiz extends IBaseBiz<Storedetail>{
	
	/**
	 * 获取商品库存不足的 商品的 预警类
	 * @return
	 */
	List<StoreAlert> getStoreAlertList();
	
	/**
	 * 发送邮件
	 * @throws MessagingException 
	 */
	void sendStoreAlertMail() throws MessagingException;

}

