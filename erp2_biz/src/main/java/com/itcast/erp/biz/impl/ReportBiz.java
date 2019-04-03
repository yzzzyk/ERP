package com.itcast.erp.biz.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itcast.erp.biz.IReportBiz;
import com.itcast.erp.dao.IReportDao;

public class ReportBiz implements IReportBiz {

	private IReportDao reportDao;
	@Override
	public List orderReport(Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return reportDao.orderReport(startDate, endDate);
	}
	
	
	
	public void setReportDao(IReportDao reportDao) {
		this.reportDao = reportDao;
	}



	@Override
	public List<Map<String, Object>> trendreport(int year) {
		
		
		//map [{"month":12,"y":7499.61}]
		 List<Map<String,Object>> sumMoney = reportDao.getSumMoney(year);
		 
		 List<Map<String,Object>> rtnSumMoney = new ArrayList<>(12);
		 
		 //将数据转换成 key 月份 值 map [{"month":12,"y":7499.61}]形式
		 Map<Integer, Map<String,Object>>  maps=new HashMap<>();
		 
		 for (Map<String, Object> map : sumMoney) {
			 maps.put((Integer) map.get("month"), map);
		 }
		 

		 for (int i = 1; i <=12; i++) {
			Map<String,Object> m;
			if( maps.get(i)==null){//没取到了该月的值
				m=new HashMap<>();
				m.put("month", i+"月");
				m.put("y", 0);
				
			}else{ //取到了
				m=maps.get(i);
				m.put("month", m.get("month")+"月");
			}
			rtnSumMoney.add(m);
		 }	
		 return rtnSumMoney;
	}
	
	
}
