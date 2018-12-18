package com.imooc.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.imooc.dao.IUserDao;
import com.imooc.pojo.Chat_Group;
import com.imooc.pojo.User;
import com.imooc.pojo.User_Group_Relation;

import net.bytebuddy.asm.Advice.This;
/**
 * 登录dao层实现类
 * @author Administrator
 *
 */
public class UserDaoImpl extends HibernateDaoSupport implements IUserDao {


	@Override
	public void updateUserByUserAccount(User user,boolean flag,int type) {
		// 登录、退登处理
		//type:1 登录  2退登
	user.setUser_online(flag);
	if (type==2) {
		user.setUser_last_login_time(new Date());
		user.setUser_online(false);
	}else {
		user.setUser_online(true);
	}
	 this.getHibernateTemplate().update(user);
	}
	
	@Override
	public User findByUserAccountAndPassword(String user_account, String user_password) {
		// 查询用户是否存在
		DetachedCriteria criteria=DetachedCriteria.forClass(User.class)
				.add(Restrictions.eq("user_forbidden", false))
				.add(Restrictions.eq("user_account", user_account))
				.add(Restrictions.eq("user_password", user_password));
		List<User> list= (List<User>) this.getHibernateTemplate().findByCriteria(criteria);
		if (list!=null&&list.size()>0) {
			return list.get(0);
		}
		return null;
	}
 

	//更新用户信息
	@Override
	public void modifyUser(User user) {

		this.getHibernateTemplate().update(user);
	}
	
	//根据用户名查找用户
	@Override
	public User findUserByUserAccount(String user_account) {
		// TODO Auto-generated method stub
	 DetachedCriteria detachedCriteria=DetachedCriteria.forClass(User.class)
			 .add(Restrictions.eq("user_forbidden", false))
			 .add(Restrictions.eq("user_account", user_account));
	List<User> list= (List<User>) this.getHibernateTemplate().findByCriteria(detachedCriteria);
	if (list!=null&&list.size()>0) {
		return list.get(0);
	}
	return null;
	}
	//根据用户id查用户
	@Override
	public User findUserByUserId(long user_id) {
		User user=	this.getHibernateTemplate().get(User.class, user_id);
		return user;
	}

	//查找所有用户
	@Override
	public List<User> findAllUser() {
		 DetachedCriteria criteria=DetachedCriteria.forClass(User.class);
		 criteria.add(Restrictions.eq("user_forbidden", false));
		 List<User> list=	(List<User>) this.getHibernateTemplate().findByCriteria(criteria);
		return list;
	}
	//修改个人资料（针对是否禁止用户登录）

	@Override
	public HashSet<Boolean> modifyUser(long user_id) {
		HashSet<Boolean> set=new HashSet<>();
		 //先查再改
		User user=	this.getHibernateTemplate().get(User.class, user_id);
		if (user.getUser_forbidden()) {
			user.setUser_forbidden(false);
			set.add(false);
		}else {
			user.setUser_forbidden(true);
			set.add(true);
		}
		this.getHibernateTemplate().update(user);
		return set;
	}

	//关键词搜索所有用户
	@Override
	public List<User> findAllUserByKey(String key) {
		
		DetachedCriteria criteria=DetachedCriteria.forClass(User.class)
				.add(Restrictions.ilike("user_account", "%"+key+"%"))
				.add(Restrictions.eq("user_forbidden", false))
				.addOrder(Order.asc("user_id"));
		List<User> list=(List<User>) this.getHibernateTemplate().findByCriteria(criteria);
		return list;
	}
//根据索引和每页数量查询
	@Override
	public List<User> findUserByPage(int beginIndex, int pageSize) {
		DetachedCriteria criteria=DetachedCriteria.forClass(User.class);
		criteria.add(Restrictions.eq("user_forbidden", false));
		criteria.addOrder(Order.asc("user_id"));
		List<User> list=(List<User>) this.getHibernateTemplate().findByCriteria(criteria, beginIndex, pageSize);
		return list;
	}
//根据关键词，索引和每页数量查询
	@Override
	public List<User> searchUserByPage(int beginIndex, int pageSize, String key) {
		DetachedCriteria criteria=DetachedCriteria.forClass(User.class);
		criteria.addOrder(Order.asc("user_id"));
		criteria.add(Restrictions.eq("user_forbidden", false));
		criteria.add(Restrictions.ilike("user_account", "%"+key+"%"));
		List<User> list=(List<User>) this.getHibernateTemplate().findByCriteria(criteria, beginIndex, pageSize);
		return list;
	}

 
}
