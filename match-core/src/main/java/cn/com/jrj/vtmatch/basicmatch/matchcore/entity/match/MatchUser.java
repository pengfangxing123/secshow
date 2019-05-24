package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.base.BaseEntity;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author jobob
 * @since 2018-10-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("win_match_user")
public class MatchUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 第三方userId
     */
    private String passportId;

    /**
     * 微信openid
     */
    private String openId;

    /**
     * 微信unionid
     */
    private String unionId;

    /**
     * 头像
     */
    private String headPic;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 状态 默认为 1  正常   0删除
     */
    private Integer userStatus;

    /**
     * 简介
     */
    private String summary;

    /**
     * 用户类型 1普通用户 2投顾
     */
    private Integer type;

    /**
     * 手机号
     */
    private String phoneNo;

    /**
     * 最近登录时间
     */
    private LocalDateTime lastLoginTime;
    /**
     * 开户时间
     */
    private LocalDateTime gmtCreate;


}
