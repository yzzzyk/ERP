package com.itcast.erp.biz.impl;

import java.util.List;
import java.util.Map;

import com.itcast.erp.dao.IBaseDao;
import com.itcast.erp.dao.IEmpDao;
import com.itcast.erp.dao.IGoodsDao;
import com.itcast.erp.dao.IStoreDao;
/**
 * 基本业务逻辑类
 * @author Administrator
 *
 * @param <T>
 */
public class BaseBiz<T> {

	private IBaseDao baseDao;
	
	public void setBaseDao(IBaseDao baseDao) {
		this.baseDao = baseDao;
	}

	/**
	 * 条件查询
	 */
	public List<T> getList(T t1,T t2,Object param) {
		return baseDao.getList(t1,t2,param);
	}

	/**
	 * 分页条件查询
	 */
	public List<T> getListByPage(T t1, T t2, Object param, int firstResult, int maxResults) {
		
		return baseDao.getListByPage(t1, t2, param, firstResult, maxResults);
	}

	/**
	 * 统计记录个数
	 */
	public long getCount(T t1, T t2, Object param) {
		
		return baseDao.getCount(t1, t2, param);
	}

	/**
	 * 增加
	 */
	public void add(T t) {
		baseDao.add(t);		
	}
	
	/**
	 * 修改
	 */
	public void update(T t) {
		baseDao.update(t);		
	}

	/**
	 * 删除
	 */
	public void delete(Long id) {
		baseDao.delete(id);		
	}

	/**
	 * 查询实体
	 */
	public T get(Long id) {		
		return (T) baseDao.get(id);
	}
	
	/**
	 * 查询实体
	 */
	public T get(String id) {		
		return (T) baseDao.get(id);
	}
	
	public String getStoreName(Long uuid,Map<Long,String> storeNameMap,IStoreDao storeDao){
		if(uuid==null){
			return null;
		}
		String storeName=storeNameMap.get(uuid);
		if(storeName==null){//	集合中没有
			storeName=storeDao.get(uuid).getName();
			
		}
		return storeName;	
	}
	
	public String getGoodsName(Long uuid,Map<Long,String> goodsNameMap,IGoodsDao goodsDao){
		if(uuid==null){
			return null;
		}
		String goodsName=goodsNameMap.get(uuid);
		if(goodsName==null){//	集合中没有
			goodsName=goodsDao.get(uuid).getName();		
		}
		return goodsName;	
	}
	public String getEmpName(Long uuid, Map<Long,String> nameMap,IEmpDao empDao){
		//
		if(uuid==null){
			return null;
		}
		//
		String empName = nameMap.get(uuid);
		if(empName==null){//集合中没有需要查询
			empName =empDao.get(uuid).getName();
		}
		return empName;
	}
}
