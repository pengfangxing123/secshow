package cn.com.jrj.vtmatch.basicmatch.matchcore.service.impl;

import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchUserAccountDayStatRankMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUserAccountDayStatRank;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.UserStatRank;
import cn.com.jrj.vtmatch.basicmatch.matchcore.enums.RankType;
import cn.com.jrj.vtmatch.basicmatch.matchcore.service.IMatchUserAccountDayStatRankService;
import cn.com.jrj.vtmatch.basicmatch.matchcore.utils.CacheConstants;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户账户清算 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-10-26
 */
@RequiredArgsConstructor
@Service
public class MatchUserAccountDayStatRankServiceImpl extends ServiceImpl<MatchUserAccountDayStatRankMapper, MatchUserAccountDayStatRank> implements IMatchUserAccountDayStatRankService {

    private final MatchUserAccountDayStatRankMapper matchUserAccountDayStatRankMapper;

    @Cacheable(
            value =  CacheConstants.MATCH_REDIS_USER_RANK_LIST_KEY,
            key="T(String).valueOf(#page.current).concat('-').concat(#page.size).concat('-').concat(#type) ",
            unless = "#result==null or #result.records==null or #result.records.size()==0")
    @Override
    public IPage<UserStatRank> queryUserStatRank(Page<UserStatRank> page, RankType type) {
        IPage<UserStatRank> result;
        switch (type) {
            default:
            case DAY:
                result = matchUserAccountDayStatRankMapper.selectDayRankOrderPage(page);
                break;
            case WEEK:
                result = matchUserAccountDayStatRankMapper.selectWeekRankOrderPage(page);
                break;
            case MONTH:
                result = matchUserAccountDayStatRankMapper.selectMonthRankOrderPage(page);
                break;
            case TOTAL:
                result = matchUserAccountDayStatRankMapper.selectTotalRankOrderPage(page);
                break;
        }
        return result;
    }

    @Cacheable(value = CacheConstants.MATCH_REDIS_USER_RANK_KEY, key = "#userId + '::' + #type", unless = "#userId==null or #result==null")
    @Override
    public UserStatRank queryUserStatRankByUserId(Long userId, RankType type) {
        return matchUserAccountDayStatRankMapper.selectByUserId(userId);
    }

}
