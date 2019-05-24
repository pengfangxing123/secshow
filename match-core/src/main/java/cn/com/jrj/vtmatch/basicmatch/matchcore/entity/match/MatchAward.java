package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 比赛奖励表
 * </p>
 *
 * @author jobob
 * @since 2018-10-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("win_match_award")
public class MatchAward extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 比赛ID
     */
    private Long matchId;

    private Integer brank;

    private Integer erank;

    /**
     * 奖品
     */
    private String award;

    /**
     * 奖品类型
     */
    private Integer awardType;

    private Integer issueType;

    /**
     * 1有效，其他无效
     */
    private Boolean validStatus;


}
