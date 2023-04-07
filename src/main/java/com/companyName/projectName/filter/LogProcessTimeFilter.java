package com.companyName.projectName.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//javax.servlet.annotation 啟動類別 額外加上 @ServletComponentScan 標記
@WebFilter(urlPatterns = "/*", filterName = "logProcessTimeFilter")
@Slf4j
public class LogProcessTimeFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        //req.>>>>>>>, <<<<<<<resp.
        filterChain.doFilter(request, response);
        // performance
        long processTime = System.currentTimeMillis() - startTime;

        log.info("processTime= "+processTime + " ms");
//        System.out.println("processTime = "+processTime + " ms");

        int httpStatus = response.getStatus();
        String httpMethod = request.getMethod();
        String uri = request.getRequestURI();
        String params = request.getQueryString();

        if (params != null) {
            uri += "?" + params;
        }

        log.info("status path req.= "+String.join(" ", String.valueOf(httpStatus), httpMethod, uri));
//        System.out.println(String.join(" ", String.valueOf(httpStatus), httpMethod, uri));
    }
}
