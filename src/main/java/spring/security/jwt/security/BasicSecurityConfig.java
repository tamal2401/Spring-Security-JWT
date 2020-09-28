package spring.security.jwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.security.jwt.security.auth.ApplicationUserDetailsService;
import spring.security.jwt.security.auth.UserDetailsDaoImpl;

import java.util.concurrent.TimeUnit;

import static spring.security.jwt.security.UsePermissions.*;

@Configuration
@EnableWebSecurity
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
