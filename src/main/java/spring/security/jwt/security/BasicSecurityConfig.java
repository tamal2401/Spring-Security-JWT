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
import spring.security.jwt.security.auth.ApplicationUser;

import java.util.concurrent.TimeUnit;

import static spring.security.jwt.security.UsePermissions.*;
import static spring.security.jwt.security.UserRoles.*;

@Configuration
@EnableWebSecurity
public class BasicSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * Order of the antmatchers matters a lot.
     * the child routes needs to be checked first and then the parent routes
     */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/api/index", "css/*", "js/*").permitAll()
                .antMatchers("/api/student/**").hasAnyAuthority(STUDENT_READ.getPermission(), STUDENT_WRITE.getPermission(), COURSE_READ.getPermission())
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/dashboard", true)
                .passwordParameter("password")
                .usernameParameter("username")  // pwd & username parameter is used to identify the json or field key for the following fields
                .and()
                .rememberMe()
                .rememberMeParameter("remember-me")
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(2))
                .key("key_to_implement_MD5_hash_algorithm")
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .logoutSuccessUrl("/login").permitAll();
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails tamalUser = new ApplicationUser("tamal",
                "password",
                ADMIN.getAuthorities(),
                false,
                false,
                true,
                true);

//        UserDetails tamalUser = User.builder()
//                .username("tamal")
//                .password(passwordEncoder.encode("password"))
////                .roles(ADMIN.name())
//                .authorities(ADMIN.getAuthorities())
//                .build();

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
