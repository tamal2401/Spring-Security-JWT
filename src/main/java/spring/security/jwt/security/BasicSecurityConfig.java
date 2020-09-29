package spring.security.jwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.security.jwt.jwt.JwtAuthenticationFIlter;
import spring.security.jwt.jwt.JwtTokenVerifierFilter;
import spring.security.jwt.security.auth.ApplicationUserDetailsService;
import spring.security.jwt.security.auth.UserDetailsDaoImpl;

import static spring.security.jwt.security.UsePermissions.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class BasicSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("MockUserRepo")
    UserDetailsDaoImpl userDetailsDao;

    @Autowired
    ApplicationUserDetailsService applicationUserDetailsService;

    /**
     * Order of the antmatchers matters a lot.
     * the child routes needs to be checked first and then the parent routes
     */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtAuthenticationFIlter(authenticationManager()))
                .addFilterAfter(new JwtTokenVerifierFilter(), JwtAuthenticationFIlter.class)
                .authorizeRequests()
                .antMatchers("/", "/api/index", "css/*", "js/*").permitAll()
                .antMatchers("/api/student/**").hasAnyAuthority(STUDENT_READ.getPermission(), STUDENT_WRITE.getPermission(), COURSE_READ.getPermission())
                .anyRequest()
                .authenticated();

    }

//    @Override
//    protected UserDetailsService userDetailsService() {
//        return new ApplicationUserDetailsService(userDetailsDao);
//    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(getAuthnticationProvider());
    }

    private DaoAuthenticationProvider getAuthnticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserDetailsService);
        return provider;
    }

    @Bean
    PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(10);
    }
}
