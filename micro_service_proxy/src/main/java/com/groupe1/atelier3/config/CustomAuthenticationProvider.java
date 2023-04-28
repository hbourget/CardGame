package com.groupe1.atelier3.config;

import com.groupe1.atelier3.users.models.UserDTO;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        try {
            UserDTO userDTO = restTemplate.getForObject("http://localhost:8888/auth/login?username={username}&password={password}",
                    UserDTO.class, username, password);

            if (userDTO != null) {
                return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
            } else {
                throw new BadCredentialsException("Invalid username or password");
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 401) {
                throw new BadCredentialsException("Invalid username or password");
            } else {
                throw new AuthenticationServiceException("An error occurred while authenticating", e);
            }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
