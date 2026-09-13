package com.example.accountservice.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

@Component
public class BearerTokenInterceptor
        implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution)
            throws IOException {

        ServletRequestAttributes attributes =
                (ServletRequestAttributes)
                        RequestContextHolder.getRequestAttributes();

        if (attributes != null) {

            HttpServletRequest incomingRequest =
                    attributes.getRequest();

            String authorizationHeader =
                    incomingRequest.getHeader(
                            HttpHeaders.AUTHORIZATION
                    );

            if (authorizationHeader != null &&
                    authorizationHeader.startsWith("Bearer ")) {

                request.getHeaders().set(
                        HttpHeaders.AUTHORIZATION,
                        authorizationHeader
                );
            }
        }

        return execution.execute(request, body);
    }
}
