package com.xy.mvnbook.account.persist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xy.mvnbook.account.persist.dto.Account;

/**
 * 测试
 *
 * @version 0.1
 * @author xy
 * @date 2018年3月25日 下午6:16:14
 */
public class AccountPersistServiceTest {

    private AccountPersistService service;
    
    @Before
    public void pre() throws Exception {
        File file = new File("target/test-classes/persist-data.xml");
        if (file.exists()) {
            file.delete();
        }
        
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("account-persist.xml");
        service = (AccountPersistService) context.getBean("accountPersistService");
        
        Account account = new Account();
        account.setId("id");
        account.setName("name");
        account.setPassword("password");
        account.setEmail("email");
        account.setActivated(true);
        
        service.createAccount(account);
    }
    
    @Test
    public void testReadAccount() throws Exception {
        Account account = service.readAccount("id");
        
        assertNotNull(account);
        assertEquals("id", account.getId());
        assertEquals("name", account.getName());
        assertEquals("password", account.getPassword());
        assertEquals("email", account.getEmail());
        assertTrue(account.isActivated());
    }
}

