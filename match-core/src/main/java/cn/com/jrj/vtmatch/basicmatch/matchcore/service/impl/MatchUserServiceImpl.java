package cn.com.jrj.vtmatch.basicmatch.matchcore.service.impl;

import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchUserMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUser;
import cn.com.jrj.vtmatch.basicmatch.matchcore.service.IMatchUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-10-26
 */
@Service
public class MatchUserServiceImpl extends ServiceImpl<MatchUserMapper, MatchUser> implements IMatchUserService {

}
