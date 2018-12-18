package com.imooc.service;

import java.util.HashMap;
import java.util.HashSet;

import com.imooc.pojo.PageBean;
import com.imooc.pojo.User;

/**
 * User类业务层
 * @author Administrator
 *
 */
public interface IUserService {


	User varifyLogin(User user);

	void updateUser(User user);

	User findUserByUserAccount(String user_account);

	void loginSuccess(User user);

	User findUserByUserId(long user_id);

	HashSet<HashMap<String, Object>> findAllUser();

	HashSet<Boolean> modifyUser(long user_id);

	HashSet<HashMap<String, Object>> findAllUserByKey(String key);

	PageBean<HashMap<String, Object>> findUserByPage(int page, int pageSize);

	PageBean<HashMap<String, Object>> findUserByFirstPage(int pageSize);

	PageBean<HashMap<String, Object>> findUserByLastPage(int pageSize);


	PageBean<HashMap<String, Object>> searchUserByPage(int currentPage, int pageSize, String key);

	PageBean<HashMap<String, Object>> searchUserByFirstPage(int pageSize, String key);

	PageBean<HashMap<String, Object>> searchUserByLastPage(int pageSize, String key);

	void modifyUser(User user, String str, long user_id);

 

}
