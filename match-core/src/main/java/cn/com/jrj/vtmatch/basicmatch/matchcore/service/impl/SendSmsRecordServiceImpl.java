package cn.com.jrj.vtmatch.basicmatch.matchcore.service.impl;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.SendSmsRecord;
import cn.com.jrj.vtmatch.basicmatch.matchcore.dao.SendSmsRecordMapper;
import cn.com.jrj.vtmatch.basicmatch.matchcore.service.ISendSmsRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 发送短信记录表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-11-27
 */
@Service
public class SendSmsRecordServiceImpl extends ServiceImpl<SendSmsRecordMapper, SendSmsRecord> implements ISendSmsRecordService {

}
