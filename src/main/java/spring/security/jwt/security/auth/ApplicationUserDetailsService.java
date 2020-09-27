package spring.security.jwt.security.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ApplicationUserDetailsService implements UserDetailsService {

    private UserDetailsDao userDetailsDao;

    public ApplicationUserDetailsService(UserDetailsDao userDetailsDao) {
        this.userDetailsDao = userDetailsDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDetailsDao.getUserByUserName(username)
                .orElseThrow(()-> new UsernameNotFoundException(String.format("User %s does not exist", username)));
    }
}