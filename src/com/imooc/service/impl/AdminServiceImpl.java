package com.imooc.service.impl;

import javax.transaction.Transactional;

import com.imooc.dao.IAdminDao;
import com.imooc.pojo.Admin;
import com.imooc.service.IAdminService;
/**
 * admin业务层实现类
 * @author Administrator
 *
 */
@Transactional
public class AdminServiceImpl implements IAdminService {
	private IAdminDao adminDao;
	
public void setAdminDao(IAdminDao adminDao) {
		this.adminDao = adminDao;
	}

	//	用户名和密码查询管理员
	@Override
	public Admin validateLogin(String user_account, String user_password) {
		Admin admin=adminDao.findByAccountAndPassword(user_account,user_password);
		return admin;
	}

}
