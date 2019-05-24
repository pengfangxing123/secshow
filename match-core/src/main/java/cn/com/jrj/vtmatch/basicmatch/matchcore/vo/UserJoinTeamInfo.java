package cn.com.jrj.vtmatch.basicmatch.matchcore.vo;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * <p>
 * 战队联合信息表(包括战队基本信息,战队统计信息,战队老师信息等)
 * </p>
 */
@Data
@ApiModel("用户加入战队模型")
public class UserJoinTeamInfo {
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
	 * 用户类型 1 普通 2老师
	 */
	@ApiModelProperty(value = "用户类型 1 普通 2老师")
	private int userType;
	/**
	 * Im系统用户id
	 */
	@ApiModelProperty(value = "Im系统用户id")
	private String imUserId;
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
	 * 战队名称
	 */
	@ApiModelProperty(value = "战队名称")
	private String teamName;
	/**
	 * 战队宣言
	 */
	@ApiModelProperty(value = "战队宣言")
	private String declaration;
	/**
	 * 战队Id
	 */
	@ApiModelProperty(value = "战队Id")
	private long teamId;
	/**
	 * 战队成员
	 */
	@ApiModelProperty(value = "战队成员")
	private Integer memberNum;
	/**
	 * 加入时间
	 */
	@ApiModelProperty(value = "加入时间")
	private Date joinDate;
	/**
	 * 离开时间
	 */
	@ApiModelProperty(value = "离开时间")
	private Date quitDate;
	/**
	 * 加入状态 1 加入 0 离开
	 */
	@ApiModelProperty(value = "加入状态 1 加入 0 离开")
	private int joinStatus;
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
