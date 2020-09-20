package spring.security.jwt.security;

import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;

import static spring.security.jwt.security.UsePermissions.*;

public enum UserRoles {
    ADMIN(Sets.newHashSet(STUDENT_READ, STUDENT_WRITE, COURSE_READ, COURSE_WRITE)),
    STUDENT(Sets.newHashSet(STUDENT_READ, STUDENT_WRITE, COURSE_READ)),
    TRAINEE(Sets.newHashSet(STUDENT_READ, COURSE_READ));

    private Set<UsePermissions> permissions = new HashSet<>();

    UserRoles(Set<UsePermissions> newHashSet) {
        this.permissions = newHashSet;
    }

    public Set<UsePermissions> getPermissions() {
        return permissions;
    }
}
