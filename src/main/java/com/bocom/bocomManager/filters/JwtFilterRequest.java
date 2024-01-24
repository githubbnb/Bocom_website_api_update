package com.bocom.bocomManager.filters;

import com.bocom.bocomManager.services.AppUserService;
import com.bocom.bocomManager.services.JwtService;
import com.google.common.base.Strings;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Getter
public class JwtFilterRequest extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtFilterRequest(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String email = null;
        String user = null;
        String token = null;

        if (!Strings.isNullOrEmpty(authorizationHeader) && authorizationHeader.startsWith("Bearer ")){
            token = authorizationHeader.replace("Bearer ", "");
            user = jwtService.extractUserName(token);
            email = jwtService.extractUserEmail(token);
        }

        if (!Strings.isNullOrEmpty(email)){
            ArrayList<LinkedHashMap<String, String>> authorities = (ArrayList) jwtService.extractAllClaims(token).get("authorities");
            List<String> authoritiesValue = authorities.stream().map(linkedHashMap -> linkedHashMap.values()).map(collection -> collection.stream().collect(Collectors.joining())).collect(Collectors.toList());

            if (authoritiesValue.contains("ROLE_ADMIN") || authoritiesValue.contains("ROLE_COM_MANAGER")) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        user + ";" + email,
                        null,
                        new HashSet<>(authoritiesValue.stream().map(s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList()))
                );

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request,response);
    }

}
