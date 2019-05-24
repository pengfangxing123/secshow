package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 微信参赛记录
 * </p>
 *
 * @author jobob
 * @since 2018-11-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("win_wechat_team_join")
public class WechatTeamJoin extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 比赛ID
     */
    private Long matchId;

    /**
     * 团队ID
     */
    private Long teamId;

    /**
     * 微信唯一ID
     */
    private String unionId;

    /**
     * 微信公众号唯一ID
     */
    private String openId;

    /**
     * 状态0未处理 1已处理
     */
    private Integer status;


}
