var pojo={
		url:{
			loginvalidate:"http://localhost:8080/The_vertical_and_horizontal/admin_loginvalidate",
			loginSuccess:"http://localhost:8080/The_vertical_and_horizontal/admin_loginSuccess"
		},
		admin:{
//			登录
			login:function(paramsNode){
			 	paramsNode.form.validate({
						//自定义规则
								 rules:{
									 admin_account:{
										 required:true
									 },
									admin_password:{
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
			}
		}
}