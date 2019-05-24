package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 登录记录表
 * </p>
 *
 * @author jobob
 * @since 2018-11-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("win_login_record")
public class LoginRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 委托id
     */
    private Long userId;

    /**
     * 微信openid或者unionid
     */
    private String wechatId;


}
