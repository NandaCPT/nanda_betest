package com.nanda.betest.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;

public class JWTUtils {
	
	protected static final Logger log = LogManager.getLogger(JWTUtils.class);
	private static Clock clock = DefaultClock.INSTANCE;
	private static byte[] SECRET = "n88oMyIUJh1F887ldHrqciYfSlQmoXmTb9yZcKjBQez1niATW7YW4c0CtL5aIjwa".getBytes();
	
	public static String generateTokenByNik(String nik) throws JsonProcessingException {
        Map<String, Object> claims = new HashMap();
        return doGenerateToken(claims, nik);
    }
	
	public static String convertRawToken(String rawToken) {
        if (rawToken != null && rawToken.startsWith("Bearer ")) {
            rawToken = rawToken.substring(7);
        } else {
            log.warn("couldn't find bearer string, will ignore the header");
        }

        return rawToken;
    }
	
	private static String doGenerateToken(Map<String, Object> claims, String subject) {
        Date createdDate = clock.now();
        Date expirationDate = calculateExpirationDate(createdDate);
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(createdDate).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }
	
	public static Date calculateExpirationDate(Date now){
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.HOUR, 24);
        return cal.getTime();
    }

}
