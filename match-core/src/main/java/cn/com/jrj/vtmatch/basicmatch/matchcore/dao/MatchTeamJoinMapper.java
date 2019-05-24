package cn.com.jrj.vtmatch.basicmatch.matchcore.dao;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchTeamJoin;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.MatchTeamInfo;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.UserJoinTeamInfo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface MatchTeamJoinMapper extends BaseMapper<MatchTeamJoin> {

    List<MatchTeamJoin> selectAllJoinUser(@Param("matchId")Long matchId, @Param("date") LocalDateTime date, @Param("joinStatus") Integer joinStatus);

    UserJoinTeamInfo queryUserJoinTeamInfo(@Param("matchId")Long matchId, @Param("userId")Long userId);

    /**
     * 实时查询用户加入战队人数
     * @param matchId 比赛ID
     * @param minTeamMemberNum  成团最少人数
     * @return list of {@link MatchTeamJoin}
     */
    List<MatchTeamInfo> queryStatTeamMemberNum(@Param("matchId")Long matchId, @Param("minTeamMemberNum")int minTeamMemberNum);

}
