
package com.innocito.axcl.config.filters;

import com.innocito.axcl.config.JwtUtil;
import com.innocito.axcl.entity.User;
import com.innocito.axcl.enums.UserRole;
import com.innocito.axcl.enums.UserStatus;
import com.innocito.axcl.model.TenantContext;
import com.innocito.axcl.model.TenantData;
import com.innocito.axcl.repository.UserRepository;
import com.innocito.axcl.util.BasicUtils;
import com.innocito.axcl.util.MessageConstants;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.innocito.axcl.util.ApplicationConstants.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final BasicUtils basicUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            TenantData tenantData = new TenantData();
            final String authorizationHeader = request.getHeader(AUTHORIZATION);
            String userName = null;
            String jwt = null;
            if (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {
                jwt = authorizationHeader.substring(7);
                userName = jwtUtil.extractUsername(jwt);
            }

            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);

                User user = userRepository.findByEmail(userName);
                if (UserStatus.ACTIVE.getValue() != user.getUserStatus()) {
                    log.info(basicUtils.getLocalizedMessage(MessageConstants.AUTH_USER_NOT_ACTIVE, null));
                    throw new JwtException(basicUtils.getLocalizedMessage(MessageConstants.USER_INACTIVE, null));
                }

                if (jwtUtil.validateToken(jwt, userDetails)) {
                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    tenantData.setLoggedInUserId(jwtUtil.extractAllClaims(jwt).get(LOGGED_IN_USER_ID, String.class));
                    tenantData.setLoggedInUserType(jwtUtil.extractAllClaims(jwt).get(LOGGED_IN_USER_TYPE, Integer.class));
                    tenantData.setPermissions(List.of(UserRole.getByValue(user.getUserRole()).name()));
                    for (String permissionTemp : tenantData.getPermissions()) {
                        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permissionTemp);
                        authorities.add(authority);
                    }
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken
                            (userDetails, null, authorities);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            try {
                // Code that processes the request, sets ThreadLocal data, etc.
                TenantContext.setTenantData(tenantData);
                filterChain.doFilter(request, response); // Request processing
            } finally {
                // Ensure that the ThreadLocal data is cleared after the request is completed
                TenantContext.clear(); // This prevents memory leaks by removing the reference to tenantData
            }
        } catch (JwtException ex) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(UTF_8);
            response.getWriter().write("{\"error\": \"Invalid or expired token\"}");
        }
    }
}
