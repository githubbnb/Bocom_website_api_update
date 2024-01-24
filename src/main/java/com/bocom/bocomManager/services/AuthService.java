package com.bocom.bocomManager.services;

import com.bocom.bocomManager.dto.AuthenticationResponse;
import com.bocom.bocomManager.dto.Credential;
import com.bocom.bocomManager.dto.appUser.AppUserRequest;
import com.bocom.bocomManager.dto.appUser.AppUserResponse;
import com.bocom.bocomManager.enums.UserRole;
import com.bocom.bocomManager.models.AppUser;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final AppUserService userService;

    private final JwtService jwtService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(AppUserService userService, JwtService jwtService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public AuthenticationResponse login(Credential credential) {
        AppUser appUser = userService.getOneAppUserByEmail(credential.getEmail());

        if (appUser != null ? verifyPassword(credential.getPassword(), appUser.getPassword()) : false) {
            AuthenticationResponse authenticationResponse = jwtService.generatedTokens(appUser);
            return authenticationResponse;
        }

        throw new BadCredentialsException("Aucun utilisateur trouvé avec ces identifiants");
    }

    public boolean verifyPassword(String keyCheck, String password) {
        return bCryptPasswordEncoder.matches(keyCheck, password) ? true : false;
    }

    public AppUserResponse generatedFirstAdmin() {
        AppUser appUser = userService.getOneAppUserByEmail("super-admin@gmail.com");
        if (appUser != null ? appUser.getId() > 0 : false) {
            throw new IllegalStateException("Email already taken");
        }

        return userService.register(
                AppUserRequest
                        .builder()
                        .email("super-admin@gmail.com")
                        .nom("Super Admin")
                        .prenom("")
                        .telephone("00000000")
                        .role(UserRole.ADMIN)
                        .password("Bocom@2023-Admin")
                        .build()
        );
    }

}
