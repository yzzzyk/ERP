package com.itcast.erp.biz;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IReportBiz {
	/**
	 * 根据商品类型销售报表统计
	 * @param startDate 起始日期
	 * @param endDate 截止日期 
	 * @return
	 */
	public List orderReport(Date startDate,Date endDate);
	
	/**
	 * 销售趋势
	 * @param year
	 * @return
	 */
	public List<Map<String, Object>> trendreport(int year);
}
