package cn.com.jrj.vtmatch.basicmatch.config;

import cn.com.jrj.vtmatch.basicmatch.helper.StringRedisHelper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.utils.CacheConstants;
import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * string redis config
 *
 * @author lei.ning
 */
@Configuration
@ConditionalOnClass(value = {RedisOperations.class, RedisConnectionFactory.class, JSONObject.class})
//@ConditionalOnBean(StringRedisTemplate.class)
public class StringRedisConfig {

    @Bean
    public StringRedisHelper stringRedisHelper(StringRedisTemplate stringRedisTemplate) {
        return new StringRedisHelper(stringRedisTemplate);
    }

    public static final String REDIS_CACHE_KEY_PREFIX = CacheConstants.REDIS_CACHE_KEY_PREFIX;
}
