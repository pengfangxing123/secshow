package cn.com.jrj.vtmatch.basicmatch.matchcore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 战队联合信息表(包括战队基本信息,战队统计信息,战队老师信息等)
 * </p>
 */
@Data
@ApiModel("比赛战队信息模型")
public class MatchTeamInfo {
	/**
	 * 战队老师用户Id
	 */
	@ApiModelProperty(value = "战队老师用户Id")
	private long masterId;
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
	 * 用户类型 1队长 2老师
	 */
	@ApiModelProperty(value = "用户类型 1队长 2老师")
	private int userType;
	/**
	 * Im用户id
	 */
	@ApiModelProperty(value = "Im用户id")
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
	 * 加入战队的TeamId
	 */
	@ApiModelProperty(value = "加入战队的TeamId 默认为0, 未加入任何战队;列表中全部显示0")
	private long joinTeamId = 0;
	/**
	 * 战队成员
	 */
	@ApiModelProperty(value = "战队成员")
	private int memberNum;
	/**
	 * 日收益率
	 */
	@ApiModelProperty(value = "日收益率")
	private Float dayYield;
	/**
	 * 昨日日排名
	 */
	@ApiModelProperty(value = "昨日日排名")
	private Integer lastDayRank;
	/**
	 * 日排名
	 */
	@ApiModelProperty(value = "日排名")
	private Integer dayRank;
	/**
	 * 日浮动排名
	 */
	@ApiModelProperty(value = "日浮动排名")
	private Integer dayTrend;
	/**
	 * 周收益
	 */
	@ApiModelProperty(value = "周收益")
	private Float weekYield;
	/**
	 * 昨日周排名
	 */
	@ApiModelProperty(value = "昨日周排名")
	private Integer lastWeekRank;
	/**
	 * 周排名
	 */
	@ApiModelProperty(value = "周排名")
	private Integer weekRank;
	/**
	 * 周浮动排名
	 */
	@ApiModelProperty(value = "周浮动排名")
	private Integer weekTrend;
	/**
	 * 月收益
	 */
	@ApiModelProperty(value = "月收益")
	private Float monthYield;
	/**
	 * 昨日月排名
	 */
	@ApiModelProperty(value = "昨日月排名")
	private Integer lastMonthRank;
	/**
	 * 月排名
	 */
	@ApiModelProperty(value = "月排名")
	private Integer monthRank;
	/**
	 * 月浮动排名
	 */
	@ApiModelProperty(value = "月浮动排名")
	private Integer monthTrend;
	/**
	 * 总收益
	 */
	@ApiModelProperty(value = "总收益")
	private Float totalYield;
	/**
	 * 昨日总排名
	 */
	@ApiModelProperty(value = "昨日总排名")
	private Integer lastTotalRank;
	/**
	 * 总排名
	 */
	@ApiModelProperty(value = "总排名")
	private Integer totalRank;
	/**
	 * 总浮动排名
	 */
	@ApiModelProperty(value = "总浮动排名")
	private Integer totalTrend;
	/**
	 * 大赛规则配置信息
	 */
	@ApiModelProperty(value = "大赛规则配置信息",hidden = true)
	private String ruleConfig;
	/**
	 * 团队记算收益所需人数
	 */
	@ApiModelProperty(value = "团队记算收益所需人数")
	private int teamRuleNum;

}
