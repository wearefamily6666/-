//模块化处理交互
//访问格式：pojo.*.*

var pojo={
		//地址
		url:{
			user_login:"http://localhost:8080/The_vertical_and_horizontal/user_login",
			user_logout:"http://localhost:8080/The_vertical_and_horizontal/user_logout",
			loginvalidate:"http://localhost:8080/The_vertical_and_horizontal/user_loginvalidate",
			loginSuccess:"http://localhost:8080/The_vertical_and_horizontal/user_loginSuccess",
			modify_user:"http://localhost:8080/The_vertical_and_horizontal/user_modifyUser",
			modify_userSuccess:"http://localhost:8080/The_vertical_and_horizontal/user_modifyUserSuccess",
	//加载已加入群 
	findAllJoined:"http://localhost:8080/The_vertical_and_horizontal/chatGroup_findAllJoined",
	//根据key搜索已加入群 
	findAllJoinedBykey:"http://localhost:8080/The_vertical_and_horizontal/chatGroup_findAllJoinedByKey"
		},
 
//		验证手机号
		isPhoneNo:function(){
			jQuery.validator.addMethod("isPhoneNo", function(value, element) {   
				 var pattern = /^1[34578]\d{9}$/;
			    return this.optional(element) || (pattern.test(value));
			}, "请正确填写您的手机号")
		},
		
		//登录页
		user:{

//			登录
			login:function(paramsNode){
			 	paramsNode.form.validate({
						//自定义规则
								 rules:{
									 user_account:{
										 required:true
									 },
									 user_password:{
										 required:true,
										 minlength:5
									 }
								 },
								 message:{
									 
								 },
								 submitHandler:function(){ 
									 $.post(pojo.url.loginvalidate,paramsNode.form.serialize(),function(result){ 
										 if (result=="false") 
										paramsNode.error.text("用户名或密码错误！");
										 else
											 window.location.href=pojo.url.loginSuccess;
									 })
									 
								 }
						 })
			},
//			退登
			user_logout:function(params){
			$.post(pojo.url.user_logout,{"user_account":params},function(){
				
			})
		},
		//关闭浏览器监听
		close_window:function(params){
			pojo.user.user_logout({"user_account":params})
		},
//		修改个人信息
		modify_user:function(params){
		 pojo.isPhoneNo();
		return	params.form.validate({
						//自定义规则
								 rules:{
									 some:{
										 required:true
									 },
									 user_name:{
										 required:true
									 },
									 user_phone:{
										 required:true,
										 isPhoneNo:true
									 },
									 user_email:{
										 required:true,
										 email:true
									 }
								 },
								 message:{
									 
								 },
								 submitHandler:function(){ 
									 
									 $.ajax({
							                url:pojo.url.modify_userSuccess,
							                type:"post",
							                data:params.formData,
							                processData:false,
							                contentType:false,
							                success:function(data){console.log("modify_user:  "+data)
							                    if (!data) {
							                    	 params.tip.text("上传失败！").fadeIn().fadeOut("slow");
												} else {
													 params.tip.text("上传成功！").fadeIn().fadeOut("slow");
												}
							                },
							                error:function(e){
							                    alert("错误！！");
							                    window.clearInterval(timer);
							                }
							            });        
								 }
						 })
			}
		 
		},

		//登录后左布局
		chatGroup:{
			//获取group_id
			getGroupId:function(){
				return $("#group_id");
			},
			//接收消息（针对消息列表）
				receive:function(params){  
				    //接收到消息的回调方法
				    params.websocket.onmessage = function (event) {
				    		var str=event.data;  
					    	var obj = eval('(' + str + ')'); //字符串转json

				    	var num=0;//记录单项值
	        			var count=params.getImgCountHide.val();//记录总消息量
	        			var html=null;
        				switch (obj.type) {
						case "communicate":
					    	//是否为非当前聊天群，假设有消息，但没在该聊天界面，则消息数量不断增加
					    	if (pojo.chatGroup.getGroupId().val()==obj.group_id) {
								return false;
							} 
					    	var flag=false;
							//1.如果列表没有该群，则添加，并初始化消息为1;如果有该群，则消息数量加一
			        		//2.总消息数量加一
			        		//3.点击该群，则该群消息数量变为0，下次进入该群时不再隐藏原先隐藏的消息
		        			//返回同类标签所有元素
							var inputs=new Array();		
		        			params.getNewsul.find("input:hidden").each(function(i) {

								var input=$(this);
		        				//如果有该群
								if (input.val()==obj.group_id) {
									flag=true;
									num=parseInt(input.parent().find(".hideNewsCount").text())+1;
									 //移除该群
									 input.parent().remove();
									//如果消息量超过9，则不显示数量，只显示红点
									if (num>9) {
						        		 html="			<li>"+
										"							<div class=\"info_wraper\">"+
										"								<img src="+obj.group_image+" />"+
										"								<h4>"+obj.group_name+"</h4>"+
										"								<span class=\"last_news\">"+obj.whocontent+"</span>"+
										"							<span class=\"time\">"+obj.time+"</span>"+
										"							<span class=\"newsCount itemNoNum\" ></div>"+
										"<span class=\"hideNewsCount\">"+num+"</span>"+
										"<input type=\"hidden\" class=\"group_id\" value="+obj.group_id+"></input>"
										"							"+
										"						</li>";
									}else {
						        		 html="			<li>"+
										"							<div class=\"info_wraper\">"+
										"								<img src="+obj.group_image+" />"+
										"								<h4>"+obj.group_name+"</h4>"+
										"								<span class=\"last_news\">"+obj.whocontent+"</span>"+
										"							<span class=\"time\">"+obj.time+"</span>"+
										"							<span class=\"newsCount itemHasNum\">"+num+"</div>"+
										"<span class=\"hideNewsCount\">"+num+"</span>"+
										"<input type=\"hidden\" class=\"group_id\" value="+obj.group_id+"></input>"
										"							"+
										"						</li>";
									}
									params.getNewsul.append(html);
									count++;
									
									return false;
								}
		        			})
		        			 if (!flag) {
									//没有该群
			        			 html="			<li>"+
									"							<div class=\"info_wraper\">"+
									"								<img src="+obj.group_image+" />"+
									"								<h4>"+obj.group_name+"</h4>"+
									"								<span class=\"last_news\">"+obj.whocontent+"</span>"+
									"							<span class=\"time\">"+obj.time+"</span>"+
									"							<span class=\"newsCount itemHasNum\">1</div>"+
									"<span class=\"hideNewsCount\">1</span>"+
									"<input type=\"hidden\" class=\"group_id\" value="+obj.group_id+"></input>"
									"							"+
									"						</li>";
								params.getNewsul.append(html);
								count++;
							}

							break;
						case "addGroup":
						case "removeMember":
						case "inviteMember":
						case "退出":
						case "解散":	
							//addGroup:非群主，没收到该消息
							//removeMember:非被移除成员，没收到该消息
							//inviteMember:非被邀请人，没收到消息
							//removeGroup:根据消息内容分为：退出群，解散群
							//退群：退群成员移除对应群列表，消息列表，  该群其他成员收到某成员退群消息
							//解散群：所有群成员移除对应群列表，消息列表，除了群主的所有成员收到群主解散群消息
							var isAddNews=false;//是否要添加普通消息
							var isRemoveList=false;//是否移除对应群列表，消息列表
							console.log(obj.type+"          obj.user_id:"+obj.user_id+"          params.user_id:"+params.user_id+"        obj.sender_id:"+obj.sender_id+"        obj.group_id:"+obj.group_id)
							 if (obj.type=="addGroup"||obj.type=="removeMember"||obj.type=="inviteMember") {
								if (obj.user_id==params.user_id) {
									isAddNews=true;
									isRemoveList=true;
								}
							}else if (obj.type=="退出") {
								if (params.user_id==obj.sender_id) {
									isRemoveList=true;
								}
								//判断是否有对应群
								params.getGroupsul.find(".group_id").each(function() {
									if ($(this).val()==obj.group_id&&params.user_id!=obj.sender_id) {
										isAddNews=true;
									}
								})
							}else{
								//判断是否有对应群
								params.getGroupsul.find(".group_id").each(function() {
									if ($(this).val()==obj.group_id) {
										isRemoveList=true;
										if (params.user_id!=obj.sender_id) {
											isAddNews=true;
										}
									}
								})
								
							}
							 
								if (isRemoveList) {console.log("isRemoveList")
									//移除群列表，消息列表中的对应群
									params.getNewsul.find("input:hidden").each(function(i) {
										if ($(this).val()==obj.group_id&&$(this).attr("id")==null) {
											$(this).parent().remove();
										}
									})
									params.getGroupsul.find(".group_id").each(function(i) {
										if ($(this).val()==obj.group_id) {
											$(this).prev().remove();
											$(this).remove();
										}
									})
									//如果有群则聊天界面显示第一个群内容(传递当前group_id)
									if ($("#talk").find(".group_id").length>0) {
										setTimeout(function() {
											params.iframe.attr("src",params.root+"/chatRecord_communicate?group_id="+params.getGroupsul.find(".group_id:eq(0)").val()+"&newsCount="+0);
										}, 0);
									}else{
										//没有群隐藏新消息提示
										$("news_count").hide();
									}
								} if (isAddNews) {console.log("isAddnews")
									//判断是否有普通消息列表，有先移除
									if (params.getNewsul.find("#commonMessage").length>0) {
										//获取普通消息数量
										num=parseInt(params.getNewsul.find("#commonMessage").parent().find(".hideNewsCount").text())+1;
										params.getNewsul.find("#commonMessage").parent().remove();
									}else{
										num=1;
									}
									if (num>9) {
										html="	<li>"+
										"							<div class=\"info_wraper\">"+
										"								<img src="+obj.group_image+" />"+
										"								<h4>"+obj.group_name+"</h4>"+
										"								<span class=\"last_news\">"+obj.last_news+"</span>"+
										"							<span class=\"time\">"+obj.time+"</span>"+
										"							<span class=\"newsCount itemNoNum\">1</div>"+
										"<span class=\"hideNewsCount\">"+num+"</span>"+
										"					<input type=\"hidden\" id=\"commonMessage\"	/>	"+
										"						</li>";	
									}else{
										html="	<li>"+
										"							<div class=\"info_wraper\">"+
										"								<img src="+obj.group_image+" />"+
										"								<h4>"+obj.group_name+"</h4>"+
										"								<span class=\"last_news\">"+obj.last_news+"</span>"+
										"							<span class=\"time\">"+obj.time+"</span>"+
										"							<span class=\"newsCount itemHasNum\">"+num+"</div>"+
										"<span class=\"hideNewsCount\">"+num+"</span>"+
										"					<input type=\"hidden\" id=\"commonMessage\"	/>	"+
										"						</li>";
									}

									params.getNewsul.append(html);
									count++;
								}
							break;
						case "information":
							console.log("information:          obj.receiver_id"+obj.receiver_id+"          params.user_id:"+params.user_id)
							if (obj.receiver_id!=params.user_id) {
								return false;
							}
							//删除对应群，避免重复
							$("#list .group_id").each(function(index) {
								if ($(this).val()==obj.group_id) {
									$(this).prev().remove();
									$(this).remove();
								}
							})
								html="	<li><img src= "+obj.group_image+"/>"+
								"							<div class=\"wraper\">"+
								"								<h3>"+obj.group_name+"</h3>"+
								"								<span class=\"count\">"+
																+obj.group_sum+ "人"+
								"							</span>"+
								"							</div>"+
								"						</li>"+
								"<input type=\"hidden\" value="+obj.group_id+" class=\"group_id\"></input>";
							 
						count++;
						
							$("#list ul:eq(0)").append(html);
							break;
						default:
							break;
						}
        				//消息总量
        				if (obj.type=="information") {
							return false;
						}
        				if (count>9) {
							params.getImgCount.text("");
							params.getImgCount.attr("class","noNum");
						}else if(count<=9&count>0){
							params.getImgCount.text(count);
							params.getImgCount.attr("class","hasNum");
						}
						params.getImgCountHide.val(count)//（隐藏元素）该元素记录总量
						if (count>0) {
							params.getImgCount.show();
						}
				    }
				},
//			加载所有已加入群 ,初始化消息列表
			loadJoined:function(params){
				 var hiddenCount=0;
				 var itemCount=0;
				$.post(pojo.url.findAllJoined,null,function(result){
					params.ul.empty(); 
					$.each(result.groupList,function(index,value){   
						var html="	<li><img src= "+value.group_image+"/>"+
						"							<div class=\"wraper\">"+
						"								<h3>"+value.group_name+"</h3>"+
						"								<span class=\"count\">"+
														+value.group_sum+ "人"+
						"							</span>"+
						"							</div>"+
						"						</li>"+
						"<input type=\"hidden\" value="+value.group_id+" class=\"group_id\"></input>";
						params.ul.append(html); 
					})
					$.each(result.newsList,function(index,value){
						var	 html=null;
						if (value.commonMessage!=null) {  
							if (value.newsCount>9) {
								html="	<li>"+
								"							<div class=\"info_wraper\">"+
								"								<img src="+value.group_image+" />"+
								"								<h4>"+value.group_name+"</h4>"+
								"								<span class=\"last_news\">"+value.whocontent+"</span>"+
								"							<span class=\"time\">"+value.time+"</span>"+
								"							<span class=\"newsCount itemNoNum\"></div>"+
								"<span class=\"hideNewsCount\">"+value.newsCount+"</span>"+
								"					<input type=\"hidden\" id=\"commonMessage\"	/>	"+
								"						</li>";
							}else if (value.newsCount>0&value.newsCount<=9) {
								html="	<li>"+
								"							<div class=\"info_wraper\">"+
								"								<img src="+value.group_image+" />"+
								"								<h4>"+value.group_name+"</h4>"+
								"								<span class=\"last_news\">"+value.whocontent+"</span>"+
								"							<span class=\"time\">"+value.time+"</span>"+
								"							<span class=\"newsCount itemHasNum\">"+value.newsCount+"</div>"+
								"<span class=\"hideNewsCount\">"+value.newsCount+"</span>"+
								"					<input type=\"hidden\" id=\"commonMessage\"	/>	"+
								"						</li>";
							}else{ 
								html="	<li>"+
								"							<div class=\"info_wraper\">"+
								"								<img src="+value.group_image+" />"+
								"								<h4>"+value.group_name+"</h4>"+
								"								<span class=\"last_news\">"+value.whocontent+"</span>"+
								"							<span class=\"time\">"+value.time+"</span>"+
								"							<span class=\"newsCount\" style=\"display:none\">0</div>"+
								"<span class=\"hideNewsCount\">"+value.newsCount+"</span>"+
								"					<input type=\"hidden\" id=\"commonMessage\"	/>	"+
								"						</li>";
							}
						}else{
							if (value.newsCount>9) {
								//加载消息列表
								
									html="			<li>"+
									"							<div class=\"info_wraper\">"+
									"								<img src="+value.group_image+" />"+
									"								<h4>"+value.group_name+"</h4>"+
									"								<span class=\"last_news\">"+value.whocontent+"</span>"+
									"							<span class=\"time\">"+value.time+"</span>"+
									"							<span class=\"newsCount itemNoNum\"></div>"+
									"<span class=\"hideNewsCount\">"+value.newsCount+"</span>"+
									"<input type=\"hidden\" class=\"group_id\" value="+value.group_id+"></input>"
									"							"+
									"						</li>";
							
				        		
							}else if(value.newsCount>0&value.newsCount<=9){
								//加载消息列表
									 html="			<li>"+
										"							<div class=\"info_wraper\">"+
										"								<img src="+value.group_image+" />"+
										"								<h4>"+value.group_name+"</h4>"+
										"								<span class=\"last_news\">"+value.whocontent+"</span>"+
										"							<span class=\"time\">"+value.time+"</span>"+
										"							<span class=\"newsCount itemHasNum\">"+value.newsCount+"</div>"+
										"<span class=\"hideNewsCount\">"+value.newsCount+"</span>"+
										"<input type=\"hidden\" class=\"group_id\" value="+value.group_id+"></input>"
										"							"+
										"						</li>";
							} 
							
						} 
						 hiddenCount=hiddenCount+value.newsCount; 
						 params.getImgCountHide.val(hiddenCount)//隐藏元素记录总量
	        			 params.getNewsul.append(html); 
	        			 if (hiddenCount>9) {
	        				 params.getImgCount.text("");
	        				 params.getImgCount.attr("class","noNum");
							 params.getImgCount.show();
						}else if(hiddenCount>0&&hiddenCount<=9){
							 params.getImgCount.text(hiddenCount);
							 params.getImgCount.attr("class","hasNum");
							 params.getImgCount.show();

						}else{
							 params.getImgCount.hide();
						}
					})	
					if (result.groupList.length==0)
						return false;
						//初始化隐藏元素group_id
						$("#group_id").val(result.groupList[0].group_id);
					//调用receive,接入websocket
						pojo.chatGroup.receive({
							websocket: params.websocket,
							getNewsul:params.getNewsul,
							getGroupsul:params.getGroupsul,
							getImgCount:params.getImgCount,
							getImgCountHide:params.getImgCountHide,
							user_id:params.user_id,
							iframe:params.iframe
						})
					//聊天界面显示第一个群内容(传递当前group_id)
							setTimeout(function() {
								params.iframe.attr("src",params.root+"/chatRecord_communicate?group_id="+result.groupList[0].group_id+"&newsCount="+0);
							}, 0); 
				 	
				})
			
				 
			},
//			在已加入群搜索
			query:function(params){ 

				$.post(pojo.url.findAllJoinedBykey,{"key":params.key},function(result){
					params.ul.empty();
				$.each(result,function(index,value){ 
					var html="	<li><img src= "+value.group_image+"/>"+
					"							<div class=\"wraper\">"+
					"								<h3>"+value.group_name+"</h3>"+
					"								<span class=\"count\">"+
													+value.group_sum+ "人"+
					"							</span>"+
					"							</div>"+
					"<input type=\"hidden\" value="+index+" class=\"index\"></input>"+
					"						</li>"+
					"<input type=\"hidden\" value="+value.group_id+" class=\"group_id\"></input>";
					
					params.ul.append(html);
				})
				
				})
			}
		}
}
 
