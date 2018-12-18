package com.imooc.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import com.imooc.dao.IChatGroupDao;
import com.imooc.dao.ICommonMessageDao;
import com.imooc.dao.IUserDao;
import com.imooc.pojo.Chat_Group;
import com.imooc.pojo.Common_Message;
import com.imooc.pojo.User;
import com.imooc.service.ICommonMessageService;
import com.imooc.service.IUserService;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
/**
 * 普通消息实体业务层实现类
 * @author Administrator
 *
 */
@Transactional
public class CommonMessageServiceImpl implements ICommonMessageService {
	private ICommonMessageDao commonMessageDao;
	private IChatGroupDao chatGroupDao;
	
	public void setCommonMessageDao(ICommonMessageDao commonMessageDao) {
		this.commonMessageDao = commonMessageDao;
	}
	//根据用户id查新的普通消息
	public void setChatGroupDao(IChatGroupDao chatGroupDao) {
		this.chatGroupDao = chatGroupDao;
	}
	@Override
	public List<HashMap<String, Object>> findByUserId(long user_id) {
		List<Common_Message> list= commonMessageDao.findAllByUserId(user_id);
		List<HashMap<String, Object>> list2=new ArrayList<>();
		for (int i=list.size()-1;i>=0;i--) {
			Common_Message common_Message=list.get(i);
			//判断是否该收到消息
			if (common_Message.getMessage_content().contains("解散")||common_Message.getMessage_content().contains("退出")) {
				if (user_id==common_Message.getReceiver_id()) {
					continue;
				}
			}
			HashMap<String, Object> map=new HashMap<>();
			Chat_Group chat_Group=chatGroupDao.findDetailByGroupId(common_Message.getGroup_id());
			map.put("group_image", chat_Group.getGroup_image());
			map.put("group_name", chat_Group.getGroup_name());
			map.put("newitem", common_Message.getMessage_content());
			map.put("type", common_Message.getMessage_type());
			try {
				map.put("time", ChatGroupServiceImpl.getTime(common_Message.getMessage_send_time()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			map.put("commonMessage_id", common_Message.getMessage_id());
			map.put("message_receive", common_Message.isMessage_receive());
			list2.add(map);
			//把该消息改为已读
			common_Message.setMessage_read(true);
		}
		return list2;
	}

	//申请加群
	@Override
	public Common_Message applyToJoin(Common_Message commonMessage) {
		 commonMessage.setMessage_type(2);
		 commonMessage.setMessage_read(false);
		 commonMessage.setMessage_receive(false);
		 //根据群id查群
		 Chat_Group chatGroup=commonMessageDao.findUserIdByGroupId(commonMessage.getGroup_id());
		 commonMessage.setReceiver_id(chatGroup.getUser_id());//接受人为群主
		 commonMessage.setMessage_send_time(new Date());
		 //标题格式：用户申请加入群
		 //根据用户id查用户
		 User user=commonMessageDao.findUserByUserId(commonMessage.getSender_id());
		 commonMessage.setMessage_title(user.getUser_name()+"申请加入群"+chatGroup.getGroup_name());
		 commonMessage.setMessage_content(user.getUser_name()+"申请加入群"+chatGroup.getGroup_name());
		 commonMessage.setMessage_receive(false);
		commonMessageDao.save(commonMessage);
		return commonMessage;
	}

	//对发送邀请做处理
	@Override
	public Common_Message doInvite(User receiver,Chat_Group chat_Group,User sender) {
		 Common_Message common_Message=new Common_Message();
		 common_Message.setMessage_type(2);
		 common_Message.setMessage_read(false);
		 common_Message.setMessage_receive(false);
		 common_Message.setMessage_send_time(new Date());
		 //标题格式：用户邀请用户加入群
		 String reg=sender.getUser_account()+"邀请您加入"+chat_Group.getGroup_name();
		 common_Message.setMessage_title(reg);
		 common_Message.setMessage_content(reg);
		 common_Message.setSender_id(sender.getUser_id());
		 common_Message.setReceiver_id(receiver.getUser_id());
		  common_Message.setGroup_id(chat_Group.getGroup_id());
		  common_Message.setMessage_receive(false);
		 commonMessageDao.save(common_Message);
		 return common_Message;
	}
	@Override
	public Common_Message findDetailById(long commonMessage_id) {
		Common_Message common_Message=commonMessageDao.findCommonMessageById(commonMessage_id);
		return common_Message;
	}
	//更新消息为已接受
	@Override
	public void update(Common_Message common_Message) {
		 common_Message.setMessage_receive(true);
		 commonMessageDao.update(common_Message);
		
	}


}
