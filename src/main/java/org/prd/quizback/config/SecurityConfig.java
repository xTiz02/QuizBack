package org.prd.quizback.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*@Component
@Order(Ordered.HIGHEST_PRECEDENCE)*/
public class SecurityConfig implements Filter{
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(SecurityConfig.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        logger.info("Se realizo una peticion a la api: " + ((HttpServletRequest) req).getRequestURI());

        HttpServletResponse response= (HttpServletResponse) res;
        HttpServletRequest request= (HttpServletRequest) req;
        String origin = request.getHeader("origin");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH"); //permite que el navegador acceda a los metodos
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Max-Age", "3600"); //3600 segundos de cache, es decir que se guarda la respuesta en el navegador por 3600 segundos
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {//si el metodo es options se devuelve un ok
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}