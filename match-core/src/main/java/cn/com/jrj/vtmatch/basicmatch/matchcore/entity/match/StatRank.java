package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 收益排名类
 *
 * @author yuan.cheng
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class StatRank extends Stat {
    /**
     * 日收益排名
     */
    private Integer dayRank;

    /**
     * 上一工作日日排名
     */
    private Integer lastDayRank;

    /**
     * 日排名趋势
     */
    private Integer dayTrend;

    /**
     * 周收益排名
     */
    private Integer weekRank;

    /**
     * 上一工作日周排名
     */
    private Integer lastWeekRank;

    /**
     * 周排名趋势
     */
    private Integer weekTrend;

    /**
     * 月收益排名
     */
    private Integer monthRank;

    /**
     * 上一工作日月排名
     */
    private Integer lastMonthRank;

    /**
     * 月排名趋势
     */
    private Integer monthTrend;

    /**
     * 总收益排名
     */
    private Integer totalRank;

    /**
     * 上一工作日总排名
     */
    private Integer lastTotalRank;

    /**
     * 总排名趋势
     */
    private Integer totalTrend;
}
