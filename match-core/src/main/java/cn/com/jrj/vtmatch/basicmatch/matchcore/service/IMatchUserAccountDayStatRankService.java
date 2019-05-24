package cn.com.jrj.vtmatch.basicmatch.matchcore.service;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUserAccountDayStatRank;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.UserStatRank;
import cn.com.jrj.vtmatch.basicmatch.matchcore.enums.RankType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户账户清算 服务类
 * </p>
 *
 * @author jobob
 * @since 2018-10-26
 */
public interface IMatchUserAccountDayStatRankService extends IService<MatchUserAccountDayStatRank> {

    IPage<UserStatRank> queryUserStatRank(Page<UserStatRank> page, RankType type);

    UserStatRank queryUserStatRankByUserId(Long userId, RankType type);
}
