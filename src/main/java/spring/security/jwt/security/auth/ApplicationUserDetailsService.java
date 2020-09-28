package spring.security.jwt.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ApplicationUserDetailsService implements UserDetailsService {

    @Autowired
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
