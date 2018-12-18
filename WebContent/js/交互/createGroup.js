var pojo={
		url:{
			createGroupSuccess:"http://localhost:8080/The_vertical_and_horizontal/chatGroup_createGroupSuccess"
		},
//		创建群界面
		chatGroup:{
			//创建群验证
			createGroup:function(params){
				return	params.form.validate({
					//自定义规则
							 rules:{
								 group_name:{
									 required:true,
									 maxlength:10
								 },
								 group_image:{
									 required:true
								 },
								 group_notice:{
									 required:true,
									 minlength:5,
									 maxlength:10
								 }
							 },
							 message:{
								 
							 },
							 submitHandler:function(){ 
								 $.ajax({
						                url:pojo.url.createGroupSuccess,
						                type:"post",
						                data:params.formData,
						                processData:false,
						                contentType:false,
						                success:function(data){ 
						                    if (data=="false") {
						                    	 params.tip.text("创建群失败！").fadeIn().fadeOut("slow");
											} else {
												 params.tip.text("创建群成功！").fadeIn().fadeOut("slow");
											}
						                },
						                error:function(e){
						                    alert("错误！！");
						                }
						            });        
							 }
					 })
				}
		}

	}
