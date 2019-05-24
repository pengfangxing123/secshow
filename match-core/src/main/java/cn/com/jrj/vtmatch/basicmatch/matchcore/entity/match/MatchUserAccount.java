package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 用户账户关系表
 * </p>
 *
 * @author jobob
 * @since 2018-10-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("win_match_user_account")
public class MatchUserAccount extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户账户id
     */
    private String accountId;

    /**
     * 初始资金（保留字段）
     */
    private BigDecimal initAmount;

    /**
     * 当前资金（保留字段）
     */
    private BigDecimal currentAmount;

    /**
     * 冻结资金（保留字段）
     */
    private BigDecimal frozenAmount;

    /**
     * 当前资金（保留字段）
     */
    private BigDecimal lastAmount;

    /**
     * 状态 1正常 0停止交易
     */
    private Integer accountStatus;

    /**
     * 交易次数
     */
    private Integer tradeTimes;

    /**
     * 账户类型 1普通账户
     */
    private Integer type;

    private Boolean valid;

    /**
     * 是否是默认账户 1 是 2 否
     */
    private Boolean isDefault;


}
