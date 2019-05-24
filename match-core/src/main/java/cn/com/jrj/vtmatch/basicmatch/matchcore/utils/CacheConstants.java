package cn.com.jrj.vtmatch.basicmatch.matchcore.utils;

import cn.com.jrj.vtmatch.basicmatch.matchcore.enums.RankType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * cache key constants.
 *
 * @author lei.ning
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheConstants {

    /**
     * 刷新access token最小剩余秒数
     */
    public static final int MIN_ACCESSTOKEN_EXPIRATION_SECONDS = 10 * 60;
    /**
     * redis accesstoken key
     */
    public static final String REDIS_ACCESSTOKEN_KEY = "match:accesstoken";
    /**
     * redis session winuser key
     */
    public static final String REDIS_SESSION_USERS_KEY = "winuser";
    /**
     * redis cache key profix
     */
    public static final String REDIS_CACHE_KEY_PREFIX = "match:cacheable:";
    /**
     * redis cache key user
     */
    public static final String USER_REDIS_INTERFACE_CACHE_KEY = REDIS_CACHE_KEY_PREFIX + "user";
    /**
     * redis cache key tg user
     */
    public static final String TGUSER_REDIS_INTERFACE_CACHE_KEY = REDIS_CACHE_KEY_PREFIX + "tginfo";
    /**
     * redis cache key match
     */
    public static final String MATCH_REDIS_INTERFACE_CACHE_KEY = REDIS_CACHE_KEY_PREFIX + "match";
    /**
     * redis cache key team
     */
    public static final String TEAM_REDIS_INTERFACE_CACHE_KEY = REDIS_CACHE_KEY_PREFIX + "team";
    /**
     * redis cache key fragement
     */
    public static final String FRAGEMENT_REDIS_INTERFACE_CACHE_KEY = REDIS_CACHE_KEY_PREFIX + "fragement";
    /**
     * redis cache key fra
     */
    public static final String STAT_REDIS_INTERFACE_CACHE_KEY = REDIS_CACHE_KEY_PREFIX + "stat";
    /**
     * headimages fragment type
     */
    public static final int HEAD_IMAGES_FRAGMENT_TYPE = 1;

    /**
     * user rank key
     */
    public static final String MATCH_REDIS_USER_RANK_KEY = REDIS_CACHE_KEY_PREFIX + "user:rank";
    /**
     * user rank list key
     */
    public static final String MATCH_REDIS_USER_RANK_LIST_KEY = REDIS_CACHE_KEY_PREFIX + "user:rank:list";
    /**
     * redis cache key match
     */
    public static final String MATCH_REDIS_ACCOUNT_MATCH_KEY = REDIS_CACHE_KEY_PREFIX + "account_match";

    /**
     * redis cache key match team info
     */
    public static final String MATCH_REDIS_MATCH_TEAM_INFO_KEY = REDIS_CACHE_KEY_PREFIX + "match_team_info";
    /**
     * redis cache key match team info + userId
     */
    public static final String MATCH_REDIS_MATCH_TEAM_USER_INFO_KEY = REDIS_CACHE_KEY_PREFIX + "match_team_user_info";

    /**
     * redis cache key match
     */
    public static final String MATCH_REDIS_INDEXRED_CACHE_KEY = REDIS_CACHE_KEY_PREFIX + "matchIndexRed";

    /**
     * redis cache key team rank
     */
    public static final String MATCH_REDIS_TEAM_RANK_KEY = REDIS_CACHE_KEY_PREFIX + "team_rank_";
    /**
     * redis cache key sms code
     */
    public static final String SMS_REDIS_KEY_PREFIX = "match:sms:";
    /**
     * redis cache key team member rank
     */
    public static final String MATCH_REDIS_TEAM_MEMBER_RANK_KEY = REDIS_CACHE_KEY_PREFIX + "team_member_rank_";
    /**
     * redis cache key match member rank
     */
    public static final String MATCH_REDIS_MATCH_MEMBER_RANK_KEY = REDIS_CACHE_KEY_PREFIX + "team_match_member_rank_";
    /**
     * redis cache key user in  team  rank info
     */
    public static final String MATCH_REDIS_USER_TEAM_RANKINFO_KEY = REDIS_CACHE_KEY_PREFIX + "user_team_rankinfo_";
    /**
     * redis cache key user in  team  rank info
     */
    public static final String MATCH_REDIS_MATCH_USER_JOIN_INFO_KEY = REDIS_CACHE_KEY_PREFIX + "match_user_join_info";


    public static List<String> matchKeys(String accountId,long userId,long  matchId){
        return Arrays.asList(
                //红包展示列表
                MATCH_REDIS_INDEXRED_CACHE_KEY+"::list:"+matchId,
                MATCH_REDIS_ACCOUNT_MATCH_KEY+"::"+accountId,
                USER_REDIS_INTERFACE_CACHE_KEY+"::"+userId+"::"+matchId
        );
    }

    public static List<String> teamKeys(String accountId,long userId,long  matchId,long teamId){
        return Arrays.asList(
                USER_REDIS_INTERFACE_CACHE_KEY + "::" + userId + "::" + matchId,
                MATCH_REDIS_ACCOUNT_MATCH_KEY+"::"+accountId,
                MATCH_REDIS_USER_TEAM_RANKINFO_KEY + "::" + userId + "::" + teamId,
                MATCH_REDIS_MATCH_TEAM_USER_INFO_KEY + "::" + userId + "::" + teamId,
                "match:cacheable:match_user_join_info_" + matchId + "_" + userId,
                MATCH_REDIS_MATCH_TEAM_INFO_KEY + "::" + teamId
        );
    }

    public static List<String> userRankKeys(long userId){
        return Arrays.asList(
                MATCH_REDIS_USER_RANK_KEY+"::"+userId+"::"+ RankType.DAY,
                MATCH_REDIS_USER_RANK_KEY+"::"+userId+"::"+ RankType.WEEK,
                MATCH_REDIS_USER_RANK_KEY+"::"+userId+"::"+ RankType.MONTH,
                MATCH_REDIS_USER_RANK_KEY+"::"+userId+"::"+ RankType.TOTAL
                );
    }

    public static String discoveryTeamKey(long matchId){
        return "match:cacheable:discorvery_team_"+matchId;
    }

}
