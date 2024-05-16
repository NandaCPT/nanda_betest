package com.nanda.betest.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "AccountLogin")
public class AccountLoginEntity {
	
//    @Id
    private String accountId;
    private String userName;
    private String password;
    private String lastLoginDateTime;
    private String userId;

}
