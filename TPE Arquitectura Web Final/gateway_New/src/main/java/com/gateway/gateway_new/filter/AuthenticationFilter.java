package com.gateway.gateway_new.filter;

import com.gateway.gateway_new.util.TokenProviderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    @Autowired
    private RouterValidator validator;
    @Autowired
    private TokenProviderUtil tokenProviderUtil;


    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())){

                String authHeaders = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if(authHeaders != null && authHeaders.startsWith("Bearer")){
                    authHeaders = authHeaders.substring(7);
                }
                try {
                    tokenProviderUtil.validateToken(authHeaders);
                }catch (Exception e){
                    throw new RuntimeException("un authorized acces to application");
                }
            }
            return chain.filter(exchange);
        }));
    }

    public static class Config {
    }
}
