package cn.com.jrj.vtmatch.basicmatch.matchcore.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * <p>
 * 账户资金信息模型
 * </p>
 */
@Data
@ApiModel("账户资金信息模型")
public class AccountFundInfo {
	@ApiModelProperty(value = "用户账号")
	private String accountId;
	
	@ApiModelProperty(value = "初始资金")
	private BigDecimal beginBalance;
	
	@ApiModelProperty(value = "当前资金=可用资金+冻结资金")
	private BigDecimal currentBalance;
	
	@ApiModelProperty(value = "冻结资金")
	private BigDecimal frozenBalance;
	
	@ApiModelProperty(value = "昨日资金")
	private BigDecimal lastBalance;
	
	@ApiModelProperty(value = "可用资金")
	private BigDecimal availableBalance;
	
//	/**
//	 * 周收益率
//	 */
//	private BigDecimal weekYield;
//	/**
//	 * 月收益率
//	 */
//	private BigDecimal monthYield;
	
}
