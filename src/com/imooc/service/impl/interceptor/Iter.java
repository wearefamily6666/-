package com.imooc.service.impl.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.imooc.action.AdminAction;
import com.imooc.action.UserAction;
import com.imooc.pojo.Admin;
import com.imooc.pojo.User;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.sun.org.apache.bcel.internal.generic.NEW;
 
public class Iter extends MethodFilterInterceptor {
 
	@Override
	public String doIntercept(ActionInvocation invocation) throws Exception {
		System.out.println("--intercept()--");
		Action action=(Action) invocation.getAction();System.out.println(action);
		//获取相应的Session
		Map<String,Object> session=invocation.getInvocationContext().getSession(); 
		 if (action instanceof AdminAction) {
			Admin admin=(Admin) session.get("existAdmin");
			if (admin!=null) {
				return invocation.invoke();  
			}else {
				return "login2";
			}
			
		} else  {
			User user=(User) session.get("existUser");
			if (user!=null) {
				return invocation.invoke();  
			}else {
				return "login1";
			}
		} 
	}
}
