package cn.com.jrj.vtmatch.basicmatch.matchteam.config;

import cn.com.jrj.vtmatch.basicmatch.config.StringRedisConfig;

public class TeamCache {
	
		//交易日缓存标识
		private static final String USER_JOIN_TEAM_INFO_KEY = StringRedisConfig.REDIS_CACHE_KEY_PREFIX + "user_join_team_info_";
		private static final String USER_TEAM_RANK_INFO_KEY = StringRedisConfig.REDIS_CACHE_KEY_PREFIX + "user_team_rank_info_";
		private static final String DISCORVERY_TEAM_KEY = StringRedisConfig.REDIS_CACHE_KEY_PREFIX + "discorvery_team_";
		
		
		public static final long LONG_TIME_OUT  =  60*30;
		
		public static final long SHORT_TIME_OUT  =  15;
		
		public static final long BIG_LONG_TIME_OUT  =  7200;
		
		
		public static String getUserJoinTeamInfoKey(long matchId,long userId){
			StringBuffer sb = new StringBuffer(USER_JOIN_TEAM_INFO_KEY);
			sb.append(matchId).append("_").append(userId);
			return sb.toString();
		}
		
		public static String getUserTeamRankInfoKey(long teamId,long userId){
			StringBuffer sb = new StringBuffer(USER_TEAM_RANK_INFO_KEY);
			sb.append(teamId).append("_").append(userId);
			return sb.toString();
		}
		
		public static String getDiscorveryTeamKey(long matchId){
			return DISCORVERY_TEAM_KEY+matchId;
		}
		
}
