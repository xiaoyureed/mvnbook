package com.xy.mvnbook.account.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.xy.mvnbook.account.captcha.AccountCaptchaService;
import com.xy.mvnbook.account.captcha.exception.AccountCaptchaException;
import com.xy.mvnbook.account.captcha.util.RandomGenerator;
import com.xy.mvnbook.account.email.AccountEmailService;
import com.xy.mvnbook.account.email.exception.AccountEmailException;
import com.xy.mvnbook.account.persist.AccountPersistService;
import com.xy.mvnbook.account.persist.dto.Account;
import com.xy.mvnbook.account.persist.exception.AccountPersistException;
import com.xy.mvnbook.account.service.AccountService;
import com.xy.mvnbook.account.service.bean.SignUpRequest;
import com.xy.mvnbook.account.service.exception.AccountServiceException;

import lombok.Data;
/**
 * 接口实现
 *
 * @version 0.1
 * @author xy
 * @date 2018年4月11日 下午7:51:32
 */
@Data
public class AccountServiceImpl implements AccountService {
    
    private AccountCaptchaService accountCaptchaService;
    private AccountEmailService accountEmailService;
    private AccountPersistService accountPersistService;
    
    /**
     * key为激活码, value为userId
     */
    private Map<String, String> activateMap = new HashMap<String, String>();
    
    
    public String generateCaptchaKey() throws AccountServiceException {
        try {
            return accountCaptchaService.generageCaptchaKey();
        } catch (AccountCaptchaException e) {
            throw new AccountServiceException("无法创建验证码key。", e);
        }
    }

    public byte[] generateCaptchaImage(String captchaKey) throws AccountServiceException {
        try {
            return accountCaptchaService.genarateCaptchaImage(captchaKey);
        } catch (AccountCaptchaException e) {
            throw new AccountServiceException("无法创建验证码image", e);
        }
    }

    public void activate(String activateNum) throws AccountServiceException {
        String accountId = this.getActivateMap().get(activateNum);
        if (accountId == null) {
            throw new AccountServiceException("无效的激活码。");
        }
        try {
            Account account = accountPersistService.readAccount(accountId);
            account.setActivated(true);
            accountPersistService.updateAccount(account);
        } catch (AccountPersistException e) {
            throw new AccountServiceException("无法激活。");
        }
    }

    public void login(String id, String pwd) throws AccountServiceException {
        try {
            Account account = accountPersistService.readAccount(id);
            if (account == null) {
                throw new AccountServiceException("account不存在");
            }
            if (!account.isActivated()) {
                throw new AccountServiceException("account未激活");
            }
            if (!account.getPassword().equals(pwd)) {
                throw new AccountServiceException("密码错误");
            }
        } catch (AccountPersistException e) {
            throw new AccountServiceException("无法登录.", e);
        }
        

    }

    public void singUp(SignUpRequest rqt) throws AccountServiceException {
        if (!rqt.getPwd().equals(rqt.getConfirmPwd())) {
            throw new AccountServiceException("两次输入的密码不一致。");
        }
        boolean validate;
        try {
            validate = accountCaptchaService.validate(rqt.getCaptchaKey(), rqt.getCaptchaValue());
        } catch (AccountCaptchaException e) {
            throw new AccountServiceException("验证码验证动作失败。", e);
        }
        if (!validate) {
            throw new AccountServiceException("验证码输入错误。");
        }
        
        Account account = new Account();
        account.setActivated(false);
        account.setEmail(rqt.getEmail());
        account.setId(rqt.getId());
        account.setName(rqt.getUsername());
        account.setPassword(rqt.getPwd());
        
        try {
            accountPersistService.createAccount(account);
        } catch (AccountPersistException e) {
            throw new AccountServiceException("保存account失败", e);
        }
        
        String activateId = RandomGenerator.getRandomString();
        activateMap.put(activateId, account.getId());
        
        try {
            accountEmailService.sendMail(account.getEmail(), "请激活。。。", rqt.getActivateServiceUrl().endsWith("/") ? 
                 rqt.getActivateServiceUrl() + activateId : rqt.getActivateServiceUrl() + "/" + activateId);
        } catch (AccountEmailException e) {
            throw new AccountServiceException("发送邮件失败。", e);
        }

    }

}
