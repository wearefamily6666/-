var pojo = {
		ul:{
			loadInformation:"http://localhost:8080/The_vertical_and_horizontal/commonMessage_findByUserId",
		},
	commonMessage:{
		loadInformation:function(params){
			 params.ul.empty();
			 $.post(pojo.ul.loadInformation,null,function(result){ 
				 $.each(result,function(index,value){
					 var html=null;	 
					 if (value.type==1) {
						
						 html = " <div class=\"divider\"></div>"+
						 "				<div class=\"info_wraper\">"+
						 "					<img"+
						 "						src="+value.group_image+">"+
						 "					<h4>"+value.group_name+"</h4>"+
						 "					<span class=\"last_news\">"+value.newitem+"</span> <span class=\"time\">"+value.time+"</span>"+
						 "					"+
						 "				</div>"+
						 "					<input type=\"hidden\" class=\"commonMessage_id\" value="+value.commonMessage_id+"></input>";
						  
					}else{
						if (value.message_receive) {
							 html=  " <div class=\"divider\"></div>"+
							 "				<div class=\"info_wraper\">"+
							 "					<img"+
							 "						src="+value.group_image+">"+
							 "					<h4>"+value.group_name+"</h4>"+
							 "					<span class=\"last_news\">"+value.newitem+"</span> <span class=\"time\">"+value.time+"</span>"+
							 "					"+
							 "				</div>"+
							 "					<button disabled=\"disabled\">已接受</button>"+
							 "					<input type=\"hidden\" class=\"commonMessage_id\" value="+value.commonMessage_id+"></input>";
						}else{
							 html=  " <div class=\"divider\"></div>"+
							 "				<div class=\"info_wraper\">"+
							 "					<img"+
							 "						src="+value.group_image+">"+
							 "					<h4>"+value.group_name+"</h4>"+
							 "					<span class=\"last_news\">"+value.newitem+"</span> <span class=\"time\">"+value.time+"</span>"+
							 "					"+
							 "				</div>"+
							 "					<button>接受</button>"+
							 "					<input type=\"hidden\" class=\"commonMessage_id\" value="+value.commonMessage_id+"></input>";
						}
						
						 
					}

					 params.ul.append(html); 
				 })

			 })
		},
		//针对被邀请加群，申请加群的处理
		agree:function(params){
			var js={
					type:params.type,
					commonMessage_id:params.commonMessage_id,
					sender_id:params.sender_id
			}
			var json=JSON.stringify(js);
			params.websocket.send(json);
		},
		receive:function(params){ 
			params.websocket.onmessage=function(event){
				var str=event.data;
				var json=eval('('+str+')'); 
				if (json.success!=null&&json.type=="information"&&json.sender_id==params.user_id) {
					$(".isReceive").hide().text("已接受").show();
					$(".isReceive").attr("disabled", true); //按钮不可用
					$(".isReceive").removeClass("isReceive");
				}
			}
		}
	}
}