package cn.com.jrj.vtmatch.basicmatch.matchcore.vo;

import lombok.Data;

/**
 * <p>
 * 一创红包
 * </p>
 *
 * @author jobob
 * @since 2018-11-30
 */
@Data
public class YcRedDetailVO {

	private Long id;
    /**
     * 预留字段比赛ID
     */
    private Long matchId;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 团队id
     */
    private Long teamId;

    /**
     * 排名
     */
    private Integer rank;

    /**
     * 微信openid
     */
    private String openId;

    /**
     * unionId
     */
    private String unionId;

    /**
     * 红包金额，单位：分
     */
    private String cash;

    /**
     * 用户手机号
     */
    private String phoneNo;

    /**
     * 红包类型：1
     */
    private String redType;

    /**
     * 调用状态：0未成功，1已成功
     */
    private Integer redStatus;

    /**
     * 比赛名称
     */
    private String matchName;
    
    /**
     * 比赛开始时间
     */
    private String startDate;
    
    /**
     * 比赛结束时间
     */
    private String endDate;

}
