package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * <p>
 * 发送短信记录表
 * </p>
 *
 * @author jobob
 * @since 2018-11-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("win_send_sms_record")
public class SendSmsRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 内容
     */
    private String content;

    /**
     * 状态 0发送成功，1发送失败
     */
    private Integer status;

    /**
     * 发送日期
     */
    private LocalDate sendDate;


}
