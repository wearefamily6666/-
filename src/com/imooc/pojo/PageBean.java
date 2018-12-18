package com.imooc.pojo;
/**
 * 分页bean
 * @author Administrator
 *
 */

import java.util.HashSet;
import java.util.List;

public class PageBean<T> {
	private int currentPage;//当前页数
	private int pageSize;//每页记录数
	private int totalPage;//总页数
	private int totalCount;//总数量
	private List<T> list;//T表示分页实体，如用户，群
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
 
 
	
}

