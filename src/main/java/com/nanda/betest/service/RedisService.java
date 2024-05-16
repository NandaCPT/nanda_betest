package com.nanda.betest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nanda.betest.entity.RedisSession;
import com.nanda.betest.repository.RedisSessionRepository;

@Service
public class RedisService {
	
	@Autowired
	RedisSessionRepository repository;
	
	public RedisSession saveSession(RedisSession param) {
		System.out.println(param);
		return repository.save(param);
	}
	
	public RedisSession findByToken(String token) {
		return repository.findByToken(token);
	}

}
