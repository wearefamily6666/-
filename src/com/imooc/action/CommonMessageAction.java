package com.imooc.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.imooc.pojo.Chat_Group;
import com.imooc.pojo.Common_Message;
import com.imooc.pojo.User;
import com.imooc.service.IChatGroupService;
import com.imooc.service.ICommonMessageService;
import com.imooc.service.IUserService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 普通消息实体action
 * @author Administrator
 *
 */
public class CommonMessageAction extends ActionSupport implements ModelDriven<Common_Message>{
	private Common_Message commonMessage=new Common_Message();
	private List<HashMap<String, Object>> list;
	 private IUserService userService;
	 private IChatGroupService chatGroupService;
	@Override
	public Common_Message getModel() {
		// TODO Auto-generated method stub
		return commonMessage;
	}
	private ICommonMessageService commonMessageService;
	
	public void setCommonMessageService(ICommonMessageService commonMessageService) {
		this.commonMessageService = commonMessageService;
	}
	
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setChatGroupService(IChatGroupService chatGroupService) {
		this.chatGroupService = chatGroupService;
	}
	
	public List<HashMap<String, Object>> getList() {
		return list;
	}

	//获取输出对象
	public static PrintWriter getInstance() throws IOException {
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		 PrintWriter printWriter=response.getWriter();
		 return printWriter;
	}
	//根据用户id查找新的普通消息,并把消息改为已读,最新消息置于上方
	public String findByUserId() {
	User user=	(User) ActionContext.getContext().getSession().get("existUser");
	 list= commonMessageService.findByUserId(user.getUser_id());
	if (list==null) {
		list=new ArrayList<>();
	}
	return "findByUserId";
	}
	//显示普通消息列表
	public String information() {
		return "information";
	}
	

}
