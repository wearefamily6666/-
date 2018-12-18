package com.imooc.action;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.struts2.json.annotations.JSON;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.imooc.dao.impl.ChatGroupDaoImpl;
import com.imooc.dao.impl.ChatRecordDaoImpl;
import com.imooc.pojo.Chat_Group;
import com.imooc.pojo.Chat_Record;
import com.imooc.pojo.User;
import com.imooc.service.IChatGroupService;
import com.imooc.service.IChatRecordService;
import com.imooc.service.IUserService;
import com.imooc.service.impl.UserServiceImpl;
import com.mysql.fabric.xmlrpc.base.Data;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 群聊天记录action
 * 
 * @author Administrator
 *
 */

public class ChatRecordAction extends ActionSupport implements ModelDriven<Chat_Record> {
	Chat_Record chat_Record = new Chat_Record();
	IChatRecordService chatRecordService;
	IUserService userService;
	IChatGroupService chatGroupService;
	private List<Map<String, Object>> maps;
	@Override
	public Chat_Record getModel() {
		// TODO Auto-generated method stub
		return chat_Record;
	}

	public void setChatRecordService(IChatRecordService chatRecordService) {
		this.chatRecordService = chatRecordService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	
	public void setChatGroupService(IChatGroupService chatGroupService) {
		this.chatGroupService = chatGroupService;
	}


	public List<Map<String, Object>> getMaps() {
		return maps;
	}
	
	//(获得所有消息：前台标记新消息数量)
	//前端记录从切换其他群到重新点击该群这段时间都是新消息
	//前端判断是否在当前群聊天界面，不在则开始记录消息
	public String getMsg() {
		User user=	userService.findUserByUserId(chat_Record.getUser_id());
		Chat_Group chat_Group= chatGroupService.findDetailByGroupId(chat_Record.getGroup_id());
		maps=chatRecordService.getNewMsg(user,chat_Group,userService);
		if (maps==null) {
			maps=new ArrayList<Map<String, Object>>();
		} 
		return "getMsg";
	}
	//返回聊天界面
	public String communicate() {
		return "communicate";
	}
	//返回邀请界面
	public String invitation() {
		return "invitation";
	}

}
