package cn.com.jrj.vtmatch.basicmatch.matchcore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 红包对账信息
 * @author hongtao.he
 *
 */
@Data
@ApiModel("红包对账信息模型")
public class SendRedDetailInfoVo {

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private long id;
	
	/**
	 * 用户id
	 */
	@ApiModelProperty(value = "用户id")
	private long userId;
	
	/**
	 * 昵称
	 */
	@ApiModelProperty(value = "昵称")
	private String nickName;
	
	/**
     * 手机号
     */
	@ApiModelProperty(value = "手机号")
    private String phoneNo;
	
	/**
	 * 比赛id
	 */
	@ApiModelProperty(value = "比赛id")
	private long matchId;
	
	/**
	 * 比赛名称
	 */
	@ApiModelProperty(value = "比赛名称")
	private String matchName;
	
	/**
	 * 战队id
	 */
	@ApiModelProperty(value = "战队id")
	private long teamId;
	
	/**
	 * 战队名称
	 */
	@ApiModelProperty(value = "战队名称")
	private String teamName;
	
	/**
     * 红包金额，单位：分
     */
	@ApiModelProperty(value = "红包金额，单位：分")
    private long cash;
	
	/**
     * 调用状态：0未成功，1已成功
     */
	@ApiModelProperty(value = "调用状态：0未成功，1已成功")
    private Integer redStatus;
	
	/**
     * 错误信息
     */
	@ApiModelProperty(value = "错误信息")
    private String errMsg;
	
	/**
     * 排名
     */
	@ApiModelProperty(value = "排名")
    private Integer rank;
}
