package com.gndbiz.gateway.filters;

import com.gndbiz.gateway.beans.FilterArgsBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalFilter extends AbstractGatewayFilterFactory<FilterArgsBean> {

	public GlobalFilter() {
		super(FilterArgsBean.class);
	}

	@Override
	public GatewayFilter apply(FilterArgsBean config) {
		return ((exchange, chain) -> {
			log.info("GlobalFilter baseMessage >>>>>>> {}", config.getBaseMessage());
			if (config.isPreLogger()) {
				log.info("GlobalFilter Start >>>>>>>" + exchange.getRequest());
			}
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				if (config.isPostLogger()) {
					log.info("GlobalFilter End >>>>>>>" + exchange.getResponse());
					ServerHttpResponse response = exchange.getResponse();
					log.info("status code: {}", response.getStatusCode());

				}
			}));
		});
	}
}
