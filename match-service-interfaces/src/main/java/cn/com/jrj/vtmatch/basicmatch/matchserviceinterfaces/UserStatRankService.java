package cn.com.jrj.vtmatch.basicmatch.matchserviceinterfaces;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.UserStatRank;
import cn.com.jrj.vtmatch.basicmatch.matchcore.enums.RankType;
import cn.com.jrj.vtmatch.basicmatch.vo.UserStatRankList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @author yuan.cheng
 */
public interface UserStatRankService {

    /**
     * 用户排名查询
     *
     * @param userId 用户ID
     * @param type   类型 {@link RankType}
     * @param page   分页 {@link Page}
     * @return {@link UserStatRankList}
     */
    UserStatRankList queryUserStatRankList(Long userId, RankType type, Page<UserStatRank> page);

}
