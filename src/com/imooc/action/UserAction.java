package com.imooc.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.imooc.pojo.User;
import com.imooc.pojo.User_Group_Relation;
import com.imooc.service.IChatGroupService;
import com.imooc.service.IUserService;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
/**
 * User实体action
 * @author Administrator
 *
 */
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.util.ValueStack;
public class UserAction extends ActionSupport implements ModelDriven<User>{
	private User user=new User();
	private File some;
	private String someFileName;
	
	private long group_id,group_id2;
	private IChatGroupService chatGroupService;
	private Set<User> users;
	//获取输出对象
	public static PrintWriter getInstance() throws IOException {
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		 PrintWriter printWriter=response.getWriter();
		 return printWriter;
	}

	public void setSome(File some) {
		this.some = some;
	}
public void setSomeFileName(String someFileName) {
	this.someFileName = someFileName;
}
	@Override
	public User getModel() {
		// TODO Auto-generated method stub
		return user;
	}
	private IUserService userService;
	
	
	 
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setGroup_id(long group_id) {
		this.group_id = group_id;
	}

	public void setGroup_id2(long group_id2) {
		this.group_id2 = group_id2;
	}

	public void setChatGroupService(IChatGroupService chatGroupService) {
		this.chatGroupService = chatGroupService;
	}

	public Set<User> getUsers() {
		return users;
	}

 

	//登录验证
	public void loginvalidate() throws IOException { 
			 
			 if (user.getUser_account()!=null&&!"".equals(user.getUser_account())) {
				  
				 user=userService.varifyLogin(user); 
				 if (user!=null) {
					 ActionContext.getContext().getSession().put("existUser", user);
					 getInstance().write("true");
				}else {
					getInstance().write("false");
					
				}
				 getInstance().close();
			}
	}
	//登录页
	public String login() {
		return "login";
	}
//	登录成功
	public String loginSuccess() {
		user=(User) ActionContext.getContext().getSession().get("existUser");
		userService.loginSuccess(user);
		ActionContext.getContext().getSession().put("loginTime", new Date());
		 return "loginSuccess";
	}
	//退出登录
	public String logout() {
		System.out.println("======================================================================================logout");
	 user=	 (User) ActionContext.getContext().getSession().get("existUser");
		 if (user!=null) {
			 ActionContext.getContext().getSession().remove("existUser");
		}else {
			return "login";
		}
		 try {
			 userService.updateUser(user); 
			 return "login";
		} catch (Exception e) {
			// TODO: handle exception
			 return "loginSuccess";
		}
	}
	//修改个人资料
	public String modifyUser() {
		user=userService.findUserByUserId(user.getUser_id());
		return "modifyUser";
	}
//	修改个人资料成功
	public void modifyUserSuccess() { 
		try { 
		String str=	UUID.randomUUID()+"";
			User user2=	 (User) ActionContext.getContext().getSession().get("existUser");
			ImageAction.doUpLoad(1,  some,str);
			userService.modifyUser(user,str,user2.getUser_id()); 
			
			getInstance().print(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				getInstance().print(false);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			
		}
	}
	//邀请新成员(显示其他成员)
	public String invitation2() {
		//找出不在group_id2群中的group_id群成员
		users=chatGroupService.findAllNewer(group_id,group_id2);
		if (users==null) {
			users=new HashSet<>();
		}
		return "invitation2";
	}
}