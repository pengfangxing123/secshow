package cn.com.jrj.vtmatch.basicmatch.matchcore.vo;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * <p>
 * 用户加入比赛模型(包括用户信息,用户参加比赛信息等)
 * </p>
 */
@Data
@ApiModel("用户加入比赛模型")
public class UserMatchJoinVO {
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
	 * 比赛开始时间
	 */
	@ApiModelProperty(value = "比赛开始时间")
	private Date matchStartDate;
	/**
	 * 比赛Id
	 */
	@ApiModelProperty(value = "比赛Id")
	private long matchId;
	/**
	 * 比赛名称
	 */
	@ApiModelProperty(value = "比赛名称")
	private String matchName;
	/**
	 * 比赛图片
	 */
	@ApiModelProperty(value = "比赛图片")
	private String matchPic;
	/**
	 * 比赛类型 1联赛 2 团队赛
	 */
	@ApiModelProperty(value = "比赛类型 1联赛 2 团队赛")
	private int matchType;
	/**
	 * 总收益
	 */
	@ApiModelProperty(value = "总收益")
	private float totalYield;
	/**
	 * 总排名
	 */
	@ApiModelProperty(value = "总排名")
	private int totalRank;

}
