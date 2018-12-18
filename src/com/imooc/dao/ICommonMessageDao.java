package com.imooc.dao;

import java.util.List;

import com.imooc.pojo.Chat_Group;
import com.imooc.pojo.Common_Message;
import com.imooc.pojo.User;

/**
 * 普通消息实体dao
 * @author Administrator
 *
 */
public interface ICommonMessageDao {

	List<Common_Message> findByUserId(long user_id);

	Chat_Group findUserIdByGroupId(long group_id);

	User findUserByUserId(long sender_id);

	void save(Common_Message common_Message);

	Common_Message findCommonMessageById(long commonMessage_id);

	void update(Common_Message common_Message);

	List<Common_Message> findAllByUserId(long user_id);

}
