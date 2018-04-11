package com.xy.mvnbook.account.captcha.exception;

public class AccountCaptchaException extends Exception {

    /**
     */
    private static final long serialVersionUID = -1931995608010974023L;
    
    public AccountCaptchaException(String msg, Exception e) {
        super(msg, e);
    }

    public AccountCaptchaException(String msg) {
        super(msg);
    }
}
