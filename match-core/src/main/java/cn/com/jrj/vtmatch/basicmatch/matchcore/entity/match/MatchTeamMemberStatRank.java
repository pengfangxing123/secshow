package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * <p>
 * 团队成员收益排行表
 * </p>
 *
 * @author jobob
 * @since 2018-10-25
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("win_match_team_member_stat_rank")
public class MatchTeamMemberStatRank extends StatRank {

    private static final long serialVersionUID = 1L;

    /**
     * 预留字段比赛ID
     */
    private Long matchId;

    /**
     * 团队Id
     */
    private Long teamId;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 用户账户id
     */
    private String accountId;

    /**
     * 排名日期
     */
    private LocalDate statDate;

}
