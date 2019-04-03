package com.itcast.erp.action;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;import com.alibaba.fastjson.JSON;
import com.itcast.erp.biz.IReportBiz;

public class ReportAction {
	
	
	private Date startDate;
	private Date endDate;
	private int year;
	
	private IReportBiz reportBiz;
	
	public void setReportBiz(IReportBiz reportBiz) {
		this.reportBiz = reportBiz;
	}
	//报表
	public void orderReport(){
		List list = reportBiz.orderReport(startDate, endDate);
		
		write(JSON.toJSONString(list));
		
	}
	
	//趋势
	public void trendReport(){
		
		
		write(JSON.toJSONString(reportBiz.trendreport(year)));
		
	}

	
	/**
	 * 输出字符串
	 * 
	 * @param jsonString
	 */
	public void write(String jsonString) {

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		try {
			response.getWriter().print(jsonString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
}
