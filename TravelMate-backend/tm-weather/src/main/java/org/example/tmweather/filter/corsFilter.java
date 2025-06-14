package org.example.tmweather.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域资源共享（CORS）过滤器配置类。
 * 用于允许前端在不同域名/端口下访问后端接口，解决跨域请求问题。
 */
public class corsFilter {

    /**
     * 注册 CORS 跨域过滤器 Bean。
     * 允许所有来源、方法和请求头，并支持携带 Cookie。
     *
     * @return CorsFilter 实例，应用于所有请求路径
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 允许所有来源（生产环境建议限制具体域名）
        corsConfiguration.addAllowedOrigin("*");

        // 允许携带 Cookie（需前端同时配置 withCredentials 为 true）
        corsConfiguration.setAllowCredentials(true);

        // 允许所有请求方式（GET、POST、PUT、DELETE 等）
        corsConfiguration.addAllowedMethod("*");

        // 允许所有请求头
        corsConfiguration.addAllowedHeader("*");

        // 创建配置源并注册路径规则，/** 表示所有接口都支持跨域
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        // 返回配置好的跨域过滤器
        return new CorsFilter(source);
    }
}
