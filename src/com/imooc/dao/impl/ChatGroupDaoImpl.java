package com.imooc.dao.impl;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.jws.soap.SOAPBinding.Use;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;import org.springframework.jdbc.object.UpdatableSqlQuery;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.imooc.dao.IChatGroupDao;
import com.imooc.pojo.Chat_Group;
import com.imooc.pojo.User;
import com.imooc.pojo.User_Group_Relation;
import com.imooc.service.ICommonMessageService;
import com.opensymphony.xwork2.ActionContext;

import sun.print.resources.serviceui;
/**
 * ChatGroup实体dao实现类
 * @author Administrator
 *
 */
public class ChatGroupDaoImpl extends HibernateDaoSupport implements IChatGroupDao {
 
	@Override
	//创建群
	public void save(Chat_Group chatGroup,User user) {
		// TODO Auto-generated method stub
			chatGroup.setUser_id(user.getUser_id()); //设置群主id
			 //自动生成10位数账号
			String string="";
			for (int i = 0; i < 10; i++) {
				string+=new Random().nextInt(10);
			}
			chatGroup.setGroup_account(string);
			chatGroup.setGroup_sum(1);
			chatGroup.setGroup_create_date(new Date());
			chatGroup.setGroup_forbidden(false);
			//级联保存
			user.getChat_Groups().add(chatGroup); 
			this.getHibernateTemplate().save(user);
	}
	//根据群主id查群
	private Chat_Group findByUserId(long user_id) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria=DetachedCriteria.forClass(Chat_Group.class)
				.add(Restrictions.eq("user_id", user_id))
				.add(Restrictions.eq("group_forbidden", false));
	List<Chat_Group> list=	(List<Chat_Group>) this.getHibernateTemplate().findByCriteria(detachedCriteria);
	
	if (list!=null&&list.size()>0) {
		return list.get(0);
	}
	return null;
	}
 

	//根据用户id 查询已加入群
	@Override
	public Set<Chat_Group> findChatGroups(User user) {
		Set<Chat_Group> set=user.getChat_Groups();
		return set;
	}

	@Override
//	根据关键词查询所有已加入群
	public Set<Chat_Group> findAllJoinedByKey(String key) {
		 key=key.trim();
		 User user=(User) ActionContext.getContext().getSession().get("existUser");
		User user2= this.getHibernateTemplate().get(User.class, user.getUser_id());
		 //根据用户id查所有已加入群
		Set<Chat_Group> set= findChatGroups(user2);
		//匹配关键词
		 Set<Chat_Group> set2=new HashSet();
		  for (Chat_Group chatGroup : set) {
			if (chatGroup.getGroup_name().contains(key)) {
				set2.add(chatGroup);
			}
		}
		  return set2;
	}


 
	@Override
	//根据群id查询群详情
	public Chat_Group findDetailByGroupId(long group_id) {
		Chat_Group chatGroup=this.getHibernateTemplate().get(Chat_Group.class, group_id);
		return chatGroup;
	}
	//查出不包含group_id群的群
	@Override
	public List<Chat_Group> findAllNewGroup(long group_id) {
		 DetachedCriteria detachedCriteria=DetachedCriteria.forClass(Chat_Group.class)
				 .add(Restrictions.ne("group_id", group_id))
				 .add(Restrictions.eq("group_forbidden", false));
        List<Chat_Group> list= (List<Chat_Group>) this.getHibernateTemplate().findByCriteria(detachedCriteria);
		return list;
	}
	//查出包含group_id群的群，用户关系
	@Override
	public List<Chat_Group> findAllNewer(long group_id) {
		 DetachedCriteria detachedCriteria=DetachedCriteria.forClass(Chat_Group.class)
				 .add(Restrictions.eq("group_id", group_id))
				 .add(Restrictions.eq("group_forbidden", false));
		 List<Chat_Group> list= (List<Chat_Group>) this.getHibernateTemplate().findByCriteria(detachedCriteria);
		return list;
	}
	
	//查询群成员(不包含群主)
	@Override
	public Set<User> findAllMemberByGroupId(long group_id) {
		//值栈获取群主
		User user=(User) ActionContext.getContext().getSession().get("existUser");
		Chat_Group chat_Group=this.getHibernateTemplate().get(Chat_Group.class, group_id);
		Set<User> set=chat_Group.getUsers();
		Set<User> users=new HashSet<>();
		for (User user2 : set) {
			if (user2.getUser_id()==user.getUser_id()) {
				continue;
			}else {
				users.add(user2);
			}
		}
		return users;
	}
	//在表user_group中操作
	@Override
	public void removeUserFromGroup(long user_id, long group_id) {
//		先查再删
		DetachedCriteria criteria=DetachedCriteria.forClass(User_Group_Relation.class)
				.add(Restrictions.eq("user_id", user_id))
				.add(Restrictions.eq("group_id", group_id));
		List<User_Group_Relation> list=(List<User_Group_Relation>) this.getHibernateTemplate().findByCriteria(criteria);
		if (list!=null&&list.size()>0) {
			this.getHibernateTemplate().delete(list.get(0));
		}
		//群成员减一
		Chat_Group chat_Group=this.getHibernateTemplate().get(Chat_Group.class, group_id);
		chat_Group.setGroup_sum(chat_Group.getGroup_sum()-1);
		this.getHibernateTemplate().update(chat_Group);
	}
	
	@Override
	public void delete(Chat_Group chat_Group) {
		this.getHibernateTemplate().delete(chat_Group);
	}
	//获取持久化数据再删除
	@Override
	public void deleteUserGroupRelation(long user_id, long group_id) {
		// TODO Auto-generated method stub
		DetachedCriteria criteria=DetachedCriteria.forClass(User_Group_Relation.class)
				.add(Restrictions.eq("user_id", user_id))
				.add(Restrictions.eq("group_id", group_id));
		List<User_Group_Relation> list=(List<User_Group_Relation>) this.getHibernateTemplate().findByCriteria(criteria);
		if (list!=null&&list.size()>0) {
			this.getHibernateTemplate().delete(list.get(0));
		}
	}
	//查询所有群
	@Override
	public List<Chat_Group> findAllGroup() {
		 DetachedCriteria criteria=DetachedCriteria.forClass(Chat_Group.class);
		 criteria.add(Restrictions.eq("group_forbidden", false));
		List<Chat_Group> chat_Groups=(List<Chat_Group>) this.getHibernateTemplate().findByCriteria(criteria);
		return chat_Groups;
	}
	//修改群（针对是否禁群）
	@Override
	public HashSet<Boolean> modifyChatGroup(long group_id) {
		HashSet<Boolean> set=new HashSet<>();
	Chat_Group chat_Group=	this.getHibernateTemplate().get(Chat_Group.class, group_id);
	if (chat_Group.isGroup_forbidden()) {
		chat_Group.setGroup_forbidden(false);
		set.add(false);
	}else {
		chat_Group.setGroup_forbidden(true);
		set.add(true);
	}
		this.getHibernateTemplate().update(chat_Group);
		return set;
	}
	//关键词查询所有群
	@Override
	public List<Chat_Group> findAllGroupByKey(String key) {
		
		DetachedCriteria criteria=DetachedCriteria.forClass(Chat_Group.class)
				.add(Restrictions.ilike("group_name", "%"+key+"%"))
				.add(Restrictions.eq("group_forbidden", false))
				.addOrder(Order.asc("group_id"));
		List<Chat_Group> list=(List<Chat_Group>) this.getHibernateTemplate().findByCriteria(criteria);
		return list;
	}
	//根据开始索引和每页数量查询群
	@Override
	public List<Chat_Group> findGroupByPage(int beginIndex, int pageSize) {
		DetachedCriteria criteria=DetachedCriteria.forClass(Chat_Group.class);
		criteria.addOrder(Order.asc("group_id"));
		criteria.add(Restrictions.eq("group_forbidden", false));
		List<Chat_Group> list=(List<Chat_Group>) this.getHibernateTemplate().findByCriteria(criteria, beginIndex, pageSize);
		return list;
	}
	//根据关键词，开始索引和每页数量查询群
	@Override
	public List<Chat_Group> searchGroupByKey(int beginIndex, int pageSize, String key) {
		DetachedCriteria criteria=DetachedCriteria.forClass(Chat_Group.class);
		criteria.add(Restrictions.ilike("group_name", "%"+key+"%"));
		criteria.add(Restrictions.eq("group_forbidden", false));
		criteria.addOrder(Order.asc("group_id"));
		List<Chat_Group> list=(List<Chat_Group>) this.getHibernateTemplate().findByCriteria(criteria, beginIndex, pageSize);
		return list;
	}
//	根据关键词查询所有未加入群
	@Override
	public Set<Chat_Group> findAllNotJoinByKey(String key,Set<Chat_Group> set) {
		// TODO Auto-generated method stub
		key=key.trim();
		//匹配关键词
		Set<Chat_Group> set2=new HashSet<Chat_Group>(); 
	Iterator<Chat_Group> iterator=	set.iterator();
	while (iterator.hasNext()) {
		Chat_Group chat_Group = (Chat_Group) iterator.next();
		System.out.println("                        群名：                                    "+chat_Group.getGroup_name());
		if (chat_Group.getGroup_name().contains(key)) {
			set2.add(chat_Group);
			}
		}
		return set2;
	}
	//解散群
	@Override
	public void removeGroup(User user, Chat_Group chat_Group) {
		//删除群与用户关联
		
		DetachedCriteria criteria=DetachedCriteria.forClass(User_Group_Relation.class);
		List<User_Group_Relation> list=(List<User_Group_Relation>) this.getHibernateTemplate().findByCriteria(criteria);
		for(int i=list.size()-1;i>=0;i--) {
			if (list.get(i).getGroup_id()==chat_Group.getGroup_id()) {
				this.getHibernateTemplate().delete(list.get(i));
			} 
		}
		//删除群
	
		this.getHibernateTemplate().delete(chat_Group);
	}
	
	//添加群成员
	@Override
	public void addGroupMember(User_Group_Relation user_Group_Relation) {
		this.getHibernateTemplate().save(user_Group_Relation);
		//群成员加一
		Chat_Group chat_Group=this.getHibernateTemplate().get(Chat_Group.class, user_Group_Relation.getGroup_id());
		chat_Group.setGroup_sum(chat_Group.getGroup_sum()+1);
		this.getHibernateTemplate().update(chat_Group);
	}
}
