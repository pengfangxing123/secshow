package cn.com.jrj.vtmatch.basicmatch.matchuser.helper;

import cn.com.jrj.vtmatch.basicmatch.helper.StringRedisHelper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchUserMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.utils.CacheConstants;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.UserAndAccountInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserHelper {

    private final MatchUserMapper matchUserMapper;
    private final StringRedisHelper stringRedisHelper;

    @Cacheable(
            value = CacheConstants.USER_REDIS_INTERFACE_CACHE_KEY,
            key = "#userId",
            condition = "#userId!=null and  #userId>0",
            unless = "#result==null")
    public UserAndAccountInfo getUserAndAccountInfoByUserId(Long userId) {
        return matchUserMapper.queryUserInfoAndAccountInfoByUserId(userId);
    }

    public void clearUserAndAccountInfoCache(Long userId){
        if(userId != null && userId > 0){
            stringRedisHelper.delete(CacheConstants.USER_REDIS_INTERFACE_CACHE_KEY+"::"+userId);
        }
    }
}
