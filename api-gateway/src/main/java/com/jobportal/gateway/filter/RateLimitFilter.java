package com.jobportal.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitFilter extends AbstractGatewayFilterFactory<RateLimitFilter.Config> {
    
    private final ConcurrentHashMap<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> requestTimes = new ConcurrentHashMap<>();
    
    public RateLimitFilter() {
        super(Config.class);
    }
    
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String clientId = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            long currentTime = System.currentTimeMillis();
            
            // Reset counter every minute
            Long lastRequestTime = requestTimes.get(clientId);
            if (lastRequestTime == null || (currentTime - lastRequestTime) > 60000) {
                requestCounts.put(clientId, new AtomicInteger(0));
                requestTimes.put(clientId, currentTime);
            }
            
            AtomicInteger count = requestCounts.get(clientId);
            if (count.incrementAndGet() > config.getMaxRequests()) {
                exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                return exchange.getResponse().setComplete();
            }
            
            return chain.filter(exchange);
        };
    }
    
    public static class Config {
        private int maxRequests = 100; // Default 100 requests per minute
        
        public int getMaxRequests() { return maxRequests; }
        public void setMaxRequests(int maxRequests) { this.maxRequests = maxRequests; }
    }
}