package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match;

import cn.com.jrj.vtmatch.basicmatch.matchcore.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 大赛碎片管理表
 * </p>
 *
 * @author jobob
 * @since 2018-10-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("win_fragment")
public class Fragment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 栏目名称
     */
    private String fraName;

    /**
     * 类型 1 头像列表
     */
    private Integer fraType;

    /**
     * 栏目说明
     */
    private String fraDesc;

    /**
     * 碎片内容
     */
    private String content;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 删除标记（1 表示删除，0 表示未删除）
     */
    private Integer isDeleted;

    /**
     * 类型 1 页面 2 字符串
     */
    private Integer type;


}
