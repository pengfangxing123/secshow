package cn.com.jrj.vtmatch.basicmatch.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import cn.com.jrj.vtmatch.basicmatch.dto.OpenCloseResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author yuan.cheng
 */
@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExchangeCalendarServiceTest {
    @Autowired
    private ExchangeCalendarService exchangeCalendarService;
    @MockBean
    private RestTemplate restTemplate;

    private final ResourceLoader resourceLoader = new DefaultResourceLoader();
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void isTradeDayTest() throws IOException {
        LocalDate date = LocalDate.of(2018, 10, 8);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("http://vt.jrjc.cloud/exchangecalendar/1/openclose")
            .queryParam("endDate", date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json;charset=utf-8");
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        OpenCloseResult openCloseResult = objectMapper.readValue(
            resourceLoader.getResource("open-close-20181008.json").getInputStream(), OpenCloseResult.class);
        ResponseEntity<OpenCloseResult> entityResult = new ResponseEntity<>(openCloseResult, HttpStatus.OK);
        when(restTemplate.exchange(builder.toUriString(),HttpMethod.GET, entity, OpenCloseResult.class))
            .thenReturn(entityResult);
        boolean result = exchangeCalendarService.isTradeDay(date);
        assertThat(result).isTrue();
    }

}