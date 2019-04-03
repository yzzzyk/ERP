package com.itcast.erp.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * 报表数据访问接口
 * 
 * @author Administrator
 *
 */
public interface IReportDao {

	/**
	 * 根据商品类型销售报表统计
	 * 
	 * @param startDate
	 *            起始日期
	 * @param endDate
	 *            截止日期
	 * @return
	 */
	public List orderReport(Date startDate, Date endDate);

	/**
	 * 查询某一年各月份的销售额
	 * @param year
	 * @return
	 */
	public List<Map<String, Object>> getSumMoney(int year);
}
