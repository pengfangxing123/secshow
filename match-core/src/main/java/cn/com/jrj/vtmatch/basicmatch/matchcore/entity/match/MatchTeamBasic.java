package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 战队基础信息表
 * </p>
 *
 * @author jobob
 * @since 2018-10-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("win_match_team_basic")
public class MatchTeamBasic extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 预留字段比赛ID
     */
    private Long matchId;

    /**
     * 老师id
     */
    private Long masterId;

    /**
     * 用户账户id
     */
    private String accountId;

    /**
     * 战队名称
     */
    private String teamName;

    /**
     * 战队宣言
     */
    private String declaration;

    /**
     * 战队人数
     */
    private Integer memberNum;

    /**
     * 1 表示删除，0 表示未删除
     */
    private Integer isDeleted;

    /**
     * 0用户建战队，1投顾建战队
     */
    private Integer type;
    
    /**
     * 1 表示禁用，0 表示未禁用
     */
    private Integer isLocked;

}
