package cn.com.jrj.vtmatch.basicmatch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yuan.cheng
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MatchTeamRule {
    @JsonProperty("constitute_num")
    private Integer teamMemberThreshold;
    @JsonProperty("start_stat_ratio")
    private Integer teamMemberCalculateRate;
}
