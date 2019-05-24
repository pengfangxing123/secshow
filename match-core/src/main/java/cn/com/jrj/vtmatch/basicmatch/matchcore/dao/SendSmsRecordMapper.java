package cn.com.jrj.vtmatch.basicmatch.matchcore.dao;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.SendSmsRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 发送短信记录表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-11-27
 */
public interface SendSmsRecordMapper extends BaseMapper<SendSmsRecord> {

    int countSendNumByDate(@Param("mobile") String mobile, @Param("sendDate")String sendDate);

}
