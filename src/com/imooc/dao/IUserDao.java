package com.imooc.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.imooc.pojo.User;

/**
 * 登录dao层
 * @author Administrator
 *
 */
public interface IUserDao {


	User findByUserAccountAndPassword(String user_account, String user_password);
	void modifyUser(User user);
	User findUserByUserId(long user_id);
	User findUserByUserAccount(String user_account);
	void updateUserByUserAccount(User user, boolean flag, int type);
	List<User> findAllUser();
	HashSet<Boolean> modifyUser(long user_id);
	List<User> findAllUserByKey(String key);
	List<User> findUserByPage(int beginIndex, int pageSize);
	List<User> searchUserByPage(int beginIndex, int pageSize, String key);
}
