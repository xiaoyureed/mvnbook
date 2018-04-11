package com.xy.mvnbook.account.captcha.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.InitializingBean;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.xy.mvnbook.account.captcha.AccountCaptchaService;
import com.xy.mvnbook.account.captcha.exception.AccountCaptchaException;
import com.xy.mvnbook.account.captcha.util.RandomGenerator;

/**
 * 验证码服务 实现
 *
 * @version 0.1
 * @author xy
 * @date 2018年3月25日 下午10:22:10
 */
public class AccountCaptchaServiceImpl implements AccountCaptchaService, InitializingBean {
    /**
     * 验证码生成器
     */
    private DefaultKaptcha producer;
    /**
     * InitializingBean接口中的方法, 会在spring初始化bean时调用; 这里用来初始化  producer
     */
    public void afterPropertiesSet() throws Exception {
        producer = new DefaultKaptcha();
        producer.setConfig(new Config(new Properties()));
    }
    /**
     * 存储key和验证码正确的值
     */
    private Map<String, String> captchaMap = new HashMap<String, String>();
    /**
     * 预定义字符串, 方便测试
     */
    private List<String> preText;

    public String generageCaptchaKey() throws AccountCaptchaException {
        String key = RandomGenerator.getRandomString();
        String value = this.getCaptchaText();
        captchaMap.put(key, value);
        return key;
    }

    public byte[] genarateCaptchaImage(String key) throws AccountCaptchaException {
        String value = captchaMap.get(key);
        if (null == value) {
            throw new AccountCaptchaException("Captcha key: " + key + "not found.");
        }
        BufferedImage img = producer.createImage(value);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "jpg", out);
        } catch (IOException e) {
            throw new AccountCaptchaException("Faild to write captcha stream.", e);
        }
        return out.toByteArray();
    }

    public boolean validate(String key, String value) throws AccountCaptchaException {
        String text = captchaMap.get(key);
        if (null == text) {
            throw new AccountCaptchaException("Captcha key: " + key + "not found.");
        }
        if (value.equals(text)) {
            captchaMap.remove(key);
            return true;
        }
        else {
            return false;
        }
    }

    public List<String> getPreText() {
        return this.getPreText();
    }

    public void setPreText(List<String> preText) {
        this.preText = preText;
    }
    
    private int count = 0;
    private String getCaptchaText() {
        // 如果 预定义验证码文本 != null, 则顺序循环该字符串列表读取值, 否则, 随机创建一个串
        if (preText != null && !preText.isEmpty()) {
            String text = preText.get(count);
            count = (count + 1) % preText.size();
            return text;
        } else {
            return producer.createText();
        }
    }


}
