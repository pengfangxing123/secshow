package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 红包发放明细表
 * </p>
 *
 * @author jobob
 * @since 2018-11-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("win_send_red_detail")
public class SendRedDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

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
     * 红包金额，单位：分
     */
    private long cash;

    /**
     * 用户手机号
     */
    private String phoneNo;

    /**
     * 红包类型：S分享红包，P抢红包
     */
    private String redType;

    /**
     * 调用状态：0未成功，1已成功
     */
    private Integer redStatus;

    /**
     * 错误信息
     */
    private String errMsg;


}
