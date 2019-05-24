package cn.com.jrj.vtmatch.basicmatch.helper;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Collection;
import java.util.concurrent.TimeUnit;


@Slf4j
public class StringRedisHelper {
	
	private StringRedisTemplate stringRedisTemplate;
	
	public StringRedisHelper(StringRedisTemplate stringRedisTemplate){
		this.stringRedisTemplate = stringRedisTemplate;
	}

	public void set(String key, String value){
		stringRedisTemplate.boundValueOps(key).set(value);
	}
	
	public void set(String key, String value,long expireTime){
		stringRedisTemplate.boundValueOps(key).set(value,expireTime,TimeUnit.SECONDS);
	}
	
	public boolean setIfAbsent(String key, String value,long expireTime){
		boolean b = stringRedisTemplate.boundValueOps(key).setIfAbsent(value);
		if(b){
			stringRedisTemplate.boundValueOps(key).expire(expireTime, TimeUnit.SECONDS);
		}
		return b;
	}
	
	public boolean exist(String key){
		return stringRedisTemplate.hasKey(key);
	}
	
	public boolean delete(String key){
		return stringRedisTemplate.delete(key);
	}
	
	public long delete(Collection<String> keys){
		 return stringRedisTemplate.delete(keys);
	}
	
	public String get(String key){
		return stringRedisTemplate.boundValueOps(key).get();
	}
	
	public JSONObject getJson(String key){
		String value = stringRedisTemplate.boundValueOps(key).get();
		if(StringUtils.isBlank(value)){
			return null;
		}
		
		try {
			return JSONObject.parseObject(value);
		} catch (Exception e) {
			// TODO: handle exception
			log.info("parse JsonObject fail  date" + value);
		}
		return null;
	}
	
	
}
