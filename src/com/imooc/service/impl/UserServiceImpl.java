package com.imooc.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;

import com.imooc.dao.IUserDao;
import com.imooc.pojo.PageBean;
import com.imooc.pojo.User;
import com.imooc.service.IUserService;
import com.opensymphony.xwork2.ActionContext;
/**
 * 登录业务层实现类
 * @author Administrator
 *
 */
@Transactional
public class UserServiceImpl implements IUserService {
	private IUserDao userDao;
	
	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	// 验证登录信息
	@Override
	public User varifyLogin(User user) {
		
		user= userDao.findByUserAccountAndPassword(user.getUser_account(),user.getUser_password());
		return user;
	}

	//退登处理
	@Override
	public void updateUser(User user) {
		userDao.updateUserByUserAccount(user,false,2);
	}
	
 
//	查找用户
	@Override
	public User findUserByUserId(long user_id) {
		User user=userDao.findUserByUserId(user_id);
		return user;
	}
	
//修改个人资料
	@Override
	public void modifyUser(User user, String fileFileName,long user_id) {
		// TODO Auto-generated method stub
		User user2=userDao.findUserByUserId(user_id);
		if (fileFileName!=null&&!fileFileName.equals("")) {
			user2.setUser_image("http://localhost:8080/The_vertical_and_horizontal/image_imageInteface?userHeader="+fileFileName);
		}
		user2.setUser_name(user.getUser_name());
		user2.setUser_phone(user.getUser_phone());
		user2.setUser_email(user.getUser_email());
		 userDao.modifyUser(user2);
		 ActionContext.getContext().getSession().put("existUser", user2);
	}

	//登录成功
	@Override
	public void loginSuccess(User user) {
		// TODO Auto-generated method stub
		userDao.updateUserByUserAccount(user,true,1);
	}

	@Override
	public User findUserByUserAccount(String user_account) {
		User user=userDao.findUserByUserAccount(user_account);
		return user;
	}
	//获得所有用户
	@Override
	public HashSet<HashMap<String, Object>> findAllUser() {
		
		HashSet<HashMap<String, Object>> maps=new HashSet<>();
		List<User> users=userDao.findAllUser();
		for (User user : users) {
			HashMap<String, Object> map=new HashMap<>();
			map.put("user_id", user.getUser_id());
			map.put("user_account", user.getUser_account());
			map.put("last_logout_time", String.valueOf(user.getUser_last_login_time()).replace('T', ' '));
			map.put("user_online", user.getUser_online());
			if (user.getUser_forbidden()) {
				map.put("user_forbidden", true);
			}else {
				map.put("user_forbidden", false);
			}
			
			maps.add(map);
		}
		return maps;
	}
	//根据关键词获得所有用户
	private HashSet<HashMap<String, Object>> searchAllUser(String key) {
		HashSet<HashMap<String, Object>> maps=new HashSet<>();
		List<User> users=userDao.findAllUserByKey(key);
		for (User user : users) {
			HashMap<String, Object> map=new HashMap<>();
			map.put("user_id", user.getUser_id());
			map.put("user_account", user.getUser_account());
			map.put("last_logout_time", String.valueOf(user.getUser_last_login_time()).replace('T', ' '));
			map.put("user_online", user.getUser_online());
			if (user.getUser_forbidden()) {
				map.put("user_forbidden", true);
			}else {
				map.put("user_forbidden", false);
			}
			
			maps.add(map);
		}
		return maps;
	}
//修改个人资料（针对是否禁止用户登录）
	@Override
	public HashSet<Boolean> modifyUser(long user_id) {
		HashSet<Boolean> set= userDao.modifyUser(user_id);
		return set;
	}
	//关键词查询所有用户
	@Override
	public HashSet<HashMap<String, Object>> findAllUserByKey(String key) {
		HashSet<HashMap<String, Object>> maps=new HashSet<>();
		List<User> users=userDao.findAllUserByKey(key);
		for (User user : users) {
			HashMap<String, Object> map=new HashMap<>();
			map.put("user_id", user.getUser_id());
			map.put("user_account", user.getUser_account());
			map.put("last_logout_time", String.valueOf(user.getUser_last_login_time()).replace('T', ' '));
			map.put("user_online", user.getUser_online());
			if (user.getUser_forbidden()) {
				map.put("user_forbidden", true);
			}else {
				map.put("user_forbidden", false);
			}
			
			maps.add(map);
		}
		return maps;
	}
	//封装pageBean
	 public PageBean<HashMap<String, Object>> getPageBean(int page,int totalCount,int totalPage,int beginIndex,int pageSize,List<HashMap<String, Object>>list){
			PageBean<HashMap<String, Object>> pageBean=new PageBean<>();
			pageBean.setCurrentPage(page);
			pageBean.setTotalCount(totalCount);
			pageBean.setTotalPage(totalPage);
		
			//根据索引和每页数量查询
			List<User> users= userDao.findUserByPage(beginIndex,pageSize);
			for (User user : users) {
				HashMap<String, Object> map=new HashMap<>();
				map.put("user_id", user.getUser_id());
				map.put("user_account", user.getUser_account());
				map.put("last_logout_time", String.valueOf(user.getUser_last_login_time()).replace('T', ' '));
				map.put("user_online", user.getUser_online());
				if (user.getUser_forbidden()) {
					map.put("user_forbidden", true);
				}else {
					map.put("user_forbidden", false);
				}
				 
				list.add(map);
			}
			pageBean.setList(list);
			return pageBean;
	 }
	private PageBean<HashMap<String, Object>> getPageBean(int page, int totalCount, int totalPage, int beginIndex,
				int pageSize, List<HashMap<String, Object>>list, String key) {
		PageBean<HashMap<String, Object>> pageBean=new PageBean<>();
		pageBean.setCurrentPage(page);
		pageBean.setTotalCount(totalCount);
		pageBean.setTotalPage(totalPage);
	
		//根据索引和每页数量查询
		List<User> users= userDao.searchUserByPage(beginIndex,pageSize,key);
		for (User user : users) {
			HashMap<String, Object> map=new HashMap<>();
			map.put("user_id", user.getUser_id());
			map.put("user_account", user.getUser_account());
			map.put("last_logout_time", String.valueOf(user.getUser_last_login_time()).replace('T', ' '));
			map.put("user_online", user.getUser_online());
			if (user.getUser_forbidden()) {
				map.put("user_forbidden", true);
			}else {
				map.put("user_forbidden", false);
			}
			 
			list.add(map);
		}
		pageBean.setList(list);
		return pageBean;
		}
	//根据页数和每页数量查询
	@Override
	public PageBean<HashMap<String, Object>> findUserByPage(int page, int pageSize) {
		List<HashMap<String, Object>> list=new ArrayList<>();
		int totalCount=findAllUser().size();//获得总量
		Double t= Math.ceil(totalCount/pageSize);//获得总页数
		int totalPage=t.intValue();
		if (t<1) {
			totalPage=1;
		}
		int beginIndex=pageSize*page-1;//获得开始索引
	return	getPageBean(page, totalCount, totalPage, beginIndex, pageSize, list);
		 
	}

	//根据首页和每页数量查询
	@Override
	public PageBean<HashMap<String, Object>> findUserByFirstPage(int pageSize) {
		List<HashMap<String, Object>> list=new ArrayList<>();
		int totalCount=findAllUser().size();//获得总量
		Double t= Math.ceil(totalCount/pageSize);//获得总页数
		int totalPage=t.intValue();
		if (t<1) {
			totalPage=1;
		}
		return	getPageBean(1, totalCount, totalPage, 0, pageSize, list);
	}
	//根据尾页和每页数量查询
	@Override
	public PageBean<HashMap<String, Object>> findUserByLastPage(int pageSize) {
		List<HashMap<String, Object>> list=new ArrayList<>();
		int totalCount=findAllUser().size();//获得总量
		Double t= Math.ceil(totalCount/pageSize);//获得总页数
		int totalPage=t.intValue();
		if (t<1) {
			totalPage=1;
		}
		int lastIndex=(totalPage-1)*pageSize;//获得尾页索引
		return	getPageBean(totalPage, totalCount, totalPage, lastIndex, pageSize, list);
	}
	//根据关键词，页数和每页数量查询用户列表
	@Override
	public PageBean<HashMap<String, Object>> searchUserByPage(int currentPage, int pageSize, String key) {
		List<HashMap<String, Object>> list=new ArrayList<>();
		int totalCount=searchAllUser(key).size();//获得总量
		Double t= Math.ceil(totalCount/pageSize);//获得总页数
		int totalPage=t.intValue();
		if (t<1) {
			totalPage=1;
		}
		int beginIndex=pageSize*currentPage-1;//获得开始索引
		return	getPageBean(1, totalCount, totalPage, beginIndex, pageSize, list,key);
	}


	//根据关键词，首页和每页数量查询用户列表
	@Override
	public PageBean<HashMap<String, Object>> searchUserByFirstPage(int pageSize, String key) {
		List<HashMap<String, Object>> list=new ArrayList<>();
		int totalCount=searchAllUser(key).size();//获得总量
		Double t= Math.ceil(totalCount/pageSize);//获得总页数
		int totalPage=t.intValue();
		if (t<1) {
			totalPage=1;
		}
		return	getPageBean(1, totalCount, totalPage, 0, pageSize, list,key);
	}


	//根据关键词，尾页和每页数量查询用户列表
	@Override
	public PageBean<HashMap<String, Object>> searchUserByLastPage(int pageSize, String key) {
		List<HashMap<String, Object>> list=new ArrayList<>();
		int totalCount=searchAllUser(key).size();//获得总量
		Double t= Math.ceil(totalCount/pageSize);//获得总页数
		int totalPage=t.intValue();
		if (t<1) {
			totalPage=1;
		}
		int lastIndex=(totalPage-1)*pageSize;//获得尾页索引
		return	getPageBean(totalPage, totalCount, totalPage, lastIndex, pageSize, list,key);
	}


 
 
}
