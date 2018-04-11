package com.xy.mvnbook.account.persist.dto;

import lombok.Data;
/**
 * dto
 *
 * @version 0.1
 * @author xy
 * @date 2018年3月25日 下午6:15:11
 */
@Data
public class Account {

    private String id;
    private String name;
    private String password;
    private String email;
    private boolean activated;
}
