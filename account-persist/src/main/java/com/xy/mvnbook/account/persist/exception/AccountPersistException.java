package com.xy.mvnbook.account.persist.exception;

/**
 * 自定义异常
 *
 * @version 0.1
 * @author xy
 * @date 2018年3月25日 下午6:12:51
 */
public class AccountPersistException extends Exception {

    public AccountPersistException(String msg, Exception e) {
        super(msg, e);
    }

    private static final long serialVersionUID = -7205545969802653298L;

}
