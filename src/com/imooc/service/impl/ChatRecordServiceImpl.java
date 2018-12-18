package com.imooc.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.transaction.Transactional;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.imooc.dao.IChatRecordDao;
import com.imooc.pojo.Chat_Group;
import com.imooc.pojo.Chat_Record;
import com.imooc.pojo.User;
import com.imooc.service.IChatRecordService;
import com.imooc.service.IUserService;
import com.opensymphony.xwork2.ActionContext;


/**
 * 群聊记录业务层实现类
 * 
 * @author Administrator
 *
 */
@Transactional
public class ChatRecordServiceImpl implements IChatRecordService {
	private IChatRecordDao chatRecordDao;

	public void setChatRecordDao(IChatRecordDao chatRecordDao) {
		this.chatRecordDao = chatRecordDao;
	}


	
 
	// 保存聊天记录到表
	@Override
	public void save(Chat_Record chat_Record) {
		chat_Record.setRecord_send_time(new Date());
		chatRecordDao.save(chat_Record);

	}

	// 通过user_id,group_id获取上次退登到此次登录之前是否有新消息+登录后未查看消息
	@Override
	public List<Map<String, Object>> getNewMsg(User user, Chat_Group chat_Group, IUserService userService
			) {
		Set<Chat_Record> set = chat_Group.getChat_Records();
		Iterator<Chat_Record> iterator = set.iterator();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();//记录该方法返回set
			//返回对应群所有消息
			//前台标记新消息数量
			while(iterator.hasNext()) {
				Chat_Record record = iterator.next();
				Map<String, Object> map = new HashMap<>();
					map.put("content", record.getRecord_content());
					// 通过user_id获取user2
					User user2 = userService.findUserByUserId(record.getUser_id());
					map.put("user_image", user2.getUser_image());
					map.put("user_account", user2.getUser_account());
					map.put("user_id", record.getUser_id());
					map.put("record_id", record.getRecord_id());
					// 符合条件将map放于set2里
					list.add(map);
			}
			return list;
	}
}
