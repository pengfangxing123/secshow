package cn.com.jrj.vtmatch.basicmatch.matchcore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("比赛战队成员排行版模型")
public class MatchMemberRankVO {
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
	 * 用户类型 1普通用户 2老师
	 */
	@ApiModelProperty(value = "用户类型 1普通用户 2老师")
	private int userType;
	/**
	 * 用户账号
	 */
	@ApiModelProperty(value = "用户账号")
	private String accountId;
	/**
	 * 比赛Id
	 */
	@ApiModelProperty(value = "比赛Id")
	private long matchId;
	/**
	 * 战队Id
	 */
	@ApiModelProperty(value = "战队Id")
	private long teamId;
	/**
	 * 日收益率
	 */
	@ApiModelProperty(value = "日收益率")
	private Float dayYield;
	/**
	 * 日排名
	 */
	@ApiModelProperty(value = "日排名")
	private Integer dayRank;
	/**
	 * 周收益
	 */
	@ApiModelProperty(value = "周收益")
	private Float weekYield;
	/**
	 * 周排名
	 */
	@ApiModelProperty(value = "周排名")
	private Integer weekRank;
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
	 * 总收益
	 */
	@ApiModelProperty(value = "总收益")
	private Float totalYield;
	/**
	 * 总排名
	 */
	@ApiModelProperty(value = "总排名")
	private Integer totalRank;
	
}
