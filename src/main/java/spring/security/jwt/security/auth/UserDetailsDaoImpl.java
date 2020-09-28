package spring.security.jwt.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static spring.security.jwt.security.UserRoles.ADMIN;

@Repository("MockUserRepo")
public class UserDetailsDaoImpl implements UserDetailsDao {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Optional<ApplicationUser> getUserByUserName(String userName) {
        return getUsers()
                .stream()
                .filter(user->user.getUsername().equals(userName.trim()))
                .findFirst();
    }

    private List<ApplicationUser> getUsers(){
        List<ApplicationUser> applicationUsers = List.of(
                new ApplicationUser("tamal",
                        passwordEncoder.encode("password"),
                        ADMIN.getAuthorities(),
                        true,
                        true,
                        true,
                        true),
                new ApplicationUser("susanta",
                        passwordEncoder.encode("password"),
                        ADMIN.getAuthorities(),
                        true,
                        true,
                        true,
                        true),
                new ApplicationUser("sagnik",
                        passwordEncoder.encode("password"),
                        ADMIN.getAuthorities(),
                        true,
                        true,
                        true,
                        true)
        );
        return applicationUsers;
    }
}
