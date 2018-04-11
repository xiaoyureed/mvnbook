package com.xy.mvnbook.account.email.exception;

import javax.mail.MessagingException;

public class AccountEmailException extends Exception {
    private static final long serialVersionUID = -6256136479608938742L;
    
    public AccountEmailException(String msg, MessagingException e) {
        super(msg, e);
    }

}
