package spring.security.jwt.security.auth;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserDetailsDao {

    Optional<UserDetails> getUserByUserName(String userName);
}
