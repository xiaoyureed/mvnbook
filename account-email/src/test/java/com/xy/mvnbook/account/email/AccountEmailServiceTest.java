package com.xy.mvnbook.account.email;

import static org.junit.Assert.assertEquals;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import com.xy.mvnbook.account.email.exception.AccountEmailException;

/**
 * 测试
 *
 * @version 0.1
 * @author xy
 * @date 2018年3月24日 下午6:00:36
 */
public class AccountEmailServiceTest {

    private GreenMail green;
    
    @Before
    public void startGreen() {
        // 基于smtp协议初始化greenmail
        green = new GreenMail(ServerSetup.SMTP);
        green.setUser("test@xiaoyu.com", "123456");// 创建账户
        green.start(); // 默认监听25端口
    }
    
    @Test
    public void testSendMail() {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("account-email.xml");
        AccountEmailService service = (AccountEmailService) ctx.getBean("accountEmailService");
        try {
            service.sendMail("test@xiaoyu.com", "Test subject", "<h3>Test</h3>");
//            service.sendMail("775000738@qq.com", "test-test", "<h3>Test</h3>text");
        } catch (AccountEmailException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        green.waitForIncomingEmail(2000, 1);// 接受一封邮件, 最多等待2s
        MimeMessage[] msgs = green.getReceivedMessages();
        assertEquals(1, msgs.length);
        try {
            assertEquals("Test subject", msgs[0].getSubject());
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals("<h3>Test</h3>", GreenMailUtil.getBody(msgs[0]).trim());
        ctx.close();
    }
    
    @After
    public void stopGreen() {
        green.stop();
    }
}
