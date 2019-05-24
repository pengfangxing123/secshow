package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 比赛规则明细表
 * </p>
 *
 * @author jobob
 * @since 2018-10-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("win_match_rule")
public class MatchRule extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 比赛ID
     */
    @TableField("matchId")
    private Long matchId;

    /**
     * 键
     */
    private String key;

    /**
     * 值
     */
    private String value;

    /**
     * 1有效，其他无效
     */
    private Boolean validStatus;

    /**
     * 二进制值
     */
    private Integer bina;


}
