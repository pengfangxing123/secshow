package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.base.BaseEntity;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 战队加入退出情况表
 * </p>
 *
 * @author jobob
 * @since 2018-11-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("win_match_team_join")
public class MatchTeamJoin extends BaseEntity {

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
     * 加入时间
     */
    private LocalDateTime joinDate;

    /**
     * 退出时间
     */
    private LocalDateTime quitDate;

    /**
     * 状态，1目前正在团队，0已退出团队
     */
    private Integer joinStatus;


}
