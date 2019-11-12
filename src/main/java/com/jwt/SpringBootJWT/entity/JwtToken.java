package com.jwt.SpringBootJWT.entity;

import java.io.Serializable;

public class JwtToken implements Serializable {
    private String jwttoken;

    public JwtToken(String jwttoken) {
        this.jwttoken = jwttoken;
    }

    public String getJwttoken() {
        return jwttoken;
    }
}
