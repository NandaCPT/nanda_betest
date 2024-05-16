package com.nanda.betest.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "UserInfo")
public class UserInfoEntity {
	
//	@Id
    private String userId;
    private String fullName;
    private String accountNumber;
    private String emailAddress;
    private String registrationNumber;

}
