package com.imooc.pojo;
/**
 * 用户群关联表
 * @author Administrator
 *
 */
public class User_Group_Relation {
	private long user_group_id;
	private long user_id;
	private long group_id;
	public long getUser_group_id() {
		return user_group_id;
	}
	public void setUser_group_id(long user_group_id) {
		this.user_group_id = user_group_id;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public long getGroup_id() {
		return group_id;
	}
	public void setGroup_id(long group_id) {
		this.group_id = group_id;
	}
	@Override
	public String toString() {
		return "User_Group_Relation [user_group_id=" + user_group_id + ", user_id=" + user_id + ", group_id=" + group_id
				+ "]";
	}
	
}
