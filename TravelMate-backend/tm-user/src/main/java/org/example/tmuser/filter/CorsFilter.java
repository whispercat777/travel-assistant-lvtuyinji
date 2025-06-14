package org.example.tmuser.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * 全局跨域资源共享（CORS）配置类。
 * 该类通过注册 CorsFilter Bean，允许前端跨域访问后端接口，适用于前后端分离部署场景。
 */
public class CorsFilter {

    /**
     * 注册 CorsFilter Bean，配置跨域请求策略。
     *
     * @return 跨域过滤器实例，应用于所有请求路径
     */
    @Bean
    public org.springframework.web.filter.CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 允许所有来源（*），可根据需要指定具体域名，如 "http://localhost:8080"
        corsConfiguration.addAllowedOrigin("*");

        // 允许发送 Cookie（必须前端也设置 withCredentials = true）
        corsConfiguration.setAllowCredentials(true);

        // 允许所有的 HTTP 方法：GET、POST、PUT、DELETE 等
        corsConfiguration.addAllowedMethod("*");

        // 允许所有请求头
        corsConfiguration.addAllowedHeader("*");

        // 将上述配置应用于所有路径
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        // 返回配置好的 CorsFilter 实例
        return new org.springframework.web.filter.CorsFilter(source);
    }
}
