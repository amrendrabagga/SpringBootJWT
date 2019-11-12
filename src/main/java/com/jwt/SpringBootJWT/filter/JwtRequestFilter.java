package com.jwt.SpringBootJWT.filter;

import com.jwt.SpringBootJWT.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    AuthenticationManager authManager;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        System.out.println("================FROM FILTER=============");

        final String requestTokenHeader = request.getHeader("Authorization");
        System.out.println(requestTokenHeader);
        String username = null;
        String jwtToken = null;
// JWT Token is in the form "Bearer token". Remove Bearer word and get
// only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            System.out.println(jwtToken);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                System.out.println("username from FILTER======="+username);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        } else {
//            logger.warn("JWT Token does not begin with Bearer String");
            System.out.println("JWT Token does not begin with Bearer String");
        }

// Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
            if (username.equalsIgnoreCase("amrendra")) {
// if token is valid configure Spring Security to manually set
// authentication
                if (jwtTokenUtil.validateToken(jwtToken, username)) {

                    System.out.println("validated token=================");
                    //authentication object is created via UsernamePasswordAuthenticationToken
                    //manually creating authentication object
                    UsernamePasswordAuthenticationToken authReq
                            = new UsernamePasswordAuthenticationToken(username, "123", Arrays.asList());//manually passing password
                    //but in real case we fetch passwrod using username from DAO
                    authReq.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
// After setting the Authentication in the context, we specify
// that the current user is authenticated. So it passes the
// Spring Security Configurations successfully.
                    SecurityContextHolder.clearContext();//clearing previously stored context
                    SecurityContextHolder.getContext().setAuthentication(authReq);

                }
            }
        }
        chain.doFilter(request, response);
    }

}
