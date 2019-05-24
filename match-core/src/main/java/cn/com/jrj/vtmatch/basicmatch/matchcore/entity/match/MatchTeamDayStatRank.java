package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

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
@TableName("win_match_team_day_stat_rank")
public class MatchTeamDayStatRank extends StatRank {

    private static final long serialVersionUID = 1L;

    /**
     * 比赛ID
     */
    @TableId
    private Long matchId;

    /**
     * 战队ID
     */
    @TableId
    private Long teamId;

    /**
     * 战队人数
     */
    private Long memberNum;
}
