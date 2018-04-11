package com.xy.mvnbook.account.captcha;

import java.util.List;

import com.xy.mvnbook.account.captcha.exception.AccountCaptchaException;
/**
 * 验证码服务 interface
 *
 * @version 0.1
 * @author xy
 * @date 2018年3月25日 下午10:18:02
 */
public interface AccountCaptchaService {

    /**
     * 获取 key
     * @return
     * @throws AccountCaptchaException
     */
    String generageCaptchaKey() throws AccountCaptchaException;
    /**
     * 根据key获取img
     * @param key
     * @return
     * @throws AccountCaptchaException
     */
    byte[] genarateCaptchaImage(String key) throws AccountCaptchaException;
    /**
     * 验证
     * @param key
     * @param value
     * @return
     * @throws AccountCaptchaException
     */
    boolean validate(String key, String value) throws AccountCaptchaException;
    /**
     * 获取预定义的验证码内容, 方便测试
     * @return
     */
    List<String> getPreText();
    void setPreText(List<String> preText);
    
}
