package spring.security.jwt.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtTokenVerifierFilter extends OncePerRequestFilter {

    private static final String SECRET = "secure_key_secure_key_secure_key_secure_key_secure_key_secure_key_secure_key_secure_key_secure_key_secure_key_secure_key_";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("authorization");

        if(Strings.isNullOrEmpty(token) || token.startsWith("Bearer_")){
            filterChain.doFilter(request, response);
            return;
        }

        try{
            token.replace("Bearer_","");

            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                    .build()
                    .parseClaimsJws(token);

            Claims body = claims.getBody();
            String userName = body.getSubject();
            var authorities = (List<Map<String, String>>) body.get("authorities");

            Set<SimpleGrantedAuthority> authSet = authorities.stream().map(each -> new SimpleGrantedAuthority(each.get("authority")))
                    .collect(Collectors.toSet());

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    userName,
                    null,
                    authSet
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
        }catch (JwtException e){
            throw new IllegalStateException("Token"+token+" can not be trusted");
        }

        filterChain.doFilter(request, response);
    }
}
