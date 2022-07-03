package com.example.demo.routes;


import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Routes {
    public final String USER_GET_ROUTE = "/api/v2/router/user";
}
