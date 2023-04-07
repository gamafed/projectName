package com.companyName.projectName.filter;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogApiFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
    ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
    filterChain.doFilter(requestWrapper, responseWrapper);

    logAPI(request, response);
    logBody(requestWrapper, responseWrapper);
    responseWrapper.copyBodyToResponse();
  }

  private void logAPI(HttpServletRequest request, HttpServletResponse response) {
    // 先前打印 API 的程式
  }

  private String getContent(byte[] content) {
    String body = new String(content);
    return body.replaceAll("[\n\t]", "");
  }

  private void logBody(
      ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
    String requestBody = getContent(request.getContentAsByteArray());
    System.out.println("Request: " + requestBody);

    String responseBody = getContent(response.getContentAsByteArray());
    System.out.println("Response: " + responseBody);
  }
}
