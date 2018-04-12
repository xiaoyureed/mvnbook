package com.xy.mvnbook.account.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.xy.mvnbook.account.service.AccountService;
import com.xy.mvnbook.account.service.bean.SignUpRequest;
import com.xy.mvnbook.account.service.exception.AccountServiceException;

/**
 * 注册
 *
 * @author xiaoyu
 * @version 0.0.1
 * @date 2018年4月12日
 */
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	WebApplicationContext context = null;
	
	@Override
	public void init() throws ServletException {
	    super.init();
	    context = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    String id = req.getParameter("id");
	    String name = req.getParameter("name");
	    String email = req.getParameter("email");
	    String password = req.getParameter("password");
	    String confirmPassword = req.getParameter("confirm_password");
	    String captchaKey = req.getParameter("captcha_key");
	    String captchaValue = req.getParameter("captcha_value");
	    
	    if (StringUtils.hasText(id)
	            && StringUtils.hasText(name)
	            && StringUtils.hasText(email)
	            && StringUtils.hasText(password)
	            && StringUtils.hasText(confirmPassword)
	            && StringUtils.hasText(captchaKey)
	            && StringUtils.hasText(captchaValue)) {
	        AccountService service = context.getBean("accountService", AccountService.class);
	        SignUpRequest signUpReq = new SignUpRequest();
	        signUpReq.setId(id);
	        signUpReq.setUsername(name);
	        signUpReq.setEmail(email);
	        signUpReq.setPwd(password);
	        signUpReq.setConfirmPwd(confirmPassword);
	        signUpReq.setCaptchaKey(captchaKey);
	        signUpReq.setCaptchaValue(captchaValue);
	        
	        signUpReq.setActivateServiceUrl(this.getServletContext().getRealPath("/") + "activate");
	        
	        try {
                service.singUp(signUpReq);
                resp.getWriter().append("Served at: ").append(req.getContextPath()).append("\r\n").append(
                        "帐号创建好了, 到邮箱激活.");
                
            } catch (AccountServiceException e) {
                resp.sendError(400, e.getMessage());
                e.printStackTrace();
            }
	    }
	    else {
	        resp.sendError(400, "缺少请求参数");
	    }
	    
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
