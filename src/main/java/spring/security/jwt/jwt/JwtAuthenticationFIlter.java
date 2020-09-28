package spring.security.jwt.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFIlter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager manager;

    public JwtAuthenticationFIlter(AuthenticationManager manager) {
        this.manager = manager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try{
            UserCredentials creds = new ObjectMapper().readValue(request.getInputStream(), UserCredentials.class);
            Authentication auth = new UsernamePasswordAuthenticationToken(creds.getUserName(), creds.getPassword());
            return manager.authenticate(auth);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
