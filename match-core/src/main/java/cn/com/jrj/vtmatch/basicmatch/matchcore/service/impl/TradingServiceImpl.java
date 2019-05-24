package cn.com.jrj.vtmatch.basicmatch.matchcore.service.impl;

import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.TradingMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.Trading;
import cn.com.jrj.vtmatch.basicmatch.matchcore.service.ITradingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 调仓记录表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-10-25
 */
@Service
public class TradingServiceImpl extends ServiceImpl<TradingMapper, Trading> implements ITradingService {

}
