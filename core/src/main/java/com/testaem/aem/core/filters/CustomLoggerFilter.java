package com.testaem.aem.core.filters;

import org.apache.sling.engine.EngineConstants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

@Component(service = Filter.class,
        property = {
                EngineConstants.SLING_FILTER_SCOPE + "=" + EngineConstants.FILTER_SCOPE_REQUEST,
        })
public class CustomLoggerFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomLoggerFilter.class);


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("Custom logger " + LOGGER.getClass() + " has been instantiated");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        LOGGER.info("Custom logger " + LOGGER.getClass() + " has been processed");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        LOGGER.info("Custom logger " + LOGGER.getClass() + " has been destroyed");
    }
}
