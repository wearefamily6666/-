package com.imooc.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.imooc.dao.IAdminDao;
import com.imooc.pojo.Admin;

/**
 * 管理员dao层实现类
 * @author Administrator
 *
 */
public class AdminDaoImpl extends HibernateDaoSupport implements IAdminDao {
//	用户名和密码查询管理员
	@Override
	public Admin findByAccountAndPassword(String user_account, String user_password) {
		
		DetachedCriteria criteria=DetachedCriteria.forClass(Admin.class)
				.add(Restrictions.eq("admin_account", user_account))
				.add(Restrictions.eq("admin_password", user_password));
		List<Admin> list=  (List<Admin>) this.getHibernateTemplate().findByCriteria(criteria);
		if (list!=null&&list.size()>0) {
			return list.get(0);
		}
		return null;
	}

}
