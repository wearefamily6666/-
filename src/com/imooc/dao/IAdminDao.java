package com.imooc.dao;

import com.imooc.pojo.Admin;

/**
 * admin类dao层接口
 * @author Administrator
 *
 */
public interface IAdminDao {

	Admin findByAccountAndPassword(String user_account, String user_password);

}
