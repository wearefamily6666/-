var pojo = {
	url : {
		findDetail : "http://localhost:8080/The_vertical_and_horizontal/chatGroup_findDetailByGroupId2",
		invitation : "http://localhost:8080/The_vertical_and_horizontal/chatGroup_invitation",
		group_click : "http://localhost:8080/The_vertical_and_horizontal/user_invitation2",
		search : "http://localhost:8080/The_vertical_and_horizontal/chatGroup_search",
		doInvite : "http://localhost:8080/The_vertical_and_horizontal/commonMessage_doInvite",
		getNewMsg:"http://localhost:8080/The_vertical_and_horizontal/chatRecord_getMsg",
		send:"http://localhost:8080/The_vertical_and_horizontal/chatRecord_send",
		removeMember:"http://localhost:8080/The_vertical_and_horizontal/chatGroup_removeMember",
		search2:"http://localhost:8080/The_vertical_and_horizontal/chatGroup_search2",
		removeClick:"http://localhost:8080/The_vertical_and_horizontal/chatGroup_removeClick",
		getLocation:"http://localhost:8080/The_vertical_and_horizontal/chatGroup_getLocation"
	},
	// 聊天界面
	communicate : {
		//接收消息（针对聊天界面）
		receive:function(params){ 
				
		    //接收到消息的回调方法
		    params.websocket.onmessage = function (event) {
		    	//当前聊天群
			    	var str=event.data;  
			    	var obj = eval('(' + str + ')'); //字符串转json
			    	switch (obj.type) {
					case "communicate":
				        if (obj.user_id==params.user_id||obj.group_id!=params.group_id) {
							return false;
						}
				        var html="	<div class=\"left_msg\">"+
				        "<img src="+obj.user_image+"/>"+
						"<ul><li>"+obj.user_account+"</li>"+
						"<li><p>"+obj.content+"</p>"+
						"</li></ul>"+
				        "					</div>";
			        params.div.append(html);
			    	//设置滚动条到底部
					var talk=document.getElementById('talk');
					  talk.scrollTop=talk.scrollHeight;
						break;
					case "removeMember":
								if ( obj.success==null||obj.sender_id!=params.user_id)
									return false;
								$("#removeMember img[class='remove']").hide();
									params.removeTip.text("该成员已被移除！").fadeIn().fadeOut("slow");
								break;
					case "inviteMember":
						if ( obj.success==null||obj.sender_id!=params.user_id)
							return false;
							params.tip.text("已经发送邀请！").fadeIn().fadeOut("slow");
						break;
					case "removeGroup":
						if ( obj.success==null||obj.sender_id!=params.user_id)
							return false;
							params.removeTip.text("操作成功！").fadeIn().fadeOut("slow");
						break;
					default:
						break;
					}

		    }
		},
//			发送消息
			send:function(params){ 
			var html=	"	<div class=\"right_msg\">"+
				"							<ul>"+
				"								<li> <p>"+params.record_content+"</p></li>"+
				"								<li>	<img src="+params.user_image+" alt=\"\" /></li>"+
				"							</ul>"+
				"									"+
				"				"+
				"					"+
				"					</div>";
					params.div.append(html);
					var msgObj={
							"user_id":	params.user_id,
							"record_content":params.record_content,
							"group_id":params.group_id,
							"type":"communicate"
							};
					var msgJson=JSON.stringify(msgObj);
					params.websocket.send(msgJson);
					//设置滚动条到底部
					var talk=document.getElementById('talk');
					  talk.scrollTop=talk.scrollHeight;
			},
//			隐藏尚未查看到的新消息（点击时显示）滚动条到底部
			getNewMsg:function(params){  
				var newsHeight=0;
				var height=0;
				var record_id=[];
				//得到消息（如果新消息数量大于0，则获得相应新消息）
				$.post(pojo.url.getNewMsg,{
					"user_id":params.user_id,
					"group_id":params.group_id
					},function(result){ 
						var html=null;
						$.each(result,function(index,value){
							if (value.user_id==params.user_id) {
							html=	"	<div class=\"right_msg\">"+
								"							<ul>"+
								"								<li> <p>"+value.content+"</p></li>"+
								"								<li>	<img src="+value.user_image+" alt=\"\" /></li>"+
								"							</ul>"+
								"									"+
								"				"+
								"					"+
								"<input type=\"hidden\" value="+value.record_id+" class=\"record_id\"></input>"+
								"					</div>";
							}else{
								 html="	<div class=\"left_msg\">"+
							        "<img src="+value.user_image+"/>"+
									"<ul><li>"+value.user_account+"</li>"+
									"<li><p>"+value.content+"</p>"+
									"</li></ul>"+
									"<input type=\"hidden\" value="+value.record_id+" class=\"record_id\"></input>"+
							        "					</div>";
							}
							
						
								 
							params.div.append(html);
							//开始新消息计算高度
							if (index<result.length-params.newsCount) {
								newsHeight=newsHeight+params.div.find(".record_id").last().parent().find("ul").height()+30;
							}
						}); 
						//超过5条消息并且新消息数大于0才显示提示标签
						if (params.newsCount>0) {
							params.newMsgTip.text("有"+params.newsCount+"条新消息").show();
							params.newsHeight.val(newsHeight);
						}
						//设置滚动条到底部
						var talk=document.getElementById('talk');
						talk.scrollTop=talk.scrollHeight;
					})
			}
	},
	// 群详情
	groupDetail : {
		//封装对成员的展示
		showMember:function(result,params){ 
			 var i=0; var c=result.length%3;/*每行3个头像*/ 
			var html="<li>";
			 $.each(result,function(index,value){ 
					if (index%3==0&&index>2) {
						html+"</li><li>"
					}
				 html+="<img src="+value.user_image+" alt=\"\" data-toggle=\"tooltip\" data-placement=\"top\" title="+value.user_account+">";
			 });
			 params.ul.append(html);
			},
		//获取群详情
		findDetail : function(params) {
			$.post(pojo.url.findDetail, {
				"group_id" : params.group_id
			}, function(result) { 
				params.groupNotice.text(result.group_notice), params.groupOwner
						.text(result.group_owner), params.group_name
						.text(result.group_name),params.hidden.val(result.group_id),
						params.user_id.val(result.user_id)
				if (result.isGroupOwner == true) {
					params.img.show();
				} else {
					params.img.hide();
				}
			})
		},

		// 邀请新成员(群列表显示)
		invitation : function(params) {
			$.post(pojo.url.invitation, {
				"group_id" : params.group_id
			}, function(result) {
				params.ul.empty();
				$.each(result, function(index, value) {
					var html = "	<li><img src= " + result[index].group_image
							+ "/>" + "							<div class=\"add_group_wraper\">"
							+ "								<h3>" + result[index].group_name
							+ "</h3>" + "								<span class=\"count\">"
							+ +result[index].group_sum + "人" + "							</span>"
							+ "							</div>"
							+ "							<input type=\"hidden\" value="
							+ result[index].group_id
							+ " class=\"hidden\"></input>" + "						</li>";
					params.ul.append(html);
				})
			})
		},
		// 成员列表显示（邀请界面）
		group_click : function(params) {
			$
					.post(
							pojo.url.group_click,
							{
								"group_id" : params.group_id,
								"group_id2" : params.group_id2
							},
							function(result) {
								params.member_list_ul1.empty();
								params.member_list_ul2.empty();
								$.each(
												result,
												function(index, value) {
													var html = "<li><img src="
															+ result[index].user_image
															+ " />"
															+ "								<span >"
															+ result[index].user_account
															+ "							</span>"
															+ "							"
															+ "<input type=\"hidden\" value="
															+ index
															+ " class=\"hidden\"></input>"+

													"						</li>";
													var html2 = "<li><input type=\"radio\" name=\"group\" id=\"\" value="
															+ index
															+ " /></li>";
													params.member_list_ul1
															.append(html);
													params.member_list_ul2
															.append(html2);
												})
								if (params.member_list_ul2.children().length > 0) {
									params.ul2.show();
								} else {
									params.ul2.hide();
								}
							})
		},
		// 搜索监听(邀请界面)
		search : function(params) {
			$.post(pojo.url.search, {
				"group_id" : params.group_id,
				"key" : params.key
			}, function(result) {
				params.ul.empty();
				$.each(result, function(index, value) {
					var html = "	<li><img src= " + result[index].group_image
							+ "/>" + "							<div class=\"add_group_wraper\">"
							+ "								<h3>" + result[index].group_name
							+ "</h3>" + "								<span class=\"count\">"
							+ +result[index].group_sum + "人" + "							</span>"
							+ "							</div>"
							+ "							<input type=\"hidden\" value="
							+ result[index].group_id
							+ " class=\"hidden\"></input>" + "						</li>";
					params.ul.append(html);
				})
			})
		},
		// 发送邀请请求
 		doInvite : function(params) { 
			var js={
					"user_account" : params.user_account,
					"group_id" : params.group_id,
					"type":params.type,
					"user_id":params.user_id
			}
			var json=JSON.stringify(js);
			params.websocket.send(json);
		}, 
		//移除群成员界面显示成员
		removeMember:function(params){
			params.ul.empty();
			$.post(pojo.url.removeMember,{"group_id":params.group_id},function(result){  
					pojo.groupDetail.showMember(result,params);
			})
		},
		//搜索监听（移除群成员界面）
		search2:function(params){
			params.ul.empty();
			$.post(pojo.url.search2,{"group_id":params.group_id,"key":params.key},function(result){
				pojo.groupDetail.showMember(result,params);
			})
		},
		//点击移除按钮
		removeClick:function(params){ 
			var js={
					"user_account":params.user_account,
					"group_id":params.group_id,
					"type":"removeMember"
			};
			var json=JSON.stringify(js);
			params.websocket.send(json);

		},
		//退出/解散群聊
		exitGroup:function(params){
			var js={
					"user_id":params.user_id,
					"group_id":params.group_id,
					"type":"removeGroup"
			};
			var json=JSON.stringify(js);
			params.websocket.send(json);
		}
 
	},
	// 实时位置
	map : {
		//创建信息窗口
		infoWindow:function(params){
			var marker = new BMap.Marker(params.point); // 创建标注    
			params.map.addOverlay(marker); // 将标注添加到地图中 

			var infoWindow = new BMap.InfoWindow(params.sContent); // 创建信息窗口对象
			marker.addEventListener("click", function() {
				this.openInfoWindow(infoWindow);
				//图片加载完毕重绘infowindow
				$(".map_info img").onload=function() {
					infoWindow.redraw(); //防止在网速较慢，图片未加载时，生成的信息框高度比图片的总高度小，导致图片部分被隐藏
				}
			});
			//方便聚合
			params.markers.push(marker);
		},
		realLocation:function(params){
			var map = new BMap.Map("allmap");
			var point = new BMap.Point(116.331398,39.897445);
			map.centerAndZoom(point,15);
			map.enableScrollWheelZoom(true); //开启鼠标滚轮缩放
			var markers = [];
				//浏览器定位
			var geolocation = new BMap.Geolocation();
			var pointMe=null;
			geolocation.getCurrentPosition(function(r){
				if(this.getStatus() == BMAP_STATUS_SUCCESS){
					var mk = new BMap.Marker(r.point);
					map.addOverlay(mk);
					map.panTo(r.point);
					//定位后添加
					pointMe=new BMap.Point(r.point.lng,r.point.lat);
					var 	sContent = "<div class=\"map_info\">" +
					"		 	<img src="+params.user_image+"/>" +
					"			<p><span>"+params.user_account+"</span></p>" +
					"</div>";

					//调用信息窗口方法
					pojo.map.infoWindow({
						"point":pointMe,
						"markers":markers,
						"map":map,
						"sContent":sContent
					});
				}
				else {
					console.log('failed'+this.getStatus());
				}        
			},{enableHighAccuracy: true})
					
					
					var points=[];
					var sContents=[];
					var i = 0;
					var sContent=null;
					
					//获取群成员头像，时间，用户名
			$.post(pojo.url.getLocation,{
				"group_id":params.group_id
			},function(result){
				$.each(result,function(index,value){ 
					sContent = "<div class=\"map_info\">" +
					"		 	<img src="+value.user_image+"/>" +
					"			<p><span>"+value.user_account+"</span></p>" +
				"</div>";
				var point =new BMap.Point(value.longtitude,value.latitude);
				//调用信息窗口方法
				pojo.map.infoWindow({
					"point":point,
					"markers":markers,
					"map":map,
					"sContent":sContent
				});
				
				})
			})
			//聚合
			new BMapLib.MarkerClusterer(map, {
				markers: markers
			});
		}
	}
}