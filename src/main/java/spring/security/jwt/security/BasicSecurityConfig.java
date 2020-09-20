package spring.security.jwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import static spring.security.jwt.security.UsePermissions.*;
import static spring.security.jwt.security.UserRoles.*;

@Configuration
@EnableWebSecurity
public class BasicSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     *  Order of the antmatchers matters a lot.
     *  the child routes needs to be checked first and then the parent routes
     */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .authorizeRequests()
                .antMatchers("/", "/api/v1/index", "css/*", "js/*").permitAll()
                .antMatchers("/api/student/**").hasRole(STUDENT.name())
                .antMatchers(HttpMethod.DELETE, "/api/management/**").hasAuthority(COURSE_WRITE.getPermission())
                .antMatchers(HttpMethod.POST, "/api/management/**").hasAuthority(COURSE_WRITE.getPermission())
                .antMatchers(HttpMethod.PUT, "/api/management/**").hasAuthority(COURSE_WRITE.getPermission())
                .antMatchers("/api/management/**").hasAnyRole(ADMIN.name(), TRAINEE.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails tamalUser = User.builder()
                .username("tamal")
                .password(passwordEncoder.encode("password"))
//                .roles(ADMIN.name())
                .authorities(ADMIN.getAuthorities())
                .build();

        UserDetails sasaDetais = User.builder()
                .username("susanta")
                .password(passwordEncoder.encode("password"))
//                .roles(STUDENT.name())
                .authorities(STUDENT.getAuthorities())
                .build();

        UserDetails sagiUser = User.builder()
                .username("sagnik")
                .password(passwordEncoder.encode("password"))
//                .roles(TRAINEE.name())
                .authorities(TRAINEE.getAuthorities())
                .build();

        return new InMemoryUserDetailsManager(tamalUser, sasaDetais, sagiUser);
    }

    @Bean
    PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(10);
    }
}
