package com.xy.mvnbook.account.email.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.xy.mvnbook.account.email.AccountEmailService;
import com.xy.mvnbook.account.email.exception.AccountEmailException;

import lombok.Data;

/**
 * 'sendMail' implements
 *
 * @version 0.1
 * @author xy
 * @date 2018年3月24日 下午5:30:37
 */
@Data
public class AccountEmailServiceImpl implements AccountEmailService {
    
    /**
     *  sender, spring提供的简化邮件发送的util
     */
    private JavaMailSender sender;
    /**
     * 系统邮箱
     */
    private String systemEmail;

    public void sendMail(String to, String subject, String htmlText) throws AccountEmailException {
        MimeMessage msg = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg);
        try {
            helper.setFrom(systemEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlText);
            
            sender.send(msg);
        } catch (MessagingException e) {
            throw new AccountEmailException("Faild to send email.", e);
        }
    }

}
