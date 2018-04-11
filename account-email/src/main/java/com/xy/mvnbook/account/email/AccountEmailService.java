package com.xy.mvnbook.account.email;

import com.xy.mvnbook.account.email.exception.AccountEmailException;

/**
 * sendMail interface
 *
 * @version 0.1
 * @author xy
 * @date 2018年3月24日 下午5:29:20
 */
public interface AccountEmailService {
    
    /**
     * send email
     * 
     * @param to 接受地址
     * @param subject 主题
     * @param htmlText 正文
     * @throws AccountEmailException 邮件发送错误
     */
    void sendMail(String to, String subject, String htmlText) throws AccountEmailException;

}
