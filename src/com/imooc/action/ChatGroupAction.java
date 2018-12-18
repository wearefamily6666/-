package com.imooc.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.jws.soap.SOAPBinding.Use;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.annotations.JSON;

import com.imooc.pojo.Chat_Group;
import com.imooc.pojo.User;
import com.imooc.service.IChatGroupService;
import com.imooc.service.IUserService;
import com.mysql.fabric.xmlrpc.base.Array;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * ChatGroup 实体action
 * 
 * @author Administrator
 *
 */
public class ChatGroupAction extends ActionSupport implements ModelDriven<Chat_Group> {
	private Chat_Group chatGroup = new Chat_Group();
	private String key;
	private Set<Chat_Group> set;
	private File some;
	private IUserService userService;
	private HashMap<String, Object> map=new HashMap<>();
	 private Set<HashMap<String, Object>> maps;
	@Override
	public Chat_Group getModel() {
		// TODO Auto-generated method stub
		return chatGroup;
	}
	IChatGroupService chatGroupService;
	private String user_account;
	private HashMap<String, Object> objects;

	public void setChatGroupService(IChatGroupService chatGroupService) {
		this.chatGroupService = chatGroupService;
	}

	public void setKey(String key) {
		this.key = key;
	}
	public Set<Chat_Group> getSet() {
		return set;
	}
	public void setSet(Set<Chat_Group> set) {
		this.set = set;
	}
	public void setSome(File some) {
		this.some = some;
	}
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	
	public HashMap<String, Object> getMap() {
		return map;
	}
 

	public Set<HashMap<String, Object>> getMaps() {
		return maps;
	}

	public void setUser_account(String user_account) {
		this.user_account = user_account;
	}
 

	public HashMap<String, Object> getObjects() {
		return objects;
	}

	// 获取输出对象
	public static PrintWriter getInstance() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8"); 
		PrintWriter printWriter=response.getWriter(); 
		return printWriter;
	}
	
	// 获得已经加入的群 条件（1.搜索框没内容，2.开始加载时）
	public String findAllJoined() {
		objects = chatGroupService.findAllJoined(); 
		if (objects == null) {
			objects = new HashMap<>();
		}
 	return "findAllJoined";
	}

	// 获得所有未加入群 条件（1.搜索框没内容，2.开始加载时）
	public String findAllNotJoin() {
		//list转set
		set = new HashSet<>(chatGroupService.findAllNotJoin());
		if (set == null) {
			set = new HashSet<Chat_Group>();
		}
		return "findAllNotJoin";
	}

	// 在已加入群搜索： 显示所有已加入群的条件（1.搜索框没内容，2.开始加载时）
	public String findAllJoinedByKey() {
		set = chatGroupService.findAllJoinByKey(key);
		if (set == null) {
			set = new HashSet();
		}
		return "findAllJoinedByKey";
	}

	// 在未加入群搜索： 显示所有未加入群条件（1.搜索框没内容，2.开始加载时）
	public String findAllNotJoinByKey() {
		set = chatGroupService.findAllNotJoinByKey(key);
		if (set == null) {
			set = new HashSet();
		}
		return "findAllNotJoinedByKey";
	}

	// 创建群成功
	public void createGroupSuccess() {
		try {
			// 插入数据
			String str = UUID.randomUUID() +"";
			ImageAction.doUpLoad(2,  some, str);
			chatGroupService.save(chatGroup, str);
			if (getInstance()!=null) {
				getInstance().close();
			}
			getInstance().print(true);
		} catch (Exception e) {
			// TODO: handle exception
			try {
				if (getInstance()!=null) {
					getInstance().close();
				}
				getInstance().print(false);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	// 根据群id查群详情(addGroup.jsp)
	public String findDetailByGroupId() {
		chatGroup = chatGroupService.findDetailByGroupId(chatGroup.getGroup_id());
		if (chatGroup==null) {
			chatGroup=new Chat_Group();
		}
		return "findDetailByGroupId";
	}
	//根据群id查群详情（communication.jsp）
	public String findDetailByGroupId2() {
		chatGroup = chatGroupService.findDetailByGroupId(chatGroup.getGroup_id());
		User user=userService.findUserByUserId(chatGroup.getUser_id());
		map.put("group_owner", user.getUser_account());
		map.put("group_notice", chatGroup.getGroup_notice());
		map.put("group_name", chatGroup.getGroup_name());
		map.put("group_id", chatGroup.getGroup_id());
		map.put("user_id", chatGroup.getUser_id());
		//判断操作者是否为群主
		user=(User) ActionContext.getContext().getSession().get("existUser");
		if (chatGroup.getUser_id()==user.getUser_id()) {
			map.put("isGroupOwner", true);
		}else {
			map.put("isGroupOwner", false);
		}
		return "findDetailByGroupId2";
	}
 
	//邀请新成员(显示非group_id,并且有非group_id成员的群)
	public String invitation() {
		set=chatGroupService.findAllNewGroup(chatGroup.getGroup_id());
		return "invitation";
	}
//	邀请页面搜索群，显示所有群条件（1.搜索框没内容，2.开始加载时）
	public String search() {
//		查找非group_id的群
		set=chatGroupService.findAllNewGroup(chatGroup.getGroup_id());
//		根据key搜索群
		set=chatGroupService.findAllNewGroupByKey(key,set);
		return "search";
	}
	//查询群成员(不包含群主：移除成员界面)
	public String removeMember() {
		maps=chatGroupService.findAllMemberByGroupId(chatGroup.getGroup_id());
		if (maps==null) {
			maps=new HashSet<>();
		}
		return "removeMember";
	}
	//搜索群成员（不包含群主：移除成员界面）
	public String search2() {
		maps=chatGroupService.searchMemberInRemovePage(chatGroup.getGroup_id(),key);
		if (maps==null) {
			maps=new HashSet<>();
		}
		return "search2";
	}

	
 
	//获取成员经纬度，用户名，用户头像，时间
	public String getLocation() {
		maps=chatGroupService.getLocation(chatGroup.getGroup_id());
		if (maps==null) {
			maps=new HashSet<>();
		}
		return "getLocation";
	}
	//返回创建群界面
	public String createGroup() {
		return "createGroup";
	}
	//返回加入群界面
	public String addGroup() {
		return "addGroup";
	}
	//返回详情界面
	public String information() {
		return "information";
	}
}
