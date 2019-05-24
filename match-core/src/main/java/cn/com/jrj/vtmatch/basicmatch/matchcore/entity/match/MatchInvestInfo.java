package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 投顾信息记录表
 * </p>
 *
 * @author jobob
 * @since 2018-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("win_match_invest_info")
public class MatchInvestInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 券商投顾id
     */
    private String crmUserId;

    /**
     * 手机号
     */
    private String phoneNo;

    /**
     * 券商IM投顾id
     */
    private String imUserId;


}
