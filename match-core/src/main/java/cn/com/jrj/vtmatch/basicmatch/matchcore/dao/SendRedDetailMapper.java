package cn.com.jrj.vtmatch.basicmatch.matchcore.dao;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match.SendRedDetail;
import cn.com.jrj.vtmatch.basicmatch.matchcore.vo.SendRedDetailInfoVo;

/**
 * <p>
 * 红包发放明细表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-10-25
 */
public interface SendRedDetailMapper extends BaseMapper<SendRedDetail> {

	IPage<SendRedDetailInfoVo> selectRedDetailPage(Page page);
	
	int updateRedStatus(@Param("id") long id, @Param("openId") String  openId,@Param("phoneNo") String  phoneNo);
}
