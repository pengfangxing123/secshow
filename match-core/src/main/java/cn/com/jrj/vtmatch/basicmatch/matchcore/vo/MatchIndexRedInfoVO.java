package cn.com.jrj.vtmatch.basicmatch.matchcore.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * <p>
 * 比赛首页红包模型(包括红包总数,红包列表,参赛数量等)
 * </p>
 */
@Data
@ApiModel("比赛首页红包模型")
public class MatchIndexRedInfoVO {
	/**
	 * 红包总数
	 */
	@ApiModelProperty(value = "红包总数")
	private int totalAmount;
	
	/**
	 * 参赛人数
	 */
	@ApiModelProperty(value = "参赛人数")
	private int joinCount;
	/**
	 * 单个红包
	 */
	@ApiModelProperty(value = "单个红包")
	private float preRed;
	/**
	 * 近20位参赛
	 */
	@ApiModelProperty(value = "")
	private List<UserMatchJoinVO> joinList;
	/**
	 * 单个红包
	 */
	@ApiModelProperty(value = "显示奖金的最小人数")
	private int showRedMinPeopleNum;
	
	

}
