package com.imooc.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.imooc.dao.ICommonMessageDao;
import com.imooc.pojo.Chat_Group;
import com.imooc.pojo.Common_Message;
import com.imooc.pojo.User;
/**
 * 普通消息实体dao实现类
 * @author Administrator
 *
 */
public class CommonMessageDaoImpl extends HibernateDaoSupport implements ICommonMessageDao {

	//根据用户id,是否阅读查询
	@Override
	public List<Common_Message> findByUserId(long user_id) {
		 DetachedCriteria criteria=DetachedCriteria.forClass(Common_Message.class)
				 .add(Restrictions.eq("receiver_id", user_id))
				 .add(Restrictions.eq("message_read", false));
		List<Common_Message> list= (List<Common_Message>) this.getHibernateTemplate().findByCriteria(criteria);
		return list;
	}
	//根据用户id查询所有消息
	@Override
	public List<Common_Message> findAllByUserId(long user_id) {
		 DetachedCriteria criteria=DetachedCriteria.forClass(Common_Message.class)
				 .add(Restrictions.eq("receiver_id", user_id));
		List<Common_Message> list= (List<Common_Message>) this.getHibernateTemplate().findByCriteria(criteria);
		return list;
	}
	//根据群id查群
	@Override
	public Chat_Group findUserIdByGroupId(long group_id) {
		Chat_Group chatGroup= this.getHibernateTemplate().get(Chat_Group.class, group_id);
		return chatGroup;
	}

	//根据用户id查用户
	@Override
	public User findUserByUserId(long sender_id) {
	User user=	this.getHibernateTemplate().get(User.class, sender_id);
		return user;
	}
	//插入数据
	@Override
	public void save(Common_Message common_Message) {
		// TODO Auto-generated method stub
		this.getHibernateTemplate().save(common_Message);
	}

	@Override
	public Common_Message findCommonMessageById(long commonMessage_id) {
		Common_Message common_Message=	this.getHibernateTemplate().get(Common_Message.class, commonMessage_id);
		return common_Message;
	}
	
	@Override
	public void update(Common_Message common_Message) {
		this.getHibernateTemplate().update(common_Message);
		
	}

}
