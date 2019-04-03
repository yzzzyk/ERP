package com.itcast.erp.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * 加载Tree 的数据结构
 */
public class Tree {
	private String id;//菜单id
	private String text;//菜单名字
	private boolean checked;//是否被选中
	
	private List<Tree> children;//下级菜单
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public List<Tree> getChildren() {
		if (null == children) {
			children = new ArrayList<Tree>();
		}

		return children;
	}

	public void setChildren(List<Tree> children) {
		this.children = children;
	}
	
}
