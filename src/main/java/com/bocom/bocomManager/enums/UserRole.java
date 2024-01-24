package com.bocom.bocomManager.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.bocom.bocomManager.enums.Permission.*;

@RequiredArgsConstructor
public enum UserRole {
    ADMIN(
            Set.of(
                    PROMOTION_READ,
                    PROMOTION_UPDATE,
                    PROMOTION_DELETE,
                    PROMOTION_CREATE,

                    ACTUALITY_READ,
                    ACTUALITY_UPDATE,
                    ACTUALITY_DELETE,
                    ACTUALITY_CREATE,

                    USER_READ,
                    USER_UPDATE,
                    USER_DELETE,
                    USER_CREATE,

                    FILE_ADD,
                    FILE_DELETE
            )
    ),
    COM_MANAGER(
            Set.of(
                    PROMOTION_READ,
                    PROMOTION_UPDATE,
                    PROMOTION_DELETE,
                    PROMOTION_CREATE,

                    ACTUALITY_READ,
                    ACTUALITY_UPDATE,
                    ACTUALITY_DELETE,
                    ACTUALITY_CREATE,

                    FILE_ADD,
                    FILE_DELETE
            )
    );

    @Getter
    private final Set<Permission> permissions;

    public java.util.Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = getPermissions()
                .stream()
//                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .map(permission -> new SimpleGrantedAuthority("ROLE_" + permission.name()))
                .collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

}
