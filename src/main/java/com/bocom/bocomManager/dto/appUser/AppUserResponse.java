package com.bocom.bocomManager.dto.appUser;

import com.bocom.bocomManager.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppUserResponse {

    private Long id;

    private String nom;

    private String prenom;

    private String email;

    private String telephone;

    private String avatar;

    private UserRole role;

    private boolean block;

}
