package cn.com.jrj.vtmatch.basicmatch.matchcore.service.impl;

import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchTeamJoinMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchTeamJoin;
import cn.com.jrj.vtmatch.basicmatch.matchcore.service.IMatchTeamJoinService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 战队加入退出情况表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-10-25
 */
@Service
public class MatchTeamJoinServiceImpl extends ServiceImpl<MatchTeamJoinMapper, MatchTeamJoin> implements IMatchTeamJoinService {

}
