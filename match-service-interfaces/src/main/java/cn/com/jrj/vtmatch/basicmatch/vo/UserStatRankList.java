package cn.com.jrj.vtmatch.basicmatch.vo;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.UserStatRank;
import lombok.Data;

import java.util.List;

/**
 * @author yuan.cheng
 */
@Data
public class UserStatRankList {
    /**
     * 个人信息
     */
    private UserStatRank self;

    /**
     * 排名列表
     */
    private List<UserStatRank> items;
}
