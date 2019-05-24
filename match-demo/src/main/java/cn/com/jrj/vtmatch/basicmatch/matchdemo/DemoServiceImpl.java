package cn.com.jrj.vtmatch.basicmatch.matchdemo;

import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.MatchBasicMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.domain.DemoParams;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchBasic;
import cn.com.jrj.vtmatch.basicmatch.matchserviceinterfaces.DemoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * demo sevice
 *
 * @author lei.ning
 */
@Slf4j
@RequiredArgsConstructor
@Service("basicDemoService")
public class DemoServiceImpl implements DemoService {

    private final MatchBasicMapper matchBasicMapper;

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public String test(DemoParams params) {
        QueryWrapper<MatchBasic> winMatchBasicQueryWrapper = new QueryWrapper<>();
        IPage<MatchBasic> winMatchBasicIPage = matchBasicMapper.selectPage(new Page<>(1, 20), winMatchBasicQueryWrapper);
        log.info(winMatchBasicIPage.toString());
        log.info("DB OK!");
        stringRedisTemplate.boundValueOps("test").set("i am a test !");
        log.info("REDIS OK!");
        stringRedisTemplate.delete("test");
        log.info("REDIS CLEAN OK!");
        return "demo success";
    }
}
