package cn.com.jrj.vtmatch.basicmatch.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class FundInfo {
	
	/**
	 * 可用资金
	 */
	private BigDecimal availableBalance;
	
	/**
	 * 总资产
	 */
	private BigDecimal totalBalance;
	/**
	 * 总市值
	 */
	private BigDecimal marketValue;
	/**
	 * 总收益率
	 */
	private BigDecimal totalYieldrate;
	/**
	 * 当日收益率
	 */
	private BigDecimal todayYieldrate;
	/**
	 * 总盈亏金额
	 */
	private BigDecimal totalIncome;
	/**
	 * 当日盈亏金额
	 */
	private BigDecimal todayIncome;
	/**
	 * 周收益率
	 */
	private BigDecimal weekYield;
	/**
	 * 月收益率
	 */
	private BigDecimal monthYield;

}
