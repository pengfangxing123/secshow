package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.base.BaseEntity;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 调仓记录表
 * </p>
 *
 * @author jobob
 * @since 2018-11-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("win_trading")
public class Trading extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户SSOID
     */
    private Long userId;

    /**
     * 用户账户id
     */
    private String accountId;

    /**
     * 委托id
     */
    private Long commissionId;

    /**
     * 委托类型 0是买 1是卖 40 委托买入 41 委托卖出
     */
    private Integer commissionType;

    /**
     * 成交数量
     */
    private Integer commissionAmount;

    /**
     * 成交价格
     */
    private BigDecimal commissionPrice;

    /**
     * 成交费用
     */
    private BigDecimal tradeCost;

    /**
     * 起初持仓
     */
    private BigDecimal fromPosition;

    /**
     * 确认持仓
     */
    private BigDecimal toPosition;

    /**
     * 收益
     */
    private BigDecimal profit;

    /**
     * 股票代码
     */
    private String stockCode;

    /**
     * 股票名称
     */
    private String stockName;

    /**
     * 成交时间
     */
    private LocalDateTime commissionTime;

    /**
     * 成交时间
     */
    private LocalDateTime concludeTime;

    /**
     * 持仓id
     */
    private Long positionId;

    /**
     * 1 建仓 2 加仓 3 减仓 4 清仓
     */
    private Integer actionType;

    /**
     * 0 新建 1 已报 2 部撤 3 已撤 4 部成 5 已成 6 废单
     */
    private Integer commissionStatus;

    /**
     * 股票类型
     */
    private String stockType;

    /**
     * 市场类型
     */
    private String marketType;

    /**
     * 成交金额
     */
    private BigDecimal concludePrice;

    /**
     * 任务状态
     */
    private Integer taskStatus;

    /**
     * 是否盈利，null无盈利,0亏损,1盈利
     */
    private Integer isProfit;


}
