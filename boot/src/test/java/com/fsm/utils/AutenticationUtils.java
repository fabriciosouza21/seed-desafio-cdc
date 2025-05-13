package com.fsm.utils;

import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Singleton;

@Singleton
public class AutenticationUtils {

    private String token;

    private final RequestSpecification spec;


    public AutenticationUtils(RequestSpecification spec) {
        this.spec = spec;
    }

    public String getToken(){
        if (token != null) {
            return token;
        }
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("admin", "admin");

        this.token = spec
                .contentType("application/json")
                .body(creds)
                .when()
                .post("/login").jsonPath().getString("access_token");
        return this.token;
    }
}
