package cn.com.jrj.vtmatch.basicmatch.matchcore.vo;

import lombok.Data;

/**
 * 用户及用户账户数据
 * @author NL
 */
@Data
public class UserAndAccountInfo {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 手机号
     */
    private String moblie;
    /**
     * 微信openId
     */
    private String openId;
    /**
     * 微信unionId
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
     * 用户类型 1普通用户 2投顾
     */
    private int type;
    /**
     * 金牛账户ID
     */
    private String innerAccountId;
    /**
     * 账户ID
     */
    private Long accountId;
    /**
     * 是否是默认账户 1 是 2 否
     */
    private Long isDefault;

}
