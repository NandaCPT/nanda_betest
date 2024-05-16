package com.nanda.betest.repository;

import org.springframework.data.repository.CrudRepository;

import com.nanda.betest.entity.RedisSession;


public interface RedisSessionRepository extends CrudRepository<RedisSession, String> {

	RedisSession findByToken(String token);
}
