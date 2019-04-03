package com.itcast.erp.job;

import java.util.List;

import javax.mail.MessagingException;

import com.itcast.erp.biz.IStoredetailBiz;
import com.itcast.erp.biz.impl.StoredetailBiz;
import com.itcast.erp.entity.StoreAlert;

/**
 * 
 * 后台定时检测库存预警
 * 如果存在库存预警 自动发送邮件
 * @author Administrator
 *
 */
public class MailJob {
	
	
	private IStoredetailBiz storedetailBiz;
	
	public void sendStoreAlertMail(){
		List<StoreAlert> alertList = storedetailBiz.getStoreAlertList();
		if(alertList.size()>0){
			try {
				//调用业务发送预警
				storedetailBiz.sendStoreAlertMail();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	public void setStoredetailBiz(IStoredetailBiz storedetailBiz) {
		this.storedetailBiz = storedetailBiz;
	}
	
}
