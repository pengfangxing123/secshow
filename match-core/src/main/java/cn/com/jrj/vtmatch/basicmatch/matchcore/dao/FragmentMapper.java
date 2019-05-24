package cn.com.jrj.vtmatch.basicmatch.matchcore.dao;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.Fragment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 大赛碎片管理表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-10-26
 */
public interface FragmentMapper extends BaseMapper<Fragment> {

    /**
     * 根据类型查询所有碎片内容
     * @param fra_type
     * @return
     */
    List<String> selectAllContentByFraType(@Param("fra_type")int fra_type);

}
