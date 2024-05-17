package com.nanda.betest.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.VariableOperators.Map;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.client.result.UpdateResult;
import com.nanda.betest.dto.AccountDTO;
import com.nanda.betest.entity.AccountLoginEntity;
import com.nanda.betest.entity.UserInfoEntity;

@Service
public class AccountService {
	
	@Autowired
	MongoTemplate mongoTemplate;

    @Transactional
    public void insertAccountUser(AccountDTO accountUserDTO) {
    	
        AccountLoginEntity accountLogin = new AccountLoginEntity();
        accountLogin.setAccountId(accountUserDTO.getAccountId());
        accountLogin.setUserName(accountUserDTO.getUserName());
        accountLogin.setPassword(accountUserDTO.getPassword());
        accountLogin.setLastLoginDateTime(accountUserDTO.getLastLoginDateTime());
        accountLogin.setUserId(accountUserDTO.getUserId());

        UserInfoEntity userInfo = new UserInfoEntity();
        userInfo.setUserId(accountUserDTO.getUserId());
        userInfo.setFullName(accountUserDTO.getFullName());
        userInfo.setAccountNumber(accountUserDTO.getAccountNumber());
        userInfo.setEmailAddress(accountUserDTO.getEmailAddress());
        userInfo.setRegistrationNumber(accountUserDTO.getRegistrationNumber());

        mongoTemplate.save(accountLogin);
        mongoTemplate.save(userInfo);
    }
    
    public List<UserInfoEntity> getAccountByAccountOrRegistNumber(String accountNumber, String registrationNumber) throws Exception {
    	
    	List<UserInfoEntity> userInfo = new ArrayList<UserInfoEntity>();
    	try {
    		Query newQuery = new Query();
    		if(accountNumber != null) {
    			newQuery.addCriteria(Criteria.where("accountNumber").is(accountNumber));
    		}else {
    			newQuery.addCriteria(Criteria.where("registrationNumber").is(registrationNumber));
    		}
    		
    		userInfo = mongoTemplate.find(newQuery, UserInfoEntity.class, "UserInfo");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
    	return userInfo;
    }
    
    public List<AccountLoginEntity> getAccountByLastLogin() throws Exception {
    	
    	List<AccountLoginEntity> userInfo = new ArrayList<AccountLoginEntity>();
    	try {
    		
    		LocalDateTime lastWeek = LocalDateTime.now().minusDays(3);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            String formattedDate = lastWeek.format(formatter);
            
    		Query newQuery = new Query();
    		newQuery.addCriteria(Criteria.where("lastLoginDateTime").lte(formattedDate));
    		
    		userInfo = mongoTemplate.find(newQuery, AccountLoginEntity.class, "AccountLogin");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
    	return userInfo;
    }
    
    public Long countAccountByUserPass(String username, String password) {
    	Query newQuery = new Query();
		newQuery.addCriteria(Criteria.where("username").is(username));
		newQuery.addCriteria(Criteria.where("password").is(password));
		
		Long count = mongoTemplate.count(newQuery, "AccountLogin");
    	return count;
    }
    
    public AccountLoginEntity findUserPass(String username, String password) {
    	Query newQuery = new Query();
		newQuery.addCriteria(Criteria.where("username").is(username));
		newQuery.addCriteria(Criteria.where("password").is(password));
		
		AccountLoginEntity accountLogin = mongoTemplate.findOne(newQuery, AccountLoginEntity.class, "AccountLogin");
    	return accountLogin;
    }
    
    public UserInfoEntity findUserInfoByUserId(String userId) {
    	Query newQuery = new Query();
		newQuery.addCriteria(Criteria.where("userId").is(userId));
		UserInfoEntity userInfo = mongoTemplate.findOne(newQuery, UserInfoEntity.class, "UserInfo");
    	return userInfo;
    }
    
    public void updateLastLogin(String lastLogin, String username) {
    	Query newQuery = new Query();
    	newQuery.addCriteria(Criteria.where("username").is(username));
    	Update update = new Update();
    	update.set("lastLoginDateTime", lastLogin);
    	
    	mongoTemplate.updateMulti(newQuery, update, "AccountLogin");
    }

}
