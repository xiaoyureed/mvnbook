package com.xy.mvnbook.account.service.bean;

import lombok.Data;
/**
 * 请求对象
 *
 * @version 0.1
 * @author xy
 * @date 2018年4月11日 下午7:49:19
 */
@Data
public class SignUpRequest {

    private String id;
    private String email;
    private String username;
    private String pwd;
    private String confirmPwd;
    private String captchaKey;
    private String captchaValue;
    private String activateServiceUrl;
}
