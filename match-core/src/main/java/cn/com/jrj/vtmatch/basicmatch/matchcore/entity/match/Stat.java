package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 收益类
 *
 * @author yuan.cheng
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Stat extends BaseEntity {
    /**
     * 日收益率
     */
    private BigDecimal dayYield;

    /**
     * 周收益率
     */
    private BigDecimal weekYield;

    /**
     * 月收益率
     */
    private BigDecimal monthYield;

    /**
     * 总收益率
     */
    private BigDecimal totalYield;
}
