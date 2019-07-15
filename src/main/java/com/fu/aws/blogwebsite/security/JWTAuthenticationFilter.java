package com.fu.aws.blogwebsite.security;

import com.auth0.jwt.JWT;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fu.aws.blogwebsite.entity.Role;
import com.fu.aws.blogwebsite.entity.User;
import com.fu.aws.blogwebsite.service.MyUserDetailsService;
import com.fu.aws.blogwebsite.service.AdminService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.fu.aws.blogwebsite.constants.SecurityConstant.EXPIRATION_TIME;
import static com.fu.aws.blogwebsite.constants.SecurityConstant.HEADER_STRING;
import static com.fu.aws.blogwebsite.constants.SecurityConstant.TOKEN_PREFIX;
import static com.fu.aws.blogwebsite.constants.SecurityConstant.SECRET;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private AdminService adminService;

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {

            User creds = new ObjectMapper()
                    .readValue(req.getInputStream(), User.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        List<String> roles = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        Algorithm alg = Algorithm.HMAC256(SECRET);
        String[] tmp = new String[roles.size()];
        roles.toArray(tmp);
        String token = JWT.create()
                .withClaim("username", ((UserDetails) auth.getPrincipal()).getUsername())
                .withArrayClaim("roles", tmp)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).sign(alg);
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        res.addHeader("Access-Control-Expose-Headers", "Access-Token, Uid");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", TOKEN_PREFIX + token);
        res.getWriter().write(jsonObject.toString());
    }
}