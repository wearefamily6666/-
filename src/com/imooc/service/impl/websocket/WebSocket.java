package com.imooc.service.impl.websocket;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.persistence.criteria.CriteriaBuilder.Case;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.imooc.action.ChatRecordAction;
import com.imooc.pojo.Chat_Group;
import com.imooc.pojo.Chat_Record;
import com.imooc.pojo.Common_Message;
import com.imooc.pojo.User;
import com.imooc.service.IChatGroupService;
import com.imooc.service.IChatRecordService;
import com.imooc.service.ICommonMessageService;
import com.imooc.service.IUserService;

@ServerEndpoint("/websocket")
public class WebSocket {
	static IChatRecordService chatRecordService;
	static IUserService userService;
	static IChatGroupService chatGroupService;
	private ICommonMessageService commonMessageService;
	//静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<WebSocket>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    
    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session){
    	 System.out.println("有新连接加入！");
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
    	webSocketSet.remove(this);  //从set中删除
       subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }
    //引用业务层
    public WebSocket() {
    	ApplicationContext act = new ClassPathXmlApplicationContext("applicationContext.xml");
     userService= act.getBean(IUserService.class);
     chatGroupService=act.getBean(IChatGroupService.class);
     chatRecordService=act.getBean(IChatRecordService.class);
     commonMessageService=act.getBean(ICommonMessageService.class);
    }
    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
 
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        JSONObject jsonObject=JSONObject.parseObject(message);
        //根据type判断message类型，type:加入群，邀请新成员，移除成员，解散群,群聊
        long group_id;
        long user_id ;
		long sender_id;
		String record_content;
		switch (jsonObject.getString("type")) {
		case "addGroup":
			HashMap<String, Object> aHashMap=new HashMap<>();
				try {
					Common_Message commonMessage=new Common_Message();
					sender_id=jsonObject.getLongValue("sender_id");
					group_id=jsonObject.getLongValue("group_id");
					commonMessage.setSender_id(sender_id);
					commonMessage.setGroup_id(group_id);
					commonMessage=commonMessageService.applyToJoin(commonMessage);
					aHashMap.put("type", "addGroup");
				Chat_Group chat_Group=	chatGroupService.findDetailByGroupId(commonMessage.getGroup_id());
					aHashMap.put("group_image", chat_Group.getGroup_image());
					aHashMap.put("last_news", commonMessage.getMessage_content());
					aHashMap.put("time", LocalTime.now().withNano(0));
					aHashMap.put("group_name", chat_Group.getGroup_name());
					aHashMap.put("success", true);
					aHashMap.put("user_id", chat_Group.getUser_id());//接收者
					aHashMap.put("sender_id", sender_id);
					jsonObject=new JSONObject(aHashMap);
				} catch (Exception e) {
					 e.printStackTrace();
				}  
			break;
		case "inviteMember":
			HashMap<String, Object> iHashMap=new HashMap<>();
			try {
				Common_Message commonMessage = null;
				String user_account = jsonObject.getString("user_account");
				group_id = jsonObject.getLongValue("group_id");
				user_id=jsonObject.getLong("user_id");
				User receiver = userService.findUserByUserAccount(user_account);
				Chat_Group chat_Group = chatGroupService.findDetailByGroupId(group_id);
				User sender=userService.findUserByUserId(user_id);
				commonMessage = commonMessageService.doInvite(receiver, chat_Group,sender);
				iHashMap.put("group_image", chat_Group.getGroup_image());
				iHashMap.put("last_news", commonMessage.getMessage_content());
				iHashMap.put("time", LocalTime.now().withNano(0));
				iHashMap.put("group_name", chat_Group.getGroup_name());
				iHashMap.put("success", true);
				iHashMap.put("type", "inviteMember");
				iHashMap.put("user_id", receiver.getUser_id());// 接收者
				iHashMap.put("sender_id", commonMessage.getSender_id());
				iHashMap.put("group_id", chat_Group.getGroup_id());
				jsonObject = new JSONObject(iHashMap);
			} catch (Exception e) {
				e.printStackTrace();
				}
			break;
		case "removeMember":
			HashMap<String, Object> rHashMap=new HashMap<>();
			try {
				Common_Message commonMessage=null;
					String user_account=jsonObject.getString("user_account");
					group_id=jsonObject.getLongValue("group_id");
					commonMessage=chatGroupService.removeClick(user_account,group_id);
					Chat_Group chat_Group=	chatGroupService.findDetailByGroupId(group_id);
					rHashMap.put("group_image", chat_Group.getGroup_image());
					rHashMap.put("last_news", commonMessage.getMessage_content());
					rHashMap.put("time", LocalTime.now().withNano(0));
					rHashMap.put("group_name", chat_Group.getGroup_name());
					rHashMap.put("success", true);
					rHashMap.put("type", "removeMember");
					User user=userService.findUserByUserAccount(user_account);
					rHashMap.put("user_id", user.getUser_id());//接收者
					 rHashMap.put("sender_id", commonMessage.getSender_id());
					rHashMap.put("group_id", chat_Group.getGroup_id());
					jsonObject=new JSONObject(rHashMap);
				} catch (Exception e) {
					 e.printStackTrace();
				}
			break;
		case "removeGroup":
			HashMap<String, Object> rHashMap2=new HashMap<>();
			try {
				user_id=jsonObject.getLong("user_id");
				group_id=jsonObject.getLong("group_id");
				Chat_Group chat_Group=chatGroupService.findDetailByGroupId(group_id);
					Common_Message commonMessage=	chatGroupService.exitGroup(user_id,group_id);
					
						rHashMap2.put("group_image", chat_Group.getGroup_image());
						rHashMap2.put("last_news", commonMessage.getMessage_content());
						rHashMap2.put("time", LocalTime.now().withNano(0));
						rHashMap2.put("group_name", chat_Group.getGroup_name());
						rHashMap2.put("success", true);
						if (commonMessage.getMessage_content().contains("退出")) {
							rHashMap2.put("type", "退出");
						}else {
							rHashMap2.put("type", "解散");
						}
						
						rHashMap2.put("content", commonMessage.getMessage_content());
						rHashMap2.put("sender_id", commonMessage.getSender_id());
						rHashMap2.put("group_id", chat_Group.getGroup_id());
						jsonObject=new JSONObject(rHashMap2);
			} catch (Exception e) {
				 e.printStackTrace();
			}

			break;
		case "communicate":
			user_id=jsonObject.getLong("user_id");
			group_id=jsonObject.getLong("group_id");
			record_content=jsonObject.getString("record_content");
			Chat_Record chat_Record=new Chat_Record();
			chat_Record.setUser_id(user_id);
			chat_Record.setGroup_id(group_id);
			chat_Record.setRecord_content(record_content);
			chat_Record.setRecord_send_time(new Date());
			chat_Record.setRecord_is_read(false);
			   //保存用户发送信息到chat_record表
	 		chatRecordService.save(chat_Record);
	 
	        HashMap<String, Object> cHashMap=new HashMap<>();
	        cHashMap.put("user_id", user_id);
	        //通过user_id查询用户
	        User user=userService.findUserByUserId(user_id);
	        //通过group_id查询群
	        Chat_Group chat_Group=chatGroupService.findDetailByGroupId(group_id);
	        cHashMap.put("user_image", user.getUser_image());
	        cHashMap.put("user_account", user.getUser_account());
	        cHashMap.put("group_name", chat_Group.getGroup_name());
	        cHashMap.put("group_image", chat_Group.getGroup_image());
	        cHashMap.put("content", record_content);
	        cHashMap.put("whocontent", user.getUser_account()+" : "+record_content);
	        cHashMap.put("time",  LocalTime.now().withNano(0));
	        cHashMap.put("group_id", group_id);
	        cHashMap.put("type", "communicate");
	        jsonObject=new JSONObject(cHashMap);
			break;
		case "information":
			try {
				long commonMessage_id = jsonObject.getLong("commonMessage_id");
				sender_id=jsonObject.getLong("sender_id");
				Common_Message common_Message=commonMessageService.findDetailById(commonMessage_id);
				//更新为已接受
				commonMessageService.update(common_Message);
				if (common_Message.getMessage_content().contains("邀请")) {
					chatGroupService.addGroupMember(sender_id,common_Message.getGroup_id());
				}else if (common_Message.getMessage_content().contains("申请")) {
					chatGroupService.addGroupMember(common_Message.getSender_id(),common_Message.getGroup_id());
				}
				Chat_Group chat_Group2=chatGroupService.findDetailByGroupId(common_Message.getGroup_id());
				HashMap<String, Object> iMap=new HashMap<>();
				iMap.put("group_name", chat_Group2.getGroup_name());
				iMap.put("group_sum", chat_Group2.getGroup_sum());
				iMap.put("group_image", chat_Group2.getGroup_image());
				iMap.put("group_id", chat_Group2.getGroup_id());
				iMap.put("type", "information");
				iMap.put("success", true);
				if (common_Message.getMessage_content().contains("邀请")) {
					iMap.put("receiver_id", common_Message.getReceiver_id());
				}else {
					iMap.put("receiver_id", common_Message.getSender_id());
				}
				//插入发送人user_id
				 iMap.put("sender_id",sender_id);
				jsonObject=new JSONObject(iMap);
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		default:
			break; 
		}
      
        //群发消息
        for(WebSocket item: webSocketSet){
            try {
                item.sendMessage(jsonObject.toString());
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }
    
    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException{
    	System.out.println("消息： "+message);
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
    	WebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
    	WebSocket.onlineCount--;
    }
}
