package cn.com.jrj.vtmatch.basicmatch.service;

import cn.com.jrj.vtmatch.basicmatch.dto.OpenCloseResult;
import cn.com.jrj.vtmatch.basicmatch.exception.StatException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * 交易日历服务
 *
 * @author yuan.cheng
 */
@Service
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "vt")
public class ExchangeCalendarService {

    private final RestTemplate restTemplate;

    @Setter
    private String domain;

    public boolean isTradeDay(LocalDate date) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(domain + "/exchangecalendar/1/openclose")
            .queryParam("endDate", date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json;charset=utf-8");
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        HttpEntity<OpenCloseResult> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, OpenCloseResult.class);
        return Optional.ofNullable(result.getBody()).map(OpenCloseResult::getOpenClose).map(value -> value == 1)
            .orElseThrow(() -> new StatException("query open close from vt error"));
    }
}
