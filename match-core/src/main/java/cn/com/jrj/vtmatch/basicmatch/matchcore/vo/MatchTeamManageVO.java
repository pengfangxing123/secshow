package cn.com.jrj.vtmatch.basicmatch.matchcore.vo;

import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("后台战队信息模型")
public class MatchTeamManageVO {
    /**
     * 战队id
     */
    @ApiModelProperty(value = "战队id")
    private Long id;
    
    /**
     * 预留字段比赛ID
     */
    @ApiModelProperty(value = "预留字段比赛ID")
    private Long matchId;

    /**
     * 老师名称
     */
    @ApiModelProperty(value = "老师名称")
    private String masterName;
    
    /**
     * 老师类型
     */
    @ApiModelProperty(value = "老师类型")
    private String masterType;

    /**
     * 战队口号
     */
    @ApiModelProperty(value = "战队口号")
    private String declaration;
    /**
     * 战队用户数
     */
    @ApiModelProperty(value = "战队用户数")
    private String userNum;

    /**
     * 战队名称
     */
    @ApiModelProperty(value = "战队名称")
    private String teamName;

    /**
     * 1 表示删除，0 表示未删除
     */
    @ApiModelProperty(value = "是否已删除")
    private Integer isDeleted;

    /**
     * 0用户建战队，1投顾建战队
     */
    @ApiModelProperty(value = "战队类型")
    private Integer type;
    
    /**
     * 1 表示禁用，0 表示未禁用
     */
    @ApiModelProperty(value = "是否被禁用")
    private Integer isLocked;

    /**
     * 战队创建时间
     */
    @ApiModelProperty(value = "战队创建时间")
    private LocalDateTime gmtCreate;
}
