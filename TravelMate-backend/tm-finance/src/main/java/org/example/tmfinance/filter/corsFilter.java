package org.example.tmfinance.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 全局 CORS 跨域过滤器配置类。
 * 允许所有来源、方法和请求头的跨域访问，用于解决前后端分离部署时的跨域问题。
 */
public class corsFilter {

    /**
     * 注册一个全局跨域过滤器。
     * 配置允许任意来源、任意方法、任意请求头，并支持携带 Cookie。
     *
     * @return CorsFilter 实例，用于统一处理跨域请求
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 允许所有源（前端地址），生产环境可替换为指定前端地址如 "https://example.com"
        corsConfiguration.addAllowedOrigin("*");

        // 允许发送 Cookie 等凭证信息
        corsConfiguration.setAllowCredentials(true);

        // 允许所有 HTTP 方法（如 GET、POST、PUT、DELETE 等）
        corsConfiguration.addAllowedMethod("*");

        // 允许所有请求头
        corsConfiguration.addAllowedHeader("*");

        // 注册跨域配置，应用于所有路径
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        // 返回配置好的 CORS 过滤器
        return new CorsFilter(source);
    }
}
