package cn.com.jrj.vtmatch.basicmatch.matchcore.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 战队基础信息表
 * 
 **/
@Data
@ApiModel("比赛团队基础模型")
public class MatchTeamBasicVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 团队赛Id
     */
    @ApiModelProperty(value = "团队赛Id")
    private Long id = 0L;
    
    /**
     * 字段比赛ID
     */
    @ApiModelProperty(value = "比赛ID")
    private Long matchId;

    /**
     * 老师id
     */
    @ApiModelProperty(value = "老师id")
    private Long masterId;

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
     * 1 表示删除，0 表示未删除
     */
    @ApiModelProperty(value = "1 表示删除，0 表示未删除")
    private Integer isDeleted;

    /**
     * 0用户建战队，1投顾建战队
     */
    @ApiModelProperty(value = "0用户建战队，1投顾建战队")
    private Integer type;
    
    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String headPic;
    
    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    private String nickName;


}
