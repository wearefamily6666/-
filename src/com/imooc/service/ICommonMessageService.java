package com.imooc.service;

import java.util.HashMap;
import java.util.List;

import com.imooc.pojo.Chat_Group;
import com.imooc.pojo.Common_Message;
import com.imooc.pojo.User;

/**
 * 普通消息实体业务层
 * @author Administrator
 *
 */
public interface ICommonMessageService {

	List<HashMap<String, Object>> findByUserId(long user_id);

	Common_Message applyToJoin(Common_Message commonMessage);


	Common_Message doInvite(User user, Chat_Group chat_Group, User sender);

	Common_Message findDetailById(long commonMessage_id);

	void update(Common_Message common_Message);

}
