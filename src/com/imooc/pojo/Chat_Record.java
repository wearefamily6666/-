package com.imooc.pojo;

import java.io.Serializable;
import java.util.Date;
import org.hibernate.annotations.BatchSize;
/**
 * 群聊天记录实体
 * @author Administrator
 *
 */
public class Chat_Record implements Serializable {
	private long record_id;
	private long group_id;//群id
	private long user_id;//发送人id
	private String record_content;//发送内容
	private Date record_send_time;//发送时间
	private boolean record_is_read;//是否已经查看
	Chat_Group chat_Group;
	public long getRecord_id() {
		return record_id;
	}
	public void setRecord_id(long record_id) {
		this.record_id = record_id;
	}
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
	public String getRecord_content() {
		return record_content;
	}
	public void setRecord_content(String record_content) {
		this.record_content = record_content;
	}
	public Date getRecord_send_time() {
		return record_send_time;
	}
	public void setRecord_send_time(Date record_send_time) {
		this.record_send_time = record_send_time;
	}
	
	public boolean isRecord_is_read() {
		return record_is_read;
	}
	public void setRecord_is_read(boolean record_is_read) {
		this.record_is_read = record_is_read;
	}
	public Chat_Group getChat_Group() {
		return chat_Group;
	}
	public void setChat_Group(Chat_Group chat_Group) {
		this.chat_Group = chat_Group;
	}
	@Override
	public String toString() {
		return "Chat_Record [record_id=" + record_id + ", group_id=" + group_id + ", user_id=" + user_id
				+ ", record_content=" + record_content + ", record_send_time=" + record_send_time + ", record_is_read="
				+ record_is_read + "]";
	}
 
 
	
}
