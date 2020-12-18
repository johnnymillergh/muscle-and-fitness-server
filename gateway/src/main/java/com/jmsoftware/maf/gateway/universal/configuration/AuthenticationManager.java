package com.jmsoftware.maf.gateway.universal.configuration;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Description: AuthenticationManager, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/18/2020 3:40 PM
 **/
@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {
    private final JwtService jwtService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        String username;
        try {
            username = jwtService.getUsernameFromJwt(authToken);
        } catch (Exception e) {
            username = null;
        }
//        if (username != null && !tokenProvider.isTokenExpired(authToken)) {
//            Claims claims = tokenProvider.getAllClaimsFromToken(authToken);
//            List roles = claims.get(AUTHORITIES_KEY, List.class);
//            List authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(
//                    Collectors.toList());
//            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, username,
//                                                                                               authorities);
//            SecurityContextHolder.getContext().setAuthentication(new UserPrincipal(username, authorities));
//            return Mono.just(auth);
//        } else {
//            return Mono.empty();
//        }
        List<GrantedAuthority> authorities = Lists.newLinkedList();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, username,
                                                                                           authorities);
        return Mono.just(auth);
    }
}
