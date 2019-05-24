package cn.com.jrj.vtmatch.basicmatch.matchcore.service.impl;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchTeamJoinHistory;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchTeamJoinHistoryMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.service.IMatchTeamJoinHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 战队加入退出历史表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-10-31
 */
@Service
public class MatchTeamJoinHistoryServiceImpl extends ServiceImpl<MatchTeamJoinHistoryMapper, MatchTeamJoinHistory> implements IMatchTeamJoinHistoryService {

}
