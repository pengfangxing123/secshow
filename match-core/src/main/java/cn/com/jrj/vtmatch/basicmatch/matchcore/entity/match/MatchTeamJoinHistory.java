package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 战队加入退出历史表
 * </p>
 *
 * @author jobob
 * @since 2018-10-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("win_match_team_join_history")
public class MatchTeamJoinHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 预留字段比赛ID
     */
    private Long matchId;

    /**
     * 战队ID
     */
    private Long teamId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户账户id
     */
    private String accountId;

    /**
     * 状态，1加入团队，0退出团队
     */
    private Integer joinStatus;


}
