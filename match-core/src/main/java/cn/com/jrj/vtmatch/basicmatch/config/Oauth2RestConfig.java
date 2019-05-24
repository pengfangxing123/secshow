package cn.com.jrj.vtmatch.basicmatch.config;

import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.client.RestTemplate;

/**
 * Oauth2 Rest Config
 *
 * @author yuan.cheng
 */
@Configuration
@AutoConfigureAfter({RestTemplateAutoConfiguration.class})
@ConditionalOnClass({RestTemplate.class, OAuth2RestTemplate.class})
public class Oauth2RestConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.security.oauth2.client")
    public ClientCredentialsResourceDetails jrjCenterAuthDetails() {
        return new ClientCredentialsResourceDetails();
    }

    @Bean
    @ConditionalOnMissingBean(OAuth2RestTemplate.class)
    public OAuth2RestTemplate oAuth2RestTemplate(RestTemplateBuilder builder) {
        return builder.requestFactory(() -> new HttpComponentsClientHttpRequestFactory(HttpClients.custom()
            //.disableRedirectHandling()
            .disableCookieManagement()
            .setMaxConnPerRoute(600)
            .setMaxConnTotal(1200)
            .build()))
            .configure(new OAuth2RestTemplate(jrjCenterAuthDetails()));
    }

}