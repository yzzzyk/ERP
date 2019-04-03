package com.itcast.erp.action;

import javax.mail.MessagingException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.itcast.erp.biz.IStoredetailBiz;
import com.itcast.erp.entity.Storedetail;
import com.itcast.erp.exception.ErpException;

/**
 * 仓库库存Action
 * 
 * @author Administrator
 *
 */
public class StoredetailAction extends BaseAction<Storedetail> {

	private IStoredetailBiz storedetailBiz;

	public void setStoredetailBiz(IStoredetailBiz storedetailBiz) {
		this.storedetailBiz = storedetailBiz;
		setBaseBiz(storedetailBiz);
	}

	public void storeAlertList() {

		write(JSON.toJSONString(storedetailBiz.getStoreAlertList(),SerializerFeature.DisableCircularReferenceDetect));
		

	}

	public void sendStoreAlertMail() {

		try {
			storedetailBiz.sendStoreAlertMail();
			ajaxReturn(true, "邮件发送成功");
		} catch (MessagingException e) {
			e.printStackTrace();
			ajaxReturn(false, "发送邮件失败");
		} catch (ErpException e) {
			e.printStackTrace();
			ajaxReturn(false, e.getMessage());
		}
	}

}
