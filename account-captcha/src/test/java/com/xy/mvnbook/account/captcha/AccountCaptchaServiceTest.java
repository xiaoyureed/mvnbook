package com.xy.mvnbook.account.captcha;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xy.mvnbook.account.captcha.util.RandomGenerator;
/**
 * 测试
 *
 * @version 0.1
 * @author xy
 * @date 2018年3月25日 下午11:04:24
 */
public class AccountCaptchaServiceTest {
    
    private AccountCaptchaService service;
    
    @Before
    public void pre() throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("account-captcha.xml");
        service = (AccountCaptchaService)context.getBean("accountCaptchaService");
    }

    @Test
    public void testRandomString() throws Exception{
        HashSet<String> set = new HashSet<String>(100);
        for (int i=0; i<100; i++) {
            String random = RandomGenerator.getRandomString();
            assertFalse(set.contains(random));
            set.add(random);
        }
    }
    
    @Test
    public void testGenerateCaptcha() throws Exception {
        String key = service.generageCaptchaKey();
        assertNotNull(key);
        
        byte[] imgArray = service.genarateCaptchaImage(key);
        assertTrue(imgArray.length > 0);
        
        File imgFile = new File("target/" + key + ".jpg");
        FileOutputStream out = new FileOutputStream(imgFile);
        out.write(imgArray);
        if (out != null) {
            out.close();
        }
        assertTrue(imgFile.exists() && imgFile.length() > 0);
    }
    
    @Test
    public void testValidateCorrect() throws Exception{
        ArrayList<String> preDefinedText = new ArrayList<String>();
        preDefinedText.add("12345");
        preDefinedText.add("abcde");
        service.setPreText(preDefinedText);
        
        String key = service.generageCaptchaKey();
        service.genarateCaptchaImage(key);
        assertTrue(service.validate(key, "12345"));// 顺序读取preDefinedText的元素
        
        String key1 = service.generageCaptchaKey();
        service.genarateCaptchaImage(key1);
        assertTrue(service.validate(key1, "abcde"));
    }
    
    @Test
    public void testValidateIncorrect() throws Exception {
        ArrayList<String> preDefinedText = new ArrayList<String>();
        preDefinedText.add("12345");
        service.setPreText(preDefinedText);
        
        String key = service.generageCaptchaKey();
        service.genarateCaptchaImage(key);
        assertFalse(service.validate(key, "1234"));
    }
    
    
    
    
    
    
    
    
    
    
    
    
}
