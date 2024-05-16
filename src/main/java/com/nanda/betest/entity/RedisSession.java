package com.nanda.betest.entity;

import java.util.concurrent.TimeUnit;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@RedisHash
public class RedisSession {
	
	@JsonIgnore
    @TimeToLive(unit = TimeUnit.HOURS)
    private Long timeout = 24L;
	
	@JsonIgnore
    @Id
    private String username;
    
    @JsonProperty
    @Indexed
    private String token;
    
    @JsonProperty("userInfo")
    private UserInfoEntity userInfoEntity;

}
