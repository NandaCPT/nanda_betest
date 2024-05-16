package com.nanda.betest.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nanda.betest.dto.AccountDTO;
import com.nanda.betest.entity.AccountLoginEntity;
import com.nanda.betest.entity.RedisSession;
import com.nanda.betest.entity.UserInfoEntity;
import com.nanda.betest.service.AccountService;
import com.nanda.betest.service.RedisService;
import com.nanda.betest.utils.JWTUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {
	
	@Autowired
	AccountService service;
	
	@Autowired
	RedisService redisService;
	
	@PostMapping("/insert")
    public ResponseEntity<Map<String, Object>> insertAccountUser(@RequestBody AccountDTO accountUserDTO) {
		Map<String, Object> map = new HashMap<>();
		try {
	        service.insertAccountUser(accountUserDTO);
	        map.put("status", true);
	        map.put("message", "Insert Data Berhasil");
			return new ResponseEntity<>(map, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			map.put("status", false);
			map.put("message", "Something Wrong!");
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);	
		}

    }
	
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody AccountDTO data) throws Exception{
		
		Map<String, Object> map = new HashMap<>();
		HttpHeaders headers = new HttpHeaders();
		String token;
		
		try {
			Long count = service.countAccountByUserPass(data.getUserName(), data.getPassword());
			
			if(count > 0) {
				AccountLoginEntity accountLogin = service.findUserPass(data.getUserName(), data.getPassword());
				UserInfoEntity userInfo = service.findUserInfoByUserId(accountLogin.getUserId());
				token = JWTUtils.generateTokenByNik(data.getUserName());
		        headers.add("Authorization", "Bearer " + token);
		        
		        RedisSession redisSession = new RedisSession();
		        redisSession.setToken(token);
		        redisSession.setUsername(accountLogin.getUserName());
		        redisSession.setUserInfoEntity(userInfo);
		        redisService.saveSession(redisSession);
		        
		        Date current_date = new Date();
				SimpleDateFormat current_sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String lastLogin = current_sdf.format(current_date);
				service.updateLastLogin(lastLogin, data.getUserName());
		        
		        map.put("status", true);
		        map.put("message", "Login Berhasil");
		        map.put("data", redisSession);
				return new ResponseEntity<>(map, HttpStatus.OK);
				
			}else {
				map.put("status", false);
				map.put("message", "Invalid Username/Password!");
				return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			map.put("status", false);
			map.put("message", "Something Wrong!");
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);	
		}

	}
	
	@GetMapping({"/accountNumber/{accountNumber}", "/registrationNumber/{registrationNumber}"})
    public ResponseEntity<Map<String, Object>>  getAccountUserDetailsByAccountNumber(@PathVariable(required = false) String accountNumber,@PathVariable(required = false) String registrationNumber,
    		HttpServletRequest req) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<UserInfoEntity> result = new ArrayList<UserInfoEntity>();
		String token = req.getHeader("Authorization");
		Boolean validation = checkToken(token);
		if(validation) {
			if (accountNumber != null) {
				result = service.getAccountByAccountOrRegistNumber(accountNumber, null);
			} else {
				result = service.getAccountByAccountOrRegistNumber(null, registrationNumber);
			}
			
			 map.put("status", true);
		     map.put("message", "Get Data Berhasil");
		     map.put("data", result);
		     return new ResponseEntity<>(map, HttpStatus.OK);
		}else {
			map.put("status", false);
			map.put("message", "Token Expired!");
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}
    }
	
	@GetMapping("/lastLogin")
    public ResponseEntity<Map<String, Object>>  getAccountUserDetailsByLastLogin(HttpServletRequest req) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<UserInfoEntity> result = new ArrayList<UserInfoEntity>();
		String token = req.getHeader("Authorization");
		Boolean validation = checkToken(token);
		if(validation) {
			result = service.getAccountByLastLogin();
			
			 map.put("status", true);
		     map.put("message", "Get Data Berhasil");
		     map.put("data", result);
		     return new ResponseEntity<>(map, HttpStatus.OK);
		}else {
			map.put("status", false);
			map.put("message", "Token Expired!");
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}
    }
	
	public Boolean checkToken(String token) {
			if(token == null) {
				return false;
			}
			token = JWTUtils.convertRawToken(token);
			RedisSession redisSession = redisService.findByToken(token);
			if (redisSession != null) {
				return true;
			}else {
				return false;
			}
		}
			

}
