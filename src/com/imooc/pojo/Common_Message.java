package com.imooc.pojo;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.BatchSize;
/**
 * 普通消息表
 * @author Administrator
 *
 */
public class Common_Message implements Serializable {
	long message_id;//消息通知id
	long receiver_id;//接收人id
	long sender_id;//发送人id
	long group_id;//群id
//	四种情况
//	（1.拉人进群
//	2.移人出群
//	3.解散群聊
	//4.邀请）
	int message_type;//消息类型（1：不可操作，2：可以操作）
	String message_title;//消息标题
	String message_content;//消息内容
	Date message_send_time;//发送时间
	boolean message_read;//是否已读
	boolean message_receive;//是否已接受（针对申请加群，邀请成员）
	User user;
	public long getMessage_id() {
		return message_id;
	}
	public void setMessage_id(long message_id) {
		this.message_id = message_id;
	}
	public long getReceiver_id() {
		return receiver_id;
	}
	public void setReceiver_id(long receiver_id) {
		this.receiver_id = receiver_id;
	}
	public long getSender_id() {
		return sender_id;
	}
	public void setSender_id(long sender_id) {
		this.sender_id = sender_id;
	}
	public long getGroup_id() {
		return group_id;
	}
	public void setGroup_id(long group_id) {
		this.group_id = group_id;
	}
	public int getMessage_type() {
		return message_type;
	}
	public void setMessage_type(int message_type) {
		this.message_type = message_type;
	}
	public String getMessage_title() {
		return message_title;
	}
	public void setMessage_title(String message_title) {
		this.message_title = message_title;
	}
	public String getMessage_content() {
		return message_content;
	}
	public void setMessage_content(String message_content) {
		this.message_content = message_content;
	}
	public Date getMessage_send_time() {
		return message_send_time;
	}
	public void setMessage_send_time(Date message_send_time) {
		this.message_send_time = message_send_time;
	}
	public boolean isMessage_read() {
		return message_read;
	}
	public void setMessage_read(boolean message_read) {
		this.message_read = message_read;
	}
	
	public boolean isMessage_receive() {
		return message_receive;
	}
	public void setMessage_receive(boolean message_receive) {
		this.message_receive = message_receive;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "Common_Message [message_id=" + message_id + ", receiver_id=" + receiver_id + ", sender_id=" + sender_id
				+ ", group_id=" + group_id + ", message_type=" + message_type + ", message_title=" + message_title
				+ ", message_content=" + message_content + ", message_send_time=" + message_send_time
				+ ", message_read=" + message_read + ", message_receive=" + message_receive + "]";
	}
	 
 
}
