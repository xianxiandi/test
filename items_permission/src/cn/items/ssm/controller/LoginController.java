package cn.items.ssm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.items.ssm.exception.CustomException;
import cn.items.ssm.po.ActiveUser;
import cn.items.ssm.service.SysService;


@Controller
public class LoginController {
	
	@Autowired
	private SysService sysService;
	
	
	//用户登陆提交方法
	/**
	 * 
	 * <p>Title: login</p>
	 * <p>Description: </p>
	 * @param session
	 * @param randomcode 输入的验证码
	 * @param usercode 用户账号
	 * @param password 用户密码 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/login")
	public String login(HttpServletRequest request)throws Exception{
		
//		
//		// shiroLoginFaiure
		
		String exceptionName = (String) request.getAttribute("shiroLoginFailure");
		if(exceptionName!=null)
		{
			if(UnknownAccountException.class.getName().equals(exceptionName))
			{
				throw new CustomException("账号不存在"); // 该异常会被springmvc的异常处理器收集统一处理，跳转到error.jsp 给出提示
			}
			else if(IncorrectCredentialsException.class.getName().equals(exceptionName))
			{
				throw new CustomException("密码错误");
			}
			else
			{
				throw new Exception();
			}
		}
		return "login";

	}
	

	//用户退出
/*	@RequestMapping("/logout")
	public String logout(HttpSession session)throws Exception{
		
		//session失效
		session.invalidate();
		//重定向到商品查询页面
		return "redirect:/first.action";
		
	}*/
	

}
