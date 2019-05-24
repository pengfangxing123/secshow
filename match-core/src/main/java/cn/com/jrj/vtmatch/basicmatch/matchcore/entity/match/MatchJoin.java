package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.base.BaseEntity;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 帐户加入比赛表
 * </p>
 *
 * @author jobob
 * @since 2018-11-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("win_match_join")
public class MatchJoin extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 比赛ID
     */
    private Long matchId;

    /**
     * 用户账户id
     */
    private String accountId;

    /**
     * 加入比赛时间
     */
    private LocalDateTime joinDate;

    /**
     * 状态 1 表示删除，0 表示未删除
     */
    private Integer isDeleted;

    /**
     * 比赛结束状态 1未结束，0已结束
     */
    private Integer matchStatus;

    /**
     * 参赛后交易次数
     */
    private Integer tradeTimes;


}
