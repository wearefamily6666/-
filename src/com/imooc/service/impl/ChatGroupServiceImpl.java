package com.imooc.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.acl.Group;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.descriptor.sql.SmallIntTypeDescriptor;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.classmate.util.ResolvedTypeCache;
import com.fasterxml.classmate.util.ResolvedTypeCache.Key;
import com.imooc.dao.IChatGroupDao;
import com.imooc.dao.IChatRecordDao;
import com.imooc.dao.ICommonMessageDao;
import com.imooc.dao.IUserDao;
import com.imooc.pojo.Chat_Group;
import com.imooc.pojo.Chat_Record;
import com.imooc.pojo.Common_Message;
import com.imooc.pojo.PageBean;
import com.imooc.pojo.User;
import com.imooc.pojo.User_Group_Relation;
import com.imooc.service.IChatGroupService;
import com.mysql.fabric.xmlrpc.base.Data;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.sun.org.apache.bcel.internal.generic.NEW;

import jdk.nashorn.internal.runtime.linker.LinkerCallSite;

/**
 * ChatGroup业务层实现类
 * 
 * @author Administrator
 *
 */
@Transactional
public class ChatGroupServiceImpl implements IChatGroupService {
	IChatGroupDao chatGroupDao;
	IUserDao userDao;
	private ICommonMessageDao commonMessageDao;

	public void setChatGroupDao(IChatGroupDao chatGroupDao) {
		this.chatGroupDao = chatGroupDao;
	}

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	public void setCommonMessageDao(ICommonMessageDao commonMessageDao) {
		this.commonMessageDao = commonMessageDao;
	}

	// 获取输出对象
	public static PrintWriter getInstance() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter printWriter = response.getWriter();
		return printWriter;
	}

	@Override
	// 搜索处理(针对所有未加入群)
	public Set<Chat_Group> findAllNotJoinByKey(String key) {
		// TODO Auto-generated method stub
		Set<Chat_Group> set = null;
		// 当搜索框没内容时
		if (key == null || "".equals(key)) {
			// list转set
			// 因为List和Set都实现了Collection接口，且addAll(Collection<? extends E>
			// c);方法，因此可以采用addAll()方法将List和Set互相转换；另外，List和Set也提供了Collection<? extends E>
			// c作为参数的构造函数，
			set = new HashSet<>(findAllNotJoin());
			return set;
		}
		if (key != null && !"".equals(key)) {
			set = chatGroupDao.findAllNotJoinByKey(key, new HashSet<>(findAllNotJoin()));
			if (set != null && set.size() > 0) {
				return set;
			}
		}
		return null;
	}

	@Override
	// 创建群
	public void save(Chat_Group chatGroup, String string) {
		// TODO Auto-generated method stub

		if (chatGroup == null || "".equals(chatGroup.getGroup_name().trim())) {
			return;
		}
		User user = (User) ActionContext.getContext().getSession().get("existUser");
		chatGroup.setGroup_image(
				"http://localhost:8080/The_vertical_and_horizontal/image_imageInteface?groupHeader=" + string);
		User user2 = userDao.findUserByUserId(user.getUser_id());
		chatGroupDao.save(chatGroup, user2);
	}

	@Override
	// 查询已加入群，所有消息，新消息数(此处指上次退登到本次登录)
	public HashMap<String, Object> findAllJoined() {
		// TODO Auto-generated method stub
		User user = (User) ServletActionContext.getContext().getSession().get("existUser");
		User user2 = userDao.findUserByUserId(user.getUser_id());
		// 根据用户查询群聊消息
		Set<Chat_Group> chat_Groups = chatGroupDao.findChatGroups(user2);
		Iterator<Chat_Group> iterator = chat_Groups.iterator();
		List<HashMap<String, Object>> newsList = new ArrayList<HashMap<String, Object>>();
		while (iterator.hasNext()) {
			Chat_Group chat_Group = iterator.next();
			HashMap<String, Object> map = new HashMap<>();
			map.put("group_image", chat_Group.getGroup_image());
			map.put("group_name", chat_Group.getGroup_name());
			Set<Chat_Record> chat_Records = chat_Group.getChat_Records();
			// set转数组
			Chat_Record objects[] = new Chat_Record[chat_Records.size()];
			Chat_Record[] cha = chat_Records.toArray(objects);

			if (chat_Records.size() > 0) {
				User user3 = userDao.findUserByUserId(cha[chat_Records.size() - 1].getUser_id());
				map.put("whocontent",
						user3.getUser_account() + " : " + cha[chat_Records.size() - 1].getRecord_content());
				map.put("time", String.valueOf(LocalTime.now().withNano(0)));
				// 记录新消息数量（范围：上次退登到本次登录）
				Date loginTime = (Date) ActionContext.getContext().getSession().get("loginTime");
				int newsCount = 0;
				for (Chat_Record chat_Record : cha) {
					if (chat_Record.getRecord_send_time().compareTo(user2.getUser_last_login_time()) >= 0
							&& chat_Record.getRecord_send_time().compareTo(loginTime) <= 0) {
						newsCount++;
					}
				}
				map.put("newsCount", newsCount);
				map.put("group_id", chat_Group.getGroup_id());
				newsList.add(map);
			}
		}
		// 根据用户查询普通消息
		Set<Common_Message> set=user2.getCommon_Messages();
		ArrayList<Common_Message> arrayList=new ArrayList<>(set);
		int count=0;
		 for (Common_Message common_Message : set) {
			 if (!common_Message.isMessage_read()) {
				count++;
			}
		} 
		 //最后一条消息
		 if (arrayList.size()>0) {
			 Common_Message common_Message=arrayList.get(arrayList.size()-1);
			 HashMap<String, Object> map=new HashMap<>();
			 Chat_Group chat_Group=chatGroupDao.findDetailByGroupId(common_Message.getGroup_id());
			 map.put("group_image", chat_Group.getGroup_image());
			 map.put("group_name", chat_Group.getGroup_name());
			 map.put("whocontent", common_Message.getMessage_content());
			 //判断时间
			 Date date=common_Message.getMessage_send_time();
			 try {
				map.put("time", getTime(date));
				map.put("newsCount", count);
				map.put("group_id", chat_Group.getGroup_id());
				map.put("commonMessage","commonMessage" );
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			newsList.add(map); 	 
		 }

		// 将已加入群，新消息加到集合中
		HashMap<String, Object> lastMap = new HashMap<>();
		lastMap.put("groupList",  chat_Groups);
		lastMap.put("newsList", newsList);
		
		return lastMap;
	}
	//判断时间轴
	//1:昨天
	//2:至少前2天
	public static   String getTime(Date now) throws ParseException {
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat simpleDateFormat2=new SimpleDateFormat("HH:mm:ss");
 
		//一天24*60*60*1000=86400000
		if (now.getTime()>new Date().getTime()) {
			if (now.getTime()-new Date().getTime()<=86400000) {
				return "昨天";
			}else {
				return simpleDateFormat.format(now);
			}
		} 
		return simpleDateFormat2.format(now)+"";
		
	}
	@Override
	public Set<Chat_Group> findAllJoinedGroup() {
		User user = (User) ServletActionContext.getContext().getSession().get("existUser");
		User user2 = userDao.findUserByUserId(user.getUser_id());
		return user2.getChat_Groups();
	}

	@Override
	// 搜索处理(针对所有已加入群)
	public Set<Chat_Group> findAllJoinByKey(String key) {
		Set<Chat_Group> set = null;
		// 当搜索框没内容时
		if (key == null || "".equals(key)) {
			set = findAllJoinedGroup();
			return set;
		}
		// 当搜索框有内容时
		if (key != null && !"".equals(key)) {
			set = chatGroupDao.findAllJoinedByKey(key);
			if (set != null && set.size() > 0) {
				return set;
			}
		}
		return null;
	}

	@Override
	// 查询所有未加入群
	public List<Chat_Group> findAllNotJoin() {
		User user = (User) ActionContext.getContext().getSession().get("existUser");
		User user2 = userDao.findUserByUserId(user.getUser_id());
		// 获得所有已加入的群
		Set<Chat_Group> set = user2.getChat_Groups();
		// 在所有群中过滤
		List<Chat_Group> list = chatGroupDao.findAllGroup();
		
		for (Chat_Group chat_Group2 : set) {
			list.remove(chat_Group2);
		}
		return list;
	}

	// 查询群详情
	@Override
	public Chat_Group findDetailByGroupId(long group_id) {
		Chat_Group chatGroup = chatGroupDao.findDetailByGroupId(group_id);
		return chatGroup;
	}
	//邀请新成员(显示非group_id,并且有非group_id成员的群)
	@Override
	public Set<Chat_Group> findAllNewGroup(long group_id) {
		// 先找出所有非group_id的群
		List<Chat_Group> list = chatGroupDao.findAllNewGroup(group_id);
		// 去掉重复的group_id
		HashSet<Long> set2 = new HashSet<>();
		for (Chat_Group chat_Group : list) {
			set2.add(chat_Group.getGroup_id());
		}
		// 根据group_id查找群
		Chat_Group chat_Group=chatGroupDao.findDetailByGroupId(group_id);
		HashSet<Chat_Group> set3 = new HashSet<>();
		//排除只有group_id成员的群
		Iterator<Long> iterator2 = set2.iterator();
		while (iterator2.hasNext()) {
			Long long1 = (Long) iterator2.next();
			Chat_Group chat_Group2 = chatGroupDao.findDetailByGroupId(long1);
			int index=0;
			for (User user2 : chat_Group2.getUsers()) {
				for (User user1 : chat_Group.getUsers()) {
					if (user2.getUser_id()==user1.getUser_id()) {
						index++;
					}
				}
			}
			if (index<chat_Group2.getUsers().size()) {
				set3.add(chat_Group2);
			}
		}
		return set3;
	}
	// 找出不在group_id2群中的group_id群成员
	@Override
	public Set<User> findAllNewer(long group_id, long group_id2) {
		//查询group_id所有成员
		Chat_Group chat_Group=chatGroupDao.findDetailByGroupId(group_id);
		//查询group_id2所有成员
		Chat_Group chat_Group2=chatGroupDao.findDetailByGroupId(group_id2);
		//移除group_id中的group_id2成员，最终添加到users集合
		Set<User> users1=chat_Group.getUsers();
		Set<User> users2=chat_Group2.getUsers();
		Set<User> users=new HashSet<>();
		for (User user : users1) {
			boolean flag=false;
			for (User user2 : users2) {
				 if (user.getUser_id()==user2.getUser_id()) {
					flag=true;
				}
			}
			if (!flag) {
				users.add(user);
			}
		}
		//移除集合users中被禁用的成员
		 Set<User> set=new HashSet<>();
		 for (User user : users) {
			if (!user.getUser_forbidden()) {
				set.add(user);
			}
		}
		return set;
	}

	// 搜索处理（针对邀请页面的其他群）
	// 显示所有被搜群条件（1.搜索框没内容，2.开始加载时）
	@Override
	public Set<Chat_Group> findAllNewGroupByKey(String key, Set<Chat_Group> set) {
		Set<Chat_Group> set2 = new HashSet<>();
		Iterator<Chat_Group> iterator = set.iterator();
		// 搜索处理
		// 搜索框没内容
		if (key.equals("")||key==null) {
			return set;
		}
		while (iterator.hasNext()) {
			Chat_Group chat_Group=iterator.next();
			String group_name = chat_Group.getGroup_name();
			if (group_name.contains(key)) {
				set2.add(chat_Group);
			}
		}
		return set2;
	}

	// 查询群成员（不包含群主）
	@Override
	public Set<HashMap<String, Object>> findAllMemberByGroupId(long group_id) {
		Set<User> users = chatGroupDao.findAllMemberByGroupId(group_id);
		if (users != null && users.size() > 0) {
			Set<HashMap<String, Object>> maps = new HashSet<>();
			for (User user : users) {
				HashMap<String, Object> map = new HashMap<>();
				map.put("user_image", user.getUser_image());
				map.put("user_account", user.getUser_account());
				maps.add(map);
			}
			if (maps.size() > 0) {
				return maps;
			}
		}
		return null;
	}

	// 搜索群成员（不包含群主：移除成员界面）
	@Override
	public Set<HashMap<String, Object>> searchMemberInRemovePage(long group_id, String key) {
		Set<HashMap<String, Object>> set = findAllMemberByGroupId(group_id);
		// 当搜索框为空，或为""时,显示所有成员
		if (key == null || key.trim().equals("")) {
			set = findAllMemberByGroupId(group_id);
			return set;
		}
		Set<HashMap<String, Object>> set2 = new HashSet<>();
		for (HashMap<String, Object> map : set) {
			if (String.valueOf(map.get("user_account")).contains(key)) {
				set2.add(map);
			}
		}
		return set2;
	}

	// 移除群成员
	@Override
	public Common_Message removeClick(String user_account, long group_id) {
		User user2 = userDao.findUserByUserAccount(user_account);
		// 在表user_group_relation中删除数据

		chatGroupDao.removeUserFromGroup(user2.getUser_id(), group_id);
		// 插入数据到commonMessage

		Common_Message common_Message = new Common_Message();
		common_Message.setGroup_id(group_id);
		// 标题格式：你被移出群了
		Chat_Group chat_Group = chatGroupDao.findDetailByGroupId(group_id);
		common_Message.setMessage_content("你被移出" + chat_Group.getGroup_name());
		common_Message.setMessage_title("你被移出" + chat_Group.getGroup_name());
		common_Message.setMessage_type(1);
		common_Message.setMessage_receive(true);
		common_Message.setSender_id(chat_Group.getUser_id());

		common_Message.setReceiver_id(user2.getUser_id());
		common_Message.setMessage_read(false);
		common_Message.setMessage_send_time(new Date());
		commonMessageDao.save(common_Message);
		return common_Message;
	}

	// 退群或者解散群
	@Override
	public Common_Message exitGroup(long user_id, long group_id) {
		// 判断是否为群主（先查再删）
		User user = userDao.findUserByUserId(user_id);
		Chat_Group chat_Group = chatGroupDao.findDetailByGroupId(group_id);
		// 插入数据到commonMessage
					// 标题格式：1.张三退出群了（发给每一位普通群成员）2.张三解散群了（发给每一位普通群成员）
					Common_Message common_Message = new Common_Message();
					common_Message.setMessage_receive(true);
		if (chat_Group.getUser_id() == user_id) {
			chatGroupDao.removeGroup(user,chat_Group);
			common_Message.setMessage_content(user.getUser_account() + "解散" + chat_Group.getGroup_name() + "了");
			common_Message.setMessage_title(user.getUser_account() + "解散" + chat_Group.getGroup_name() + "了");
			common_Message.setMessage_type(1);
			common_Message.setSender_id(user_id);
			common_Message.setReceiver_id(user_id);//此处代表非接收人
			common_Message.setMessage_read(false);
			common_Message.setMessage_send_time(new Date());
			common_Message.setGroup_id(group_id);
			commonMessageDao.save(common_Message);
		} else {
			chatGroupDao.removeUserFromGroup(user_id, group_id);
			
			common_Message.setMessage_content(user.getUser_account() + "退出" + chat_Group.getGroup_name() + "了");
			common_Message.setMessage_title(user.getUser_account() + "退出" + chat_Group.getGroup_name() + "了");
			common_Message.setReceiver_id(chat_Group.getUser_id());
			common_Message.setMessage_type(1);
			common_Message.setSender_id(user_id);
			common_Message.setReceiver_id(user_id);//此处代表非接收人
			common_Message.setMessage_read(false);
			common_Message.setMessage_send_time(new Date());
			common_Message.setGroup_id(group_id);
			commonMessageDao.save(common_Message);
		}
		return common_Message;
	}

	// 获取成员经纬度，用户名，用户头像，时间
	@Override
	public Set<HashMap<String, Object>> getLocation(long group_id) {
		List<User> list = userDao.findAllUser();// 查找所有用户
		Set<HashMap<String, Object>> maps = new HashSet<>();
		// 随机生成经纬度
		Random random = new Random();
		User user1 = (User) ActionContext.getContext().getSession().get("existUser");
		for (User user : list) {
			if (user.getUser_account().equals(user1.getUser_account())) {
				continue;
			}
			HashMap<String, Object> map = new HashMap<>();

			double longtitude = random.nextInt(20) + 113.04455571986;
			double latitude = random.nextInt(30) + 23.165941076899;
			map.put("longtitude", longtitude);
			map.put("latitude", latitude);
			map.put("user_account", user.getUser_account());
			map.put("user_image", user.getUser_image());
			map.put("time", new Date());
			maps.add(map);
			System.out.println("longtitude:  " + longtitude + "latitude:  " + latitude);
		}
		// 113.04455571986,23.165941076899

		return maps;
	}

	// 查询所有群
	@Override
	public Set<HashMap<String, Object>> findAllGroup() {
		Set<HashMap<String, Object>> set = new HashSet<>();

		List<Chat_Group> chat_Groups = chatGroupDao.findAllGroup();
		for (Chat_Group chat_Group : chat_Groups) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("group_id", chat_Group.getGroup_id());
			map.put("group_name", chat_Group.getGroup_name());
			Set<User> users = chat_Group.getUsers();
			map.put("sum", users.size());
			int count = 0;// 保存在线人数
			for (User user : users) {
				if (user.getUser_online()) {
					count++;
				}
			}
			map.put("online_sum", count);
			if (chat_Group.isGroup_forbidden()) {
				map.put("group_forbidden", true);
			} else {
				map.put("group_forbidden", false);
			}
			set.add(map);
		}
		return set;
	}

	// 修改群（针对是否禁群）

	@Override
	public HashSet<Boolean> modifyChatGroup(long group_id) {
		HashSet<Boolean> set = chatGroupDao.modifyChatGroup(group_id);
		return set;
	}

	// 关键词查询所有群
	@Override
	public HashSet<HashMap<String, Object>> findAllGroupByKey(String key) {
		HashSet<HashMap<String, Object>> set = new HashSet<>();

		List<Chat_Group> chat_Groups = chatGroupDao.findAllGroupByKey(key);
		for (Chat_Group chat_Group : chat_Groups) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("group_id", chat_Group.getGroup_id());
			map.put("group_name", chat_Group.getGroup_name());
			Set<User> users = chat_Group.getUsers();
			map.put("sum", users.size());
			int count = 0;// 保存在线人数
			for (User user : users) {
				if (user.getUser_online()) {
					count++;
				}
			}
			map.put("online_sum", count);
			if (chat_Group.isGroup_forbidden()) {
				map.put("group_forbidden", true);
			} else {
				map.put("group_forbidden", false);
			}
			set.add(map);
		}
		return set;
	}

	// 封装pageBean
	public PageBean<HashMap<String, Object>> getPageBean(int currentPage, int totalCount, int totalPage, int beginIndex,
			int pageSize, List<HashMap<String, Object>> list) {
		PageBean<HashMap<String, Object>> pageBean = new PageBean<>();
		pageBean.setCurrentPage(currentPage);
		pageBean.setTotalCount(totalCount);
		pageBean.setTotalPage(totalPage);

		// 根据索引和每页数量查询
		List<Chat_Group> chat_Groups = chatGroupDao.findGroupByPage(beginIndex, pageSize);
		for (Chat_Group chat_Group : chat_Groups) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("group_id", chat_Group.getGroup_id());
			map.put("group_name", chat_Group.getGroup_name());
			Set<User> users = chat_Group.getUsers();
			map.put("sum", users.size());
			int count = 0;// 保存在线人数
			for (User user : users) {
				if (user.getUser_online()) {
					count++;
				}
			}
			map.put("online_sum", count);
			if (chat_Group.isGroup_forbidden()) {
				map.put("group_forbidden", true);
			} else {
				map.put("group_forbidden", false);
			}

			list.add(map);
		}
		pageBean.setList(list);
		return pageBean;
	}

	public PageBean<HashMap<String, Object>> getPageBean(int currentPage, int totalCount, int totalPage, int beginIndex,
			int pageSize, List<HashMap<String, Object>> list, String key) {
		PageBean<HashMap<String, Object>> pageBean = new PageBean<>();
		pageBean.setCurrentPage(currentPage);
		pageBean.setTotalCount(totalCount);
		pageBean.setTotalPage(totalPage);

		// 根据索引和每页数量查询
		List<Chat_Group> chat_Groups = chatGroupDao.searchGroupByKey(beginIndex, pageSize, key);
		for (Chat_Group chat_Group : chat_Groups) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("group_id", chat_Group.getGroup_id());
			map.put("group_name", chat_Group.getGroup_name());
			Set<User> users = chat_Group.getUsers();
			map.put("sum", users.size());
			int count = 0;// 保存在线人数
			for (User user : users) {
				if (user.getUser_online()) {
					count++;
				}
			}
			map.put("online_sum", count);
			if (chat_Group.isGroup_forbidden()) {
				map.put("group_forbidden", true);
			} else {
				map.put("group_forbidden", false);
			}

			list.add(map);
		}
		pageBean.setList(list);
		return pageBean;
	}

	// 根据当前页数和每页数量查询
	@Override
	public PageBean<HashMap<String, Object>> findGroupByPage(int currentPage, int pageSize) {
		List<HashMap<String, Object>> list = new ArrayList();
		int totalCount = findAllGroup().size();// 获得总量
		Double t = Math.ceil(totalCount / pageSize);// 获得总页数
		int totalPage = t.intValue();
		if (t < 1) {
			totalPage = 1;
		}
		int beginIndex = pageSize * currentPage - 1;// 获得开始索引

		return getPageBean(currentPage, totalCount, totalPage, beginIndex, pageSize, list);
	}

	// 根据首页和每页数量查询群
	@Override
	public PageBean<HashMap<String, Object>> findGroupByFirstPage(int pageSize) {
		List<HashMap<String, Object>> list = new ArrayList();
		int totalCount = findAllGroup().size();// 获得总量
		Double t = Math.ceil(totalCount / pageSize);// 获得总页数
		int totalPage = t.intValue();
		if (t < 1) {
			totalPage = 1;
		}
		return getPageBean(1, totalCount, totalPage, 0, pageSize, list);
	}

	// 根据尾页和每页条数查询
	@Override
	public PageBean<HashMap<String, Object>> findGroupByLastPage(int pageSize) {
		List<HashMap<String, Object>> list = new ArrayList();
		int totalCount = findAllGroup().size();// 获得总量
		Double t = Math.ceil(totalCount / pageSize);// 获得总页数
		int totalPage = t.intValue();
		if (t < 1) {
			totalPage = 1;
		}
		int lastIndex = (totalPage - 1) * pageSize;// 获得尾页索引
		return getPageBean(totalPage, totalCount, totalPage, lastIndex, pageSize, list);
	}

	// 根据关键词，页数和每页数量查询群表
	@Override
	public PageBean<HashMap<String, Object>> searchGroupByPage(int currentPage, int pageSize, String key) {
		List<HashMap<String, Object>> list = new ArrayList();
		int totalCount = findAllGroupByKey(key).size();// 获得总量
		Double t = Math.ceil(totalCount / pageSize);// 获得总页数
		int totalPage = t.intValue();
		if (t < 1) {
			totalPage = 1;
		}
		int beginIndex = pageSize * currentPage - 1;// 获得开始索引

		return getPageBean(currentPage, totalCount, totalPage, beginIndex, pageSize, list, key);
	}

	// 根据关键词，首页和每页数量查询群列表
	@Override
	public PageBean<HashMap<String, Object>> searchGroupByFirstPage(int pageSize, String key) {
		List<HashMap<String, Object>> list = new ArrayList();
		int totalCount = findAllGroupByKey(key).size();// 获得总量
		Double t = Math.ceil(totalCount / pageSize);// 获得总页数
		int totalPage = t.intValue();
		if (t < 1) {
			totalPage = 1;
		}
		return getPageBean(1, totalCount, totalPage, 0, pageSize, list, key);
	}

	// 根据关键词，尾页和每页数量查询群表
	@Override
	public PageBean<HashMap<String, Object>> searchGroupByLastPage(int pageSize, String key) {
		List<HashMap<String, Object>> list = new ArrayList();
		int totalCount = findAllGroupByKey(key).size();// 获得总量
		Double t = Math.ceil(totalCount / pageSize);// 获得总页数
		int totalPage = t.intValue();
		if (t < 1) {
			totalPage = 1;
		}
		int lastIndex = (totalPage - 1) * pageSize;// 获得尾页索引
		return getPageBean(totalPage, totalCount, totalPage, lastIndex, pageSize, list, key);
	}
	//添加成员到群
	@Override
	public void addGroupMember(long receiver_id, long group_id) {
		User_Group_Relation user_Group_Relation=new User_Group_Relation();
		user_Group_Relation.setUser_id(receiver_id);
		user_Group_Relation.setGroup_id(group_id);
		chatGroupDao.addGroupMember(user_Group_Relation);
	}
}
