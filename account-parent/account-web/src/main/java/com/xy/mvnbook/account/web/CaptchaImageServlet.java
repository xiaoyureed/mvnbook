package com.xy.mvnbook.account.web;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.xy.mvnbook.account.service.AccountService;
import com.xy.mvnbook.account.service.exception.AccountServiceException;

/**
 * 返回captcha image
 *
 * @author xiaoyu
 * @version 0.0.1
 * @date 2018年4月12日
 */
public class CaptchaImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private WebApplicationContext context = null;

	@Override
	public void init() throws ServletException {
	    super.init();
	    context = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String key = request.getParameter("key");
	    if (null == key || 0 == key.length()) {
	        response.sendError(400, "jsp 没有传[key]");// client输入不正确
	    }
	    else {
	        AccountService service = context.getBean("accountService", AccountService.class);
	        try {
                byte[] imgByteArr = service.generateCaptchaImage(key);
                response.setContentType("image/jpeg");
                ServletOutputStream out = response.getOutputStream();
                out.write(imgByteArr);
                out.close();
            } catch (AccountServiceException e) {
                response.sendError(400, e.getMessage());
                e.printStackTrace();
            }
	    }
	    
//	    response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
