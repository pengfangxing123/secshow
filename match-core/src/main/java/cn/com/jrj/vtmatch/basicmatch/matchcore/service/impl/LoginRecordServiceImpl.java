package cn.com.jrj.vtmatch.basicmatch.matchcore.service.impl;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.LoginRecord;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.LoginRecordMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.service.ILoginRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登录记录表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-11-14
 */
@Service
public class LoginRecordServiceImpl extends ServiceImpl<LoginRecordMapper, LoginRecord> implements ILoginRecordService {

}
