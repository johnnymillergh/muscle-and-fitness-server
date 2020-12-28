package com.jmsoftware.maf.apigateway.security;

import cn.hutool.core.util.StrUtil;
import com.jmsoftware.maf.apigateway.security.configuration.JwtConfiguration;
import com.jmsoftware.maf.common.exception.SecurityException;
import com.jmsoftware.maf.reactivespringbootstarter.configuration.MafConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Description: ReactiveServerSecurityContextRepository, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/18/2020 3:38 PM
 **/
@Slf4j
@RequiredArgsConstructor
public class JwtReactiveServerSecurityContextRepository implements ServerSecurityContextRepository {
    private final MafConfiguration mafConfiguration;
    private final ReactiveAuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        log.error("Unsupported operation exception: Not supported yet.");
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        val request = exchange.getRequest();
        // Ignore allowed URL
        for (var ignoredUrl : mafConfiguration.flattenIgnoredUrls()) {
            if (antPathMatcher.match(ignoredUrl, request.getURI().getPath())) {
                return Mono.empty();
            }
        }
        val authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isBlank(authorization) || !authorization.startsWith(JwtConfiguration.TOKEN_PREFIX)) {
            log.warn("Pre-authentication failure! Cause: `{}` in HTTP headers not found. Request URL: [{}] {}",
                     HttpHeaders.AUTHORIZATION, request.getMethod(), request.getURI());
            return Mono.error(new SecurityException(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED, "JWT Required"));
        }
        val jwt = authorization.replace(JwtConfiguration.TOKEN_PREFIX, "");
        String username;
        try {
            username = jwtService.getUsernameFromJwt(jwt);
        } catch (Exception e) {
            log.warn("Pre-authentication failure! Cause: Exception occurred when parsing JWT. {}. Request URL: [{}] {}",
                     e.getMessage(), request.getMethod(), request.getURI());
            return Mono.error(new SecurityException(HttpStatus.FORBIDDEN, e.getMessage()));
        }
        val userPrincipal = UserPrincipal.createByUsername(username);
        log.info("User principal is created. {}", userPrincipal);
        val authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null);
        log.info("Authentication is created. {}", authentication);
        log.warn("About to authenticate…");
        return this.authenticationManager.authenticate(authentication).map(SecurityContextImpl::new);
    }
}
