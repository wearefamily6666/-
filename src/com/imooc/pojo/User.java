package com.imooc.pojo;

import java.io.File;

import java.io.Serializable;
/**
 * 普通用户表
 * @author Administrator
 *
 */
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.servlet.jsp.tagext.IterationTag;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.BatchSize;
public class User implements Serializable {
	long user_id;//普通用户id
	String user_account;//帐号
	String user_password;//密码
	String user_email;//邮箱
	String user_phone;//手机号
	String user_name;//昵称
	String user_image;//头像
	Date user_last_login_time;//上次登录时间
	boolean user_online;//是否在线
	boolean user_forbidden;//是否禁用
	 
	Set<Chat_Group> chat_Groups=new HashSet<Chat_Group>();// 群列表
	Set<Common_Message> common_Messages;//消息列表
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public String getUser_account() {
		return user_account;
	}
	public void setUser_account(String user_account) {
		this.user_account = user_account;
	}
	public String getUser_password() {
		return user_password;
	}
	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	public String getUser_phone() {
		return user_phone;
	}
	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_image() {
		return user_image;
	}
	public void setUser_image(String user_image) {
		this.user_image = user_image;
	}
	public Date getUser_last_login_time() {
		return user_last_login_time;
	}
	public void setUser_last_login_time(Date user_last_login_time) {
		this.user_last_login_time = user_last_login_time;
	}
	public boolean getUser_online() {
		return user_online;
	}
	public void setUser_online(boolean user_online) {
		this.user_online = user_online;
	}
	public boolean getUser_forbidden() {
		return user_forbidden;
	}
	public void setUser_forbidden(boolean user_forbidden) {
		this.user_forbidden = user_forbidden;
	}
	public Set<Chat_Group> getChat_Groups() {
		return chat_Groups;
	}
	public void setChat_Groups(Set<Chat_Group> chat_Groups) {
		this.chat_Groups = chat_Groups;
	}
	
	public Set<Common_Message> getCommon_Messages() {
		return common_Messages;
	}
	public void setCommon_Messages(Set<Common_Message> common_Messages) {
		this.common_Messages = common_Messages;
	}
	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", user_account=" + user_account + ", user_password=" + user_password
				+ ", user_email=" + user_email + ", user_phone=" + user_phone + ", user_name=" + user_name
				+ ", user_image=" + user_image + ", user_last_login_time=" + user_last_login_time + ", user_online="
				+ user_online + ", user_forbidden=" + user_forbidden + "]";
	}

 
 
}
