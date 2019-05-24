package cn.com.jrj.vtmatch.basicmatch.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDate;

/**
 * @author yuan.cheng
 */
@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class VirtualTradeDataServiceTest {

    @Autowired
    VirtualTradeDataService virtualTradeDataService;

    @Test
    public void loadDayStatLog() throws IOException {
        LocalDate date = LocalDate.of(2018, 9, 28);
        virtualTradeDataService.loadDayStatLog(date);
    }

}