package com.imooc.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.imooc.pojo.Admin;
import com.imooc.pojo.Chat_Group;
import com.imooc.pojo.PageBean;
import com.imooc.pojo.User;
import com.imooc.service.IAdminService;
import com.imooc.service.IChatGroupService;
import com.imooc.service.IUserService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 管理员类action
 * @author Administrator
 *
 */
public class AdminAction extends ActionSupport implements ModelDriven<Admin>{
	private IUserService userService;
	private IChatGroupService chatGroupService;
	private HashSet<HashMap<String, Object>> maps;
	private IAdminService adminService;
	private Admin admin=new Admin();
	 private long user_id,group_id;
	private HashSet<Boolean> set;
	private String key;
	private int currentPage,pageSize;
	private PageBean<HashMap<String, Object>> pageBean;
	//获取输出对象
	public static PrintWriter getInstance() throws IOException {
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		 PrintWriter printWriter=response.getWriter();
		 return printWriter;
	}

	@Override
	public Admin getModel() {
		// TODO Auto-generated method stub
		return admin;
	}
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	public void setChatGroupService(IChatGroupService chatGroupService) {
		this.chatGroupService = chatGroupService;
	}
	
	public HashSet<HashMap<String, Object>> getMaps() {
		return maps;
	}
	
	public void setAdminService(IAdminService adminService) {
		this.adminService = adminService;
	}
	
 
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public void setGroup_id(long group_id) {
		this.group_id = group_id;
	}

	public HashSet<Boolean> getSet() {
		return set;
	}

	public void setKey(String key) {
		this.key = key;
	}
 

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}



	public PageBean<HashMap<String, Object>> getPageBean() {
		return pageBean;
	}

	//管理员管理用户
	public String userManager() {
		maps=userService.findAllUser();
		if (maps==null) {
			maps=new HashSet<>();
		}
		return "userManager";
	}
	//管理员管理所有群
	public String groupManager() {
		maps=(HashSet<HashMap<String, Object>>) chatGroupService.findAllGroup();
		if (maps==null) {
			maps=new HashSet<>();
		}
		return "groupManager";
	}
	//管理员登录界面
	public String login() {
		return "login";
	}
	public void loginvalidate() {
		try {
			Map<String, Object> map=	ActionContext.getContext().getSession();
			if (map.get("existAdmin")!=null) {
				getInstance().write("true");
				return;
			} 
			 admin=	adminService.validateLogin(admin.getAdmin_account(),admin.getAdmin_password());//到管理员表查询
			if (admin!=null) {
				map.put("existAdmin", admin);
				getInstance().write("true");
				return;
			}else {
				getInstance().write("false");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	//管理员登录成功界面
	public String loginSuccess() {
		return "loginSuccess";
	}
	//是否禁止用户登录
	public String userForbidden() {
		set=userService.modifyUser(user_id);
		if (set==null) {
			set=new HashSet<>();
		}
		return "user_forbidden";
	}
	//是否禁群
	public String groupForbidden() {
		set=	chatGroupService.modifyChatGroup(group_id);
		if (set==null) {
			set=new HashSet<>();
		}
		return "group_forbidden";
	}
	//用户搜索监听
	public String userSearch() {
		maps=userService.findAllUserByKey(key);
		if (maps==null) {
			maps=new HashSet<>();
		}
		return "userSearch";
	}
	//用户搜索监听
	public String groupSearch() {
		maps=chatGroupService.findAllGroupByKey(key);
		if (maps==null) {
			maps=new HashSet<>();
		}
		return "groupSearch";
	}
	//分页功能（针对群表）
	//根据页数和每页数量查询群列表
	public String findGroupByPage() {
		pageBean=	chatGroupService.findGroupByPage(currentPage,pageSize);
		if (pageBean==null) {
			pageBean=new PageBean<>();
		}
		return "findUserByPage";
	}
	//根据首页和每页数量查询群列表
	public String findGroupByFirstPage() {
		pageBean=	chatGroupService.findGroupByFirstPage(pageSize);
		if (pageBean==null) {
			pageBean=new PageBean<>();
		}
		return "findUserByFirstPage";
	}
	//根据尾页和每页数量查询群列表
	public String findGroupByLastPage() {
		pageBean=	chatGroupService.findGroupByLastPage(pageSize);
		if (pageBean==null) {
			pageBean=new PageBean<>();
		}
		return "findUserByLastPage";
	}
	//分页功能（针对用户表）
	//根据页数和每页数量查询用户列表
		public String findUserByPage() {
			pageBean=	userService.findUserByPage(currentPage,pageSize);
			if (pageBean==null) {
				pageBean=new PageBean<>();
			}
			return "findUserByPage";
		}
		//根据首页和每页数量查询用户列表
		public String findUserByFirstPage() {
			pageBean=	userService.findUserByFirstPage(pageSize);
			if (pageBean==null) {
				pageBean=new PageBean<>();
			}
			return "findUserByFirstPage";
		}
		//根据尾页和每页数量查询用户列表
		public String findUserByLastPage() {
			pageBean=	userService.findUserByLastPage(pageSize);
			if (pageBean==null) {
				pageBean=new PageBean<>();
			}
			return "findUserByLastPage";
		}
 
		//搜索中的分页功能（针对用户表）
/*		userFirst: "http://localhost:8080/The_vertical_and_horizontal/admin_searchUserByFirstPage",
		userLast: "http://localhost:8080/The_vertical_and_horizontal/admin_searchUserByLastPage",
		userCurrentPage: "http://localhost:8080/The_vertical_and_horizontal/admin_searchUserByPage",
		findUserTotalPage:"http://localhost:8080/The_vertical_and_horizontal/admin_searchUserTotalPage"*/
		//根据关键词，页数和每页数量查询用户列表
		public String searchUserByPage() {
			pageBean=	userService.searchUserByPage(currentPage,pageSize,key);
			if (pageBean==null) {
				pageBean=new PageBean<>();
			}
			return "searchUserByPage";
		}
		//根据关键词，首页和每页数量查询用户列表
		public String searchUserByFirstPage() {
			pageBean=	userService.searchUserByFirstPage(pageSize,key);
			if (pageBean==null) {
				pageBean=new PageBean<>();
			}
			return "searchUserByFirstPage";
		}
		//根据关键词，尾页和每页数量查询用户列表
		public String searchUserByLastPage() {
			pageBean=	userService.searchUserByLastPage(pageSize,key);
			if (pageBean==null) {
				pageBean=new PageBean<>();
			}
			return "searchUserByLastPage";
		}
 //搜索中的分页功能（针对群表）
		//根据关键词，页数和每页数量查询群表
		public String searchGroupByPage() {
			pageBean=	chatGroupService.searchGroupByPage(currentPage,pageSize,key);
			if (pageBean==null) {
				pageBean=new PageBean<>();
			}
			return "searchGroupByPage";
		}
		//根据关键词，首页和每页数量查询群列表
		public String searchGroupByFirstPage() {
			pageBean=	chatGroupService.searchGroupByFirstPage(pageSize,key);
			if (pageBean==null) {
				pageBean=new PageBean<>();
			}
			return "searchGroupByFirstPage";
		}
		//根据关键词，尾页和每页数量查询群表
		public String searchGroupByLastPage() {
			pageBean=	chatGroupService.searchGroupByLastPage(pageSize,key);
			if (pageBean==null) {
				pageBean=new PageBean<>();
			}
			return "searchGroupByLastPage";
		}
}
