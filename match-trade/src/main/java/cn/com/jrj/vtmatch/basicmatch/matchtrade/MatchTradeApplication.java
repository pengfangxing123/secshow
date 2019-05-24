package cn.com.jrj.vtmatch.basicmatch.matchtrade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ComponentScans(
        @ComponentScan("cn.com.jrj.vtmatch")
)
public class MatchTradeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatchTradeApplication.class, args);
	}
}
