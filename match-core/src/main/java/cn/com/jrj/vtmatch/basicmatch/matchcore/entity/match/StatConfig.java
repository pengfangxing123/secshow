package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 清算配置表
 * </p>
 *
 * @author jobob
 * @since 2018-10-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("win_stat_config")
public class StatConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 配置key
     */
    private String key;

    /**
     * key对应的value
     */
    private String value;

    /**
     * 状态0：false 1:true
     */
    private Boolean valid;


}
