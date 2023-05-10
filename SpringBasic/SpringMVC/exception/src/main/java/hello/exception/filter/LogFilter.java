package hello.exception.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String requestURI = servletRequest.getRequestURI();

        String uuid = UUID.randomUUID().toString();
        try {
            log.info("REQUEST [{}][{}][{}]", uuid, request.getDispatcherType(), requestURI);
            chain.doFilter(servletRequest, response);
        } catch (Exception e) {
            log.info("EXCEPTION {}", e.getMessage());
            throw e;
        } finally {
            log.info("RESPONSE [{}][{}][{}]", uuid, request.getDispatcherType(), requestURI);
        }
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
