package com.imooc.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.imooc.pojo.Chat_Group;
import com.imooc.pojo.Chat_Record;
import com.imooc.pojo.User;

/**
 * 群聊记录业务层
 * @author Administrator
 *
 */
public interface IChatRecordService {


	void save(Chat_Record chat_Record);


	List<Map<String, Object>> getNewMsg(User user, Chat_Group chat_Group, IUserService userService);

}
