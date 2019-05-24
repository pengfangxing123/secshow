package cn.com.jrj.vtmatch.basicmatch.matchuser.service;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.UserStatRank;
import cn.com.jrj.vtmatch.basicmatch.matchcore.enums.RankType;
import cn.com.jrj.vtmatch.basicmatch.matchcore.service.IMatchUserAccountDayStatRankService;
import cn.com.jrj.vtmatch.basicmatch.matchserviceinterfaces.UserStatRankService;
import cn.com.jrj.vtmatch.basicmatch.vo.UserStatRankList;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author yuan.cheng
 */
@Service
@RequiredArgsConstructor
public class UserStatRankServiceImpl implements UserStatRankService {
    private final IMatchUserAccountDayStatRankService matchUserAccountDayStatRankService;

    @Override
    public UserStatRankList queryUserStatRankList(Long userId, RankType type, Page<UserStatRank> page) {
        UserStatRankList result = new UserStatRankList();
        if (null != userId) {
            UserStatRank self = matchUserAccountDayStatRankService.queryUserStatRankByUserId(userId, type);
            result.setSelf(self);
        }

        IPage<UserStatRank> pageResult = matchUserAccountDayStatRankService.queryUserStatRank(page, type);

        result.setItems(pageResult.getRecords());

        return result;
    }
}
