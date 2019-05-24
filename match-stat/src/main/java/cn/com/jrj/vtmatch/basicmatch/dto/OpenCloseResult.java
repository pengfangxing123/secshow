package cn.com.jrj.vtmatch.basicmatch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author yuan.cheng
 */
@Data
public class OpenCloseResult {
    @JsonProperty("open_close")
    private Integer openClose;
}
