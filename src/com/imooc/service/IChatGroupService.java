package com.imooc.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.imooc.pojo.Chat_Group;
import com.imooc.pojo.Common_Message;
import com.imooc.pojo.PageBean;
import com.imooc.pojo.User;

/**
 * ChatGroup业务层
 * @author Administrator
 *
 */
public interface IChatGroupService {

 

	void save(Chat_Group chatGroup, String str);

	HashMap<String, Object> findAllJoined();

	Set<Chat_Group> findAllJoinByKey(String key);

	List<Chat_Group> findAllNotJoin();
	Set<Chat_Group> findAllNotJoinByKey(String key);

	Chat_Group findDetailByGroupId(long group_id);

	Set<Chat_Group> findAllNewGroup(long group_id);

	Set<User> findAllNewer(long group_id, long group_id2);

	Set<Chat_Group> findAllNewGroupByKey(String key, Set<Chat_Group> set);

	Set<HashMap<String, Object>> findAllMemberByGroupId(long group_id);

	Set<HashMap<String, Object>> searchMemberInRemovePage(long group_id, String key);

	Common_Message removeClick(String string, long group_id);

	Common_Message exitGroup(long user_id, long group_id);

	Set<HashMap<String, Object>> getLocation(long group_id);

	Set<HashMap<String, Object>> findAllGroup();

	HashSet<Boolean> modifyChatGroup(long group_id);

	HashSet<HashMap<String, Object>> findAllGroupByKey(String key);

	PageBean<HashMap<String, Object>> findGroupByPage(int currentPage, int pageSize);

	PageBean<HashMap<String, Object>> findGroupByFirstPage(int pageSize);

	PageBean<HashMap<String, Object>> findGroupByLastPage(int pageSize);

	PageBean<HashMap<String, Object>> searchGroupByPage(int currentPage, int pageSize, String key);

	PageBean<HashMap<String, Object>> searchGroupByFirstPage(int pageSize, String key);

	PageBean<HashMap<String, Object>> searchGroupByLastPage(int pageSize, String key);

	Set<Chat_Group> findAllJoinedGroup();

	void addGroupMember(long receiver_id, long group_id);

}
