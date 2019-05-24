package cn.com.jrj.vtmatch.basicmatch.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

/**
 * @author yuan.cheng
 */
@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class MatchStatDataServiceTest {
    @Autowired
    MatchStatDataService matchStatDataService;

    @Test
    public void loadData() {
        matchStatDataService.loadMatchUserPrevData(1L, LocalDate.of(2018, 10,8));
    }

}