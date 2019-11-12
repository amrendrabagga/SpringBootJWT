package com.jwt.SpringBootJWT.controller;

import com.jwt.SpringBootJWT.entity.JwtRequest;
import com.jwt.SpringBootJWT.entity.JwtToken;
import com.jwt.SpringBootJWT.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class WelcomeController {
    @Autowired
    JwtUtil jwtUtil;

    @RequestMapping(value = "/hello",method = RequestMethod.POST)
    public String helloMessage(HttpServletRequest request){
        System.out.println("entering controller");

        return "hello world";
    }
    @RequestMapping(value="/authenticate",method = RequestMethod.POST,consumes = "application/json")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest jwtRequest){
        //for sending whole response body with headers, body, status we use ResponseEntity

        if(jwtRequest.getUsername().equalsIgnoreCase("amrendra")&&jwtRequest.getPassword().equalsIgnoreCase("123")) {
            String token = jwtUtil.generateToken(jwtRequest.getUsername());
            return ResponseEntity.ok(new JwtToken(token));
        }

        return ResponseEntity.ok("no token");
    }
}
