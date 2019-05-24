package cn.com.jrj.vtmatch.basicmatch.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 战队基础信息表
 * 
 **/
@Data
@Accessors(chain = true)
public class MatchAccountUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 老师id
     */
    private Long userId;
    /**
     * 老师id
     */
    private String accountId;

}
