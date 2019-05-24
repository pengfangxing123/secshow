package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * <p>
 * 战队收益排行表
 * </p>
 *
 * @author jobob
 * @since 2018-10-25
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("win_match_team_stat_rank")
public class MatchTeamStatRank extends StatRank {

    private static final long serialVersionUID = 1L;

    /**
     * 比赛ID
     */
    private Long matchId;

    /**
     * 战队ID
     */
    private Long teamId;

    /**
     * 战队人数
     */
    private Long memberNum;

    /**
     * 排名日期
     */
    private LocalDate statDate;

}
