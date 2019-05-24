package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 比赛账户日收益和排名
 * </p>
 *
 * @author jobob
 * @since 2018-10-25
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("win_match_user_day_stat_rank")
public class MatchUserDayStatRank extends StatRank {

    private static final long serialVersionUID = 1L;

    /**
     * 比赛ID
     */
    @TableId
    private Long matchId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 账户总收益
     */
    @TableId
    private String accountId;

}
