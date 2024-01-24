package com.bocom.bocomManager.dto.appUser;

import com.bocom.bocomManager.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppUserRequest {
    private String nom;

    private String prenom;

    private String password;

    private String email;

    private String telephone;

    private String avatar;

    private UserRole role;
}
