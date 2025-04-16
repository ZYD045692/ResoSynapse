package org.amm.config;

import org.amm.filter.JwtFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistration(JwtFilter filter) {
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/admin/*");
        registrationBean.addUrlPatterns("/adminPage/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE); // 高优先级
        return registrationBean;
    }
}
