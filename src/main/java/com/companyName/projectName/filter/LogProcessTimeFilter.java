package com.companyName.projectName.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//javax.servlet.annotation 啟動類別 額外加上 @ServletComponentScan 標記
@WebFilter(urlPatterns = "/*", filterName = "logProcessTimeFilter")
public class LogProcessTimeFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        //req.>>>>>>>, <<<<<<<resp.
        filterChain.doFilter(request, response);
        // performance
        long processTime = System.currentTimeMillis() - startTime;

        System.out.println(processTime + " ms");

        int httpStatus = response.getStatus();
        String httpMethod = request.getMethod();
        String uri = request.getRequestURI();
        String params = request.getQueryString();

        if (params != null) {
            uri += "?" + params;
        }

        System.out.println(String.join(" ", String.valueOf(httpStatus), httpMethod, uri));
    }
}
