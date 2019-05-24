package cn.com.jrj.vtmatch.basicmatch.matchcore.vo;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * <p>
 * 账户比赛信息(包括用户信息,用户参加比赛信息,用户比赛统计信息等)
 * </p>
 */
@Data
@ApiModel("账户比赛信息模型")
public class AccountMatchInfo {
	/**
	 * 用户Id
	 */
	@ApiModelProperty(value = "用户Id")
	private long userId;
	/**
	 * 用户昵称
	 */
	@ApiModelProperty(value = "用户昵称")
	private String nickName;
	/**
	 * 用户头像
	 */
	@ApiModelProperty(value = "用户头像")
	private String headPic;
	/**
	 * 用户类型 1普通用户 2投顾
	 */
	@ApiModelProperty(value = "用户类型 1普通用户 2投顾")
	private int userType;
	/**
	 * 用户账号
	 */
	@ApiModelProperty(value = "用户账号")
	private String accountId;
	/**
	 * 加入时间
	 */
	@ApiModelProperty(value = "加入时间")
	private Date joinDate;
	
	/**
	 * 比赛Id
	 */
	@ApiModelProperty(value = "比赛Id")
	private long matchId;
	
	/**
	 * 比赛类型 1联赛 2 团队赛
	 */
	@ApiModelProperty(value = "比赛类型 1联赛 2 团队赛")
	private int matchType;
	
	/**
	 * 比赛名称
	 */
	@ApiModelProperty(value = "比赛名称")
	private String matchName;
	
	/**
	 * 比赛Id
	 */
	@ApiModelProperty(value = "加入战队Id")
	private Long joinTeamId;
	
	/**
	 * 总收益
	 */
	@ApiModelProperty(value = "总收益")
	private Float totalYield;
	
	/**
	 * 总排名
	 */
	@ApiModelProperty(value = "总排名")
	private Integer totalRank;
	/**
	 * 月收益
	 */
	@ApiModelProperty(value = "月收益")
	private Float monthYield;
	
	/**
	 * 月排名
	 */
	@ApiModelProperty(value = "月排名")
	private Integer monthRank;
	/**
	 * 日收益
	 */
	@ApiModelProperty(value = "日收益")
	private Float dayYield;
	
	/**
	 * 日排名
	 */
	@ApiModelProperty(value = "日排名")
	private Integer dayRank;

}
