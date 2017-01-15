package com.jvanhouteghem.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

public class JwtFilter extends GenericFilterBean {

    @Override
    public void doFilter(final ServletRequest req,
                         final ServletResponse res,
                         final FilterChain chain) throws IOException, ServletException {
        
    	final HttpServletRequest request = (HttpServletRequest) req;

    	// Get complete token
        final String authHeader = request.getHeader("Authorization");
        //System.out.println(authHeader); // Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0b20iLCJyb2xlcyI6WyJ1c2VyIl0sImlhdCI6MTQ4NDQ4MDYxNH0.b4Vpr5LNqdlLrKecfG2gy0lIPn-4B42z8VNcULhNybg
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ServletException("Missing or invalid Authorization header.");
        }
        
        final String token = authHeader.substring(7); // The part after "Bearer "

        try {
            final Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
            request.setAttribute("claims", claims);
        }
        catch (final SignatureException e) {
            throw new ServletException("Invalid token.");
        }

        chain.doFilter(req, res);
        
    }

}
