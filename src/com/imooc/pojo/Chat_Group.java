package com.imooc.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.BatchSize;
/**
 * 群组表
 * @author Administrator
 *
 */
public class Chat_Group implements Serializable {
	long group_id;//群id
	long user_id;//群主id
	String group_account;//群账号id
	String group_name;//群名称
	int group_sum;//群人数
	String group_image;//群头像
	Date group_create_date;//群创建日期
	String group_notice;//群公告
	boolean group_forbidden;//是否禁用
	
	Set<User> users=new HashSet<User>();//用户表
	Set<Chat_Record> chat_Records;//群聊天记录集合
	public long getGroup_id() {
		return group_id;
	}
	public void setGroup_id(long group_id) {
		this.group_id = group_id;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public String getGroup_account() {
		return group_account;
	}
	public void setGroup_account(String group_account) {
		this.group_account = group_account;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public int getGroup_sum() {
		return group_sum;
	}
	public void setGroup_sum(int group_sum) {
		this.group_sum = group_sum;
	}
	public String getGroup_image() {
		return group_image;
	}
	public void setGroup_image(String group_image) {
		this.group_image = group_image;
	}
	public Date getGroup_create_date() {
		return group_create_date;
	}
	public void setGroup_create_date(Date group_create_date) {
		this.group_create_date = group_create_date;
	}
	public String getGroup_notice() {
		return group_notice;
	}
	public void setGroup_notice(String group_notice) {
		this.group_notice = group_notice;
	}
	public boolean isGroup_forbidden() {
		return group_forbidden;
	}
	public void setGroup_forbidden(boolean group_forbidden) {
		this.group_forbidden = group_forbidden;
	}
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	public Set<Chat_Record> getChat_Records() {
		return chat_Records;
	}
	public void setChat_Records(Set<Chat_Record> chat_Records) {
		this.chat_Records = chat_Records;
	}
	@Override
	public String toString() {
		return "Chat_Group [group_id=" + group_id + ", user_id=" + user_id + ", group_account=" + group_account
				+ ", group_name=" + group_name + ", group_sum=" + group_sum + ", group_image=" + group_image
				+ ", group_create_date=" + group_create_date + ", group_notice=" + group_notice + ", group_forbidden="
				+ group_forbidden + "]";
	}
 
 
}
