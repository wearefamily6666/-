package com.imooc.service;

import com.imooc.pojo.Admin;

/**
 * admin业务层接口
 * @author Administrator
 *
 */
public interface IAdminService {

	Admin validateLogin(String user_account, String user_password);

}
