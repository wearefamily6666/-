package com.imooc.dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.imooc.pojo.Chat_Group;
import com.imooc.pojo.User;
import com.imooc.pojo.User_Group_Relation;

/**
 * ChatGroup实体dao
 * @author Administrator
 *
 */
public interface IChatGroupDao {

	Set<Chat_Group> findChatGroups(User user);


	Set<Chat_Group> findAllJoinedByKey(String key);


	Chat_Group findDetailByGroupId(long group_id);


	List<Chat_Group> findAllNewer(long group_id);


	List<Chat_Group> findAllNewGroup(long group_id);


	Set<User> findAllMemberByGroupId(long group_id);


	void removeUserFromGroup(long l, long group_id);


	void delete(Chat_Group chat_Group);


	void deleteUserGroupRelation(long user_id, long group_id);


	List<Chat_Group> findAllGroup();


	HashSet<Boolean> modifyChatGroup(long group_id);


	List<Chat_Group> findAllGroupByKey(String key);


	List<Chat_Group> findGroupByPage(int beginIndex, int pageSize);


	List<Chat_Group> searchGroupByKey(int beginIndex, int pageSize, String key);


	void save(Chat_Group chatGroup, User user);



	Set<Chat_Group> findAllNotJoinByKey(String key, Set<Chat_Group> set);


	void removeGroup(User user, Chat_Group chat_Group);



	void addGroupMember(User_Group_Relation user_Group_Relation);



	
}
