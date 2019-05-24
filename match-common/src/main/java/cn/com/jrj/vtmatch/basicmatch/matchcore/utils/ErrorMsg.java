package cn.com.jrj.vtmatch.basicmatch.matchcore.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMsg {

    public static final String SYSTEM_EXCEPTION = "系统保存异常，请稍后重试";

    public static final String USER_JOINED_ERROR = "您已参加了该比赛的战队，请勿重复报名";
    
    public static final String USER_NO_JOINED_MATCH_ERROR = "团队赛未报名，请您先报名";

    public static final String USER_JOIN_OTHER_TEAM_ERROR = "您参加了其他比赛战队，请先离开原战队，再加入";

    public static final String ALREADY_JOINING_SYSTEM_ERROR = "您已报名了该战队，请勿重复报名";
    
    public static final String USER_NO_JOINED_TEAM_ERROR = "您未参加此团队，请确认";
    
    public static final String MASTER_DOT_QUIT_TEAM_ERROR = "团队创建者，不能离开战队";

    public static final String MATCH_MAY_BE_DELETE_ERROR = "比赛已不能参加";
}
