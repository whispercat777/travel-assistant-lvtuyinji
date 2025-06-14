package org.example.tmplan.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 全局 CORS（跨域资源共享）过滤器配置类。
 * 用于允许浏览器从不同源访问后端接口，解决前后端分离部署下的跨域请求问题。
 */
public class corsFilter {

    /**
     * 注册 CORS 过滤器 Bean。
     * 允许所有来源、方法和请求头的跨域请求，并支持携带 Cookie。
     *
     * @return CorsFilter 实例，应用于整个项目的所有接口路径
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 允许所有来源的请求（生产环境建议指定前端地址）
        corsConfiguration.addAllowedOrigin("*");

        // 允许跨域请求携带 Cookie（需前后端都设置 withCredentials）
        corsConfiguration.setAllowCredentials(true);

        // 允许所有 HTTP 方法：GET、POST、PUT、DELETE 等
        corsConfiguration.addAllowedMethod("*");

        // 允许所有请求头
        corsConfiguration.addAllowedHeader("*");

        // 将配置应用于所有路径
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        // 返回配置好的过滤器
        return new CorsFilter(source);
    }
}
