package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yuan.cheng
 */
@Data
public class UserStatRank {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 头像
     */
    private String headPic;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 收益率
     */
    private BigDecimal yield;

    /**
     * 排名
     */
    private Integer rank;

    /**
     * 排名趋势
     */
    private Integer trend;
}
