var forbidden={
		url:{
			group_forbidden: "http://localhost:8080/The_vertical_and_horizontal/admin_groupForbidden",
			user_forbidden: "http://localhost:8080/The_vertical_and_horizontal/admin_userForbidden"
		},
//		群是否禁止监听
		group_forbidden:function(params){
			$.post(forbidden.url.group_forbidden,{
				"group_id":params.group_id
			},function(result){ 
				if (result=="true") {
					params.tip.text('已经禁止').fadeIn().fadeOut('slow');
				}else if(result=="false"){
					params.tip.text('取消禁止').fadeIn().fadeOut('slow');
				}else{
					params.tip.text('无效操作').fadeIn().fadeOut('slow');
				}
			})
		},
//		用户是否禁止禁止监听
		user_forbidden:function(params){ 
			$.post(forbidden.url.user_forbidden,{
				"user_id":params.user_id
			},function(result){ 
				if (result=="true") {
					params.tip.text('已经禁止').fadeIn().fadeOut('slow');
				}else if(result=="false"){
					params.tip.text('取消禁止').fadeIn().fadeOut('slow');
				}else{
					params.tip.text('无效操作').fadeIn().fadeOut('slow');
				}
			})
		}

}