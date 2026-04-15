package cn.edu.swu.campus_buddy_matching_system.security;

import cn.edu.swu.campus_buddy_matching_system.model.entity.Permission;
import cn.edu.swu.campus_buddy_matching_system.model.entity.Role;
import cn.edu.swu.campus_buddy_matching_system.model.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final Boolean enabled;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.enabled = user.getEnabled();
        this.authorities = buildAuthorities(user);
    }

    private Collection<? extends GrantedAuthority> buildAuthorities(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                if (role.getName() != null && !role.getName().isBlank()) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
                }

                if (role.getPermissions() != null) {
                    for (Permission permission : role.getPermissions()) {
                        if (permission.getName() != null && !permission.getName().isBlank()) {
                            authorities.add(new SimpleGrantedAuthority(permission.getName()));
                        }
                    }
                }
            }
        }

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(enabled);
    }
}