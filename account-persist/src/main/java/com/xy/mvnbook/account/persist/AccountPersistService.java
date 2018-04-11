package com.xy.mvnbook.account.persist;

import com.xy.mvnbook.account.persist.dto.Account;
import com.xy.mvnbook.account.persist.exception.AccountPersistException;
/**
 * 持久化 interface, crud方法
 *
 * @version 0.1
 * @author xy
 * @date 2018年3月25日 下午6:08:15
 */
public interface AccountPersistService {

    Account createAccount(Account account) throws AccountPersistException;
    boolean deleteAccount(String id) throws AccountPersistException;
    Account updateAccount(Account account) throws AccountPersistException;
    Account readAccount(String id) throws AccountPersistException;
}
