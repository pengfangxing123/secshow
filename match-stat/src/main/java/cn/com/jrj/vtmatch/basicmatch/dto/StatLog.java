package cn.com.jrj.vtmatch.basicmatch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author yuan.cheng
 */
@Data
public class StatLog {
    @JsonProperty("uuid")
    private String accountId;
    @JsonProperty("date")
    private Instant statDate;
    @JsonProperty("dayYieldRate")
    private BigDecimal dayYield;
    @JsonProperty("weekYieldRate")
    private BigDecimal weekYield;
    @JsonProperty("monthYieldRate")
    private BigDecimal monthYield;
    @JsonProperty("totalYieldRate")
    private BigDecimal totalYield;
}
