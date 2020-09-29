package spring.security.jwt.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;

public class JwtAuthenticationFIlter extends UsernamePasswordAuthenticationFilter {

    private static final String SECRET = "secure_key_secure_key_secure_key_secure_key_secure_key_secure_key_secure_key_secure_key_secure_key_secure_key_secure_key_";

    private final AuthenticationManager manager;

    public JwtAuthenticationFIlter(AuthenticationManager manager) {
        this.manager = manager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            UserCredentials creds = new ObjectMapper().readValue(request.getInputStream(), UserCredentials.class);
            Authentication auth = new UsernamePasswordAuthenticationToken(creds.getUsername(), creds.getPassword());
            return manager.authenticate(auth);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        System.out.println("into");

        String token = Jwts
                .builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();

        response.addHeader("authorization", "Bearer "+token);

        super.successfulAuthentication(request, response, chain, authResult);
    }
}
