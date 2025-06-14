package org.example.tmuser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 配置类。
 * 用于向 Spring 容器中注入一个 RestTemplate 实例，方便其他组件进行 HTTP 请求调用。
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 注册 RestTemplate Bean。
     * RestTemplate 是 Spring 提供的同步 HTTP 客户端，可用于调用其他服务的 REST 接口。
     *
     * @return RestTemplate 实例
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
