package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.base.BaseEntity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 大赛基本表
 * </p>
 *
 * @author jobob
 * @since 2018-11-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("win_match_basic")
@ApiModel(parent = BaseEntity.class)
public class MatchBasic extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 比赛所有者
     */
    @ApiModelProperty("比赛所有者")
    private Integer matchOwner;

    /**
     * 比赛名称
     */
    @ApiModelProperty("比赛名称")
    private String matchName;

    /**
     * 比赛列表背景图
     */
    @ApiModelProperty("比赛图片")
    private String matchPic;

    /**
     * 比赛图片URL
     */
    @ApiModelProperty("比赛图片URL")
    private String matchPicUrl;

    /**
     * 比赛首页头图
     */
    @ApiModelProperty("比赛首页头图")
    private String matchBanner;

    /**
     * 比赛首页头图URL
     */
    @ApiModelProperty("比赛首页头图URL")
    private String matchBannerUrl;

    /**
     * 比赛开始时间
     */
    @ApiModelProperty("比赛开始时间")
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate startDate;

    /**
     * 允许人数 默认0表示不限制
     */
    @ApiModelProperty("允许人数 默认0表示不限制")
    private Integer allowNum;

    /**
     * 比赛结束时间
     */
    @ApiModelProperty("比赛结束时间")
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate endDate;

    /**
     * 比赛状态 1未开始报名、2开始报名、3比赛开始、4比赛结束、0删除失效
     */
    @ApiModelProperty("比赛状态 1未开始报名、2开始报名、3比赛开始、4比赛结束、0删除失效")
    private Integer matchStatus;

    /**
     * 比赛类型 1联赛 2 团队赛
     */
    @ApiModelProperty("比赛类型 1联赛 2 团队赛")
    private Integer type;

    /**
     * 参加比赛开始时间
     */
    @ApiModelProperty("参加比赛开始时间")
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate applyStartDate;

    /**
     * 参加比赛结束时间
     */
    @ApiModelProperty("参加比赛结束时间")
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate applyEndDate;

    /**
     * 当前参赛人数
     */
    @ApiModelProperty("当前参赛人数")
    private Integer currNum;

    /**
     * 有效参赛人数
     */
    @ApiModelProperty("有效参赛人数")
    private Integer validateNum;

    /**
     * 比赛权重
     */
    @ApiModelProperty("比赛权重")
    private Integer weight;

    /**
     * 大赛说明
     */
    @ApiModelProperty("大赛说明")
    private String summary;

    /**
     * 奖励说明
     */
    @ApiModelProperty("奖励说明")
    private String awardDesc;

    /**
     * 规则说明
     */
    @ApiModelProperty("规则说明")
    private String ruleDesc;

    /**
     * 红包规则
     */
    @ApiModelProperty("红包规则")
    private String ruleConfig;

    /**
     * 预留字段1
     */
    @ApiModelProperty("预留字段1")
    private String bak1;

    /**
     * 预留字段1
     */
    @ApiModelProperty("预留字段2")
    private String bak2;
    
    
    /**
     * 能否可报名
     */
    @ApiModelProperty("是否可报名 0 不可   1可以")
    @TableField(exist = false)
    private int canJoin;

    /**
     * 删除标记（1 表示删除，0 表示未删除）
     */
    @ApiModelProperty("删除标记（1 表示删除，0 表示未删除）")
    private Integer isDeleted;

}
