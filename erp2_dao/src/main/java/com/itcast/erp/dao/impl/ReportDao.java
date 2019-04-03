package com.itcast.erp.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.itcast.erp.dao.IReportDao;

public class ReportDao extends HibernateDaoSupport implements IReportDao {
			/*=======================注意表达了多对一关系的   属性*/
	@Override
	public List orderReport(Date date1, Date date2) {//	[["儿童食品",6786.51],["调味品",63.3],["水果",649.8]] 二维数组
		String hql="select  new Map(gt.name as name,sum(ol.money) as y) "
				+ "from Orders o,Orderdetail ol,Goods g,Goodstype gt "
				+ "where g.goodstype=gt and ol.goodsuuid=g.uuid and ol.orders=o "
				+ "and o.type=2 ";
				
		List<Date> queryParam=new ArrayList<>();
		if(date1!=null){
			hql+=" and o.createtime >= ?";
			queryParam.add(date1);
		}
		if(date2!=null){
			hql+=" and o.createtime <=?";
			queryParam.add(date2);
			
		}
		hql+= " group by gt.name";
	
		if(queryParam.size()>0){
			
			return getHibernateTemplate().find(hql,queryParam.toArray(new Date[]{}));
		}
		return getHibernateTemplate().find(hql);
	}

	
	@Override
	public List<Map<String, Object>> getSumMoney(int year) {//map 格式[ {"month":12,"y":7499.61} ]
		
		String hql="select new Map(month(o.createtime) as month,sum(ol.money) as y)"
				+ "from Orders o,Orderdetail ol "
				+ "where o=ol.orders and o.type=2 and year(o.createtime) =?"
				+ "group by month(o.createtime)";
		return (List<Map<String, Object>>) getHibernateTemplate().find(hql, year);
	}

}
