package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * <p>
 * 用户账户清算
 * </p>
 *
 * @author jobob
 * @since 2018-10-26
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("win_match_user_account_stat_rank")
public class MatchUserAccountStatRank extends StatRank {

    private static final long serialVersionUID = 1L;

    /**
     * 账户ID
     */
    private String accountId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 清算日期
     */
    private LocalDate statDate;
}
