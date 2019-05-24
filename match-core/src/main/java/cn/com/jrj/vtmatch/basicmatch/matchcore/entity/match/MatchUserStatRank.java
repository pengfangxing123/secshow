package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * <p>
 * 比赛账户收益和排名
 * </p>
 *
 * @author jobob
 * @since 2018-10-25
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("win_match_user_stat_rank")
public class MatchUserStatRank extends StatRank {

    private static final long serialVersionUID = 1L;

    /**
     * 比赛ID
     */
    private Long matchId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 账户总收益
     */
    private String accountId;

    /**
     * 排名日期
     */
    private LocalDate statDate;

}
