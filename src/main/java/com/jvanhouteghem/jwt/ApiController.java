package com.jvanhouteghem.jwt;

import io.jsonwebtoken.Claims;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "role/{role}", method = RequestMethod.GET)
	public Boolean login(@PathVariable final String role,
			final HttpServletRequest request) throws ServletException {
		
		//System.out.println(request.getAttribute("claims")); 
		// {sub=tom, roles=[user], iat=1484480157} ? (iat = Issued at) un timestamp de la date de la requête
		// Trois ?
		
		final Claims claims = (Claims) request.getAttribute("claims");
		return ((List<String>) claims.get("roles")).contains(role);
	}
}
