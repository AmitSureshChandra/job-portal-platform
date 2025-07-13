package com.jobportal.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
        System.out.println("Gateway Request: " + request.getMethod() + " " + request.getURI());
        System.out.println("Headers: " + request.getHeaders());
        
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            System.out.println("Gateway Response Status: " + exchange.getResponse().getStatusCode());
        }));
    }
    
    @Override
    public int getOrder() {
        return -1; // Execute before other filters
    }
}