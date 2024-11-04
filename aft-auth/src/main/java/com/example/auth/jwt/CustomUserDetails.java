package com.example.auth.jwt;

import com.example.domain.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CustomUserDetails implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long idx;

    private final String name;

    private final String userId;

    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Long idx, String name, String userId, String password,
                             Collection<? extends GrantedAuthority> authorities) {
        this.idx = idx;
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.authorities = authorities;
    }

    public static CustomUserDetails build(User user) {
        List<GrantedAuthority> authorities = user.getUserRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleType().name()))
                .collect(Collectors.toList());

        return new CustomUserDetails(
                user.getIdx(),
                user.getName(),
                user.getUserId(),
                user.getPassword(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

}
