package cn.com.jrj.vtmatch.basicmatch.matchcore.service.impl;

import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchUserAccountMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUser;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUserAccount;
import cn.com.jrj.vtmatch.basicmatch.matchcore.service.IMatchUserAccountService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户账户关系表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-10-26
 */
@Service
public class MatchUserAccountServiceImpl extends ServiceImpl<MatchUserAccountMapper, MatchUserAccount> implements IMatchUserAccountService {

}
