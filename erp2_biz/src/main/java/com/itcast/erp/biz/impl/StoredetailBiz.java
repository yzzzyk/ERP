package com.itcast.erp.biz.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import com.itcast.erp.biz.IStoredetailBiz;
import com.itcast.erp.dao.IGoodsDao;
import com.itcast.erp.dao.IStoreDao;
import com.itcast.erp.dao.IStoredetailDao;
import com.itcast.erp.entity.Store;
import com.itcast.erp.entity.StoreAlert;
import com.itcast.erp.entity.Storedetail;
import com.itcast.erp.exception.ErpException;
import com.itcast.erp.util.MailUtil;

/**
 * 仓库库存业务逻辑类
 * 
 * @author Administrator
 *
 */
public class StoredetailBiz extends BaseBiz<Storedetail> implements IStoredetailBiz {

	private IStoredetailDao storedetailDao;
	private IStoreDao storeDao;
	private IGoodsDao goodsDao;
	private MailUtil mailUtil;
	
	//收件人的地址
	private String to;
	//邮件主题
	private String subject;
	//邮件正文
	private String text;

	public List<Storedetail> getListByPage(Storedetail t1, Storedetail t2, Object param, int firstResult,
			int maxResults) {
		List<Storedetail> list = super.getListByPage(t1, t2, param, firstResult, maxResults);
		Map<Long, String> storeNameMap = new HashMap<>();
		Map<Long, String> goodsNameMap = new HashMap<>();

		for (Storedetail storedetail : list) {
			storedetail.setGoodsName(getGoodsName(storedetail.getGoodsuuid(), goodsNameMap, goodsDao));
			storedetail.setStoreName(getStoreName(storedetail.getStoreuuid(), storeNameMap, storeDao));
		}
		return list;
	}

	@Override
	public List<StoreAlert> getStoreAlertList() {

		return storedetailDao.getStoreAlertList();
	}

	@Override
	public void sendStoreAlertMail() throws MessagingException {
		// 差询 出需要预警的商品
		List<StoreAlert> storeAlertList = storedetailDao.getStoreAlertList();

		int cnt = storeAlertList == null ? 0 : storeAlertList.size();

		// 存在需要预警的商品
		if (cnt > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String formatDate = sdf.format(new Date());
			mailUtil.sendMail(to, subject.replace("[time]", formatDate),
					text.replace("[count]", cnt+""));
		}else
			throw new ErpException("不存在需要预警的商品");

	}

	public void setStoreDao(IStoreDao storeDao) {
		this.storeDao = storeDao;
	}

	public void setGoodsDao(IGoodsDao goodsDao) {
		this.goodsDao = goodsDao;
	}

	public void setStoredetailDao(IStoredetailDao storedetailDao) {
		this.storedetailDao = storedetailDao;
		setBaseDao(storedetailDao);
	}

	public void setMailUtil(MailUtil mailUtil) {
		this.mailUtil = mailUtil;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
