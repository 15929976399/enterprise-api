package com.chinalife.enterprise.interceptor;

import com.chinalife.enterprise.util.JwtHelper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2017-08-20.
 */
@Configuration
public class LoginUrlConfig extends WebMvcConfigurerAdapter {
    /**
     * 登录session key
     */
    @Autowired
    private Environment env;

    @Bean
    public SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());
        // 排除配置
        // 拦截配置
        addInterceptor.addPathPatterns("/api/**");
    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                throws Exception {
            // 请求的uri
            String url = request.getRequestURI();
            System.out.println(url);
            // 不过滤的uri
            String[] notFilter = new String[]{
                    "/login", "/nologin", "/error","validate","param","/pwd"
            };
            // 是否过滤
            boolean doFilter = true;
            for (String s : notFilter) {
                if (url.indexOf(s) != -1) {
                    // 如果uri中包含不过滤的uri，则不进行过滤
                    doFilter = false;
                    break;
                }
            }

            if (doFilter) {
                String auth = request.getHeader("Authorization");
                auth = LoginUrlConfig.extractJwtTokenFromAuthorizationHeader(auth);
                String base64Security = env.getProperty("base64Security");
                Claims claims = JwtHelper.parseJWT(auth, base64Security);
                if (claims == null) {
                    response.sendRedirect("nologin");
                    return false;
                }
                request.setAttribute("user_name", claims.get("user_name") + "");
                request.setAttribute("user_type", claims.get("user_type") + "");
                return true;
            } else {
                return true;
            }
        }
    }

    public static String extractJwtTokenFromAuthorizationHeader(String auth) {
        return auth.replaceFirst("[B|b][E|e][A|a][R|r][E|e][R|r] ", "").replace(" ", "");
    }
}
