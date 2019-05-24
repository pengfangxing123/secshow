package cn.com.jrj.vtmatch.basicmatch.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class FundStatement {
	
	/**
	 * 可用资金
	 */
	private BigDecimal availableBalance;
	
	/**
	 * 冻结资金
	 */
	private BigDecimal frozenBalance;
	
	/**
	 * 初始资金
	 */
	private BigDecimal beginBalance;
	
	/**
	 * 现有资金
	 */
	private BigDecimal currentBalance;
	
	/**
	 * 昨日资金
	 */
	private BigDecimal lastBalance;
	
	/**
	 * 总市值
	 */
	private BigDecimal marketValue;
	
	/**
	 * 昨日总收益率
	 */
	private BigDecimal lastTotalYieldrate;
	
	/**
	 * 当日收益率
	 */
	private BigDecimal lastTodayYieldrate;

	/**
	 * 昨日盈亏额
	 */
	private BigDecimal lastTodayIncome;
	/**
	 * 周收益率
	 */
	private BigDecimal weekYield;
	/**
	 * 月收益率
	 */
	private BigDecimal monthYield;

}
