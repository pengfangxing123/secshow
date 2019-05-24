package cn.com.jrj.vtmatch.basicmatch.matchcore.dao;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.MatchUser;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.UserAndAccountInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-10-26
 */
public interface MatchUserMapper extends BaseMapper<MatchUser> {

    UserAndAccountInfo queryUserInfoAndAccountInfoByMobile(@Param("mobile") String mobile);

    UserAndAccountInfo queryUserInfoAndAccountInfoByUserId(@Param("userId") Long userId);

    int updateLoginTime(@Param("userId") Long userId);

}
