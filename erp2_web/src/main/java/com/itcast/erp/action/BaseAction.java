package com.itcast.erp.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.itcast.erp.biz.IBaseBiz;
import com.itcast.erp.entity.Emp;
import com.opensymphony.xwork2.ActionContext;

/**
 * 基本Action
 * 
 * @author Administrator
 *
 */
public class BaseAction<T> {

	private IBaseBiz baseBiz;

	public void setBaseBiz(IBaseBiz baseBiz) {
		this.baseBiz = baseBiz;
	}

	private T t1;

	public T getT1() {
		return t1;
	}

	public void setT1(T t1) {
		this.t1 = t1;
	}

	private T t2;

	public T getT2() {
		return t2;
	}

	public void setT2(T t2) {
		this.t2 = t2;
	}

	private Object param;

	public Object getParam() {
		return param;
	}

	public void setParam(Object param) {
		this.param = param;
	}

	/**
	 * 查询全部列表
	 */
	public void list() {

		List<T> list = baseBiz.getList(t1, t2, param);
		// SerializerFeature.DisableCircularReferenceDetect取消引用 
		String jsonString = JSON.toJSONString(list, SerializerFeature.DisableCircularReferenceDetect);
		write(jsonString);
	}

	private int page;// 页码
	private int rows;// 每页记录数

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * 查询全部列表
	 */
	public void listByPage() {
		int firstResult = (page - 1) * rows;
		List<T> list = baseBiz.getListByPage(t1, t2, param, firstResult, rows);
		long count = baseBiz.getCount(t1, t2, param);
		Map map = new HashMap();
		map.put("rows", list);
		map.put("total", count);

		// DisableCircularReferenceDetect 禁用循环引用保护
		String jsonString = JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect);
		write(jsonString);
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
	
	private T t;

	
	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}

	/**
	 * 增加
	 */
	public void add() {

		try {
			baseBiz.add(t);
			ajaxReturn(true, "增加成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ajaxReturn(false, "增加失败");
		}
	}

	/**
	 * 修改
	 */
	public void update() {

		try {
			baseBiz.update(t);
			ajaxReturn(true, "修改成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ajaxReturn(false, "修改失败");
		}
	}

	/**
	 * 标准结构返回体
	 * 
	 * @param success
	 * @param message
	 * @return
	 */
	public void ajaxReturn(boolean success, String message) {

		Map map = new HashMap();
		map.put("success", success);
		map.put("message", message);
		write(JSON.toJSONString(map));
	}

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 删除
	 */
	public void delete() {

		try {
			baseBiz.delete(id);
			ajaxReturn(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "删除失败");
		}
	}

	/**
	 * 根据id获取实体(辅助修改回写功能)
	 */
	public void get() {
		T t3 = (T) baseBiz.get(id);
		String jsonString = JSON.toJSONStringWithDateFormat(t3, "yyyy-MM-dd");
		// 响应为 t.dep 数组[] 表单字段name="t.dep.uuid"
		write(mapJson(jsonString, "t"));
	}

	/**
	 * 自定添加前缀
	 * 
	 * @param jsonString
	 * @param prefix
	 * @return
	 */
	private String mapJson(String jsonString, String prefix) {

		Map<String, Object> map = JSON.parseObject(jsonString);
		Map<String, Object> newmap = new HashMap();
		for (String key : map.keySet()) {
			if (map.get(key) instanceof Map) {// t.dep":{"name":"绠＄悊鍛樼粍","tele":"000000","uuid":1}
				Map<String, Object> map2 = (Map) map.get(key);// 获得集合dep

				for (String key2 : map2.keySet()) {
					newmap.put(prefix + "." + key + "." + key2, map2.get(key2));
				}
			} else
				newmap.put(prefix + "." + key, map.get(key));
		}
		return JSON.toJSONString(newmap);
	}

	/** 
	 * 获取登陆的用户
	 * @return  
	 */
	public Emp getloginUser(){
		
		return (Emp) SecurityUtils.getSubject().getPrincipal();
		
	}
}
