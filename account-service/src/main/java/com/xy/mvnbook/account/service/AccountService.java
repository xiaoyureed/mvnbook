package com.xy.mvnbook.account.service;

import com.xy.mvnbook.account.service.bean.SignUpRequest;
import com.xy.mvnbook.account.service.exception.AccountServiceException;
/**
 * 封装后暴露的接口
 *
 * @version 0.1
 * @author xy
 * @date 2018年4月11日 下午7:50:44
 */
public interface AccountService {

    String generateCaptchaKey() throws AccountServiceException;
    byte[] generateCaptchaImage(String captchaKey) throws AccountServiceException;
    void activate(String activateNum) throws AccountServiceException;
    void login(String id, String pwd) throws AccountServiceException;
    void singUp(SignUpRequest rqt) throws AccountServiceException;
}
