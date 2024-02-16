package com.yuk.wazzangstudyrestapi1.security;

import com.yuk.wazzangstudyrestapi1.services.JwtComponent;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityJwtFilter extends OncePerRequestFilter {

    private final JwtComponent jwtComponent;

    private final SecurityUserDetailService securityUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = jwtComponent.resolveToken((HttpServletRequest) request);

        if(tokenHeader != null) {
            String jwtToken;
            String username = null;

            jwtToken = tokenHeader.substring(7);

            if(jwtComponent.validateToken(jwtToken)) {
                username = jwtComponent.getClaimUserid();
                System.out.println(username);
            }

            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
                SecurityUserDetail userDetails = (SecurityUserDetail)this.securityUserDetailsService.loadUserByUsername(username);
                System.out.println("jwtToken "+jwtToken);
                System.out.println("userDetails.getBtoken() "+userDetails.getBtoken());
                if(userDetails.getBtoken().equals(jwtToken)) {
                    System.out.println("userDetails.getBtoken().equals(jwtToken)");
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
        System.out.println("SecurityJwtFilter - doFilterInternal method : before filterChain.doFilter(request,response)");
        filterChain.doFilter(request,response);
    }
}
