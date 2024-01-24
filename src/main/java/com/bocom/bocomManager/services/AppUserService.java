package com.bocom.bocomManager.services;

import com.bocom.bocomManager.dto.appUser.AppUserRequest;
import com.bocom.bocomManager.dto.appUser.AppUserResponse;
import com.bocom.bocomManager.enums.UserRole;
import com.bocom.bocomManager.models.AppUser;
import com.bocom.bocomManager.repositories.AppUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AppUserService(AppUserRepository appUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appUserRepository = appUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Page<AppUser> getAllAppUsers(boolean block, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        return appUserRepository.findAllByBlock(block,paging);
    }

    public List<AppUser> getAllAppUsers(boolean block) {
        return appUserRepository.findAllByBlock(block);
    }

    public List<AppUser> getAllAppUsersByRole(UserRole role) {
        return appUserRepository.findAllByRole(role);
    }

    public List<AppUser> getAllAppUsersByRole(String role) {
        return appUserRepository.findAllByRole(UserRole.valueOf(role));
    }

    public List<AppUser> getAllAppUsersByRole(UserRole role, boolean block) {
        return appUserRepository.findAllByRoleAndBlock(role, block);
    }

    public List<AppUser> getAllAppUsersByRole(String role, boolean block) {
        return appUserRepository.findAllByRoleAndBlock(UserRole.valueOf(role), block);
    }

    public AppUser getOneAppUser(Long idUser) {
        return appUserRepository.findById(idUser).orElse(null);
    }

    public AppUser getOneAppUserByEmail(String email) {
        return appUserRepository.findByEmail(email).orElse(null);
    }

    public AppUser getOneAppUserByTelephone(String telephone) {
        return appUserRepository.findByTelephone(telephone).orElse(null);
    }

    public AppUserResponse createAppUser(AppUserRequest appUserRequest) {
        AppUser oneAppUserByEmail = getOneAppUserByEmail(appUserRequest.getEmail());

        if (oneAppUserByEmail != null) {
            throw new RuntimeException("Un utilisateur existe déjà avec l'adresse email " + appUserRequest.getEmail());
        }

        AppUser oneAppUserByTelephone = getOneAppUserByTelephone(appUserRequest.getTelephone());

        if (oneAppUserByTelephone != null) {
            throw new RuntimeException("Un utilisateur existe déjà avec le numero de telephone " + appUserRequest.getTelephone());
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String str = String.valueOf(authentication.getPrincipal());

        AppUser save = appUserRepository.save(
                AppUser

                        .builder()
                        .avatar(appUserRequest.getAvatar())
                        .block(false)
                        .email(appUserRequest.getEmail())
                        .nom(appUserRequest.getNom())
                        .prenom(appUserRequest.getPrenom())
                        .telephone(appUserRequest.getTelephone())
                        .role(appUserRequest.getRole())
                        .password(appUserRequest.getPassword())
                        .createdBy(str.split(";")[0])
                        .updatedBy(str.split(";")[0])
                        .build()
        );
        return AppUserResponse
                .builder()
                .avatar(save.getAvatar())
                .block(save.isBlock())
                .email(save.getEmail())
                .nom(save.getNom())
                .prenom(save.getPrenom())
                .telephone(save.getTelephone())
                .role(save.getRole())
                .id(save.getId())
                .build();
    }

    public AppUserResponse register(AppUserRequest appUserRequest) {
        AppUser oneAppUserByEmail = getOneAppUserByEmail(appUserRequest.getEmail());

        if (oneAppUserByEmail != null) {
            throw new RuntimeException("Un utilisateur existe déjà avec l'adresse email " + appUserRequest.getEmail());
        }

        AppUser oneAppUserByTelephone = getOneAppUserByTelephone(appUserRequest.getTelephone());

        if (oneAppUserByTelephone != null) {
            throw new RuntimeException("Un utilisateur existe déjà avec le numero de telephone " + appUserRequest.getTelephone());
        }

        String encodePassword = bCryptPasswordEncoder.encode(appUserRequest.getPassword());

        AppUser save = appUserRepository.save(
                AppUser
                        .builder()
                        .avatar(appUserRequest.getAvatar())
                        .block(false)
                        .email(appUserRequest.getEmail())
                        .nom(appUserRequest.getNom())
                        .prenom(appUserRequest.getPrenom())
                        .telephone(appUserRequest.getTelephone())
                        .role(appUserRequest.getRole())
                        .password(encodePassword)
                        .createdBy(appUserRequest.getNom())
                        .updatedBy(appUserRequest.getNom())
                        .build()
        );
        return AppUserResponse
                .builder()
                .avatar(save.getAvatar())
                .block(save.isBlock())
                .email(save.getEmail())
                .nom(save.getNom())
                .prenom(save.getPrenom())
                .telephone(save.getTelephone())
                .role(save.getRole())
                .id(save.getId())
                .build();
    }

    public AppUserResponse updateAppUser(Long idAppUser, AppUserRequest appUserRequest) {
        AppUser oneAppUser = getOneAppUser(idAppUser);

        if (oneAppUser == null) {
            throw new RuntimeException("Aucun n'utilisateur n'existe avec l'identifiant " + idAppUser);
        }

        AppUser oneAppUserByEmail = getOneAppUserByEmail(appUserRequest.getEmail());

        if (oneAppUserByEmail != null && !oneAppUserByEmail.getId().equals(idAppUser)) {
            throw new RuntimeException("Un autre utilisateur existe déjà avec l'adresse email " + appUserRequest.getEmail());
        }

        AppUser oneAppUserByTelephone = getOneAppUserByTelephone(appUserRequest.getTelephone());

        if (oneAppUserByTelephone != null && !oneAppUserByTelephone.getId().equals(idAppUser)) {
            throw new RuntimeException("Un autre utilisateur existe déjà avec le numero de telephone " + appUserRequest.getTelephone());
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String str = String.valueOf(authentication.getPrincipal());

        AppUser save = appUserRepository.save(
                AppUser

                        .builder()
                        .avatar(appUserRequest.getAvatar())
                        .block(oneAppUser.isBlock())
                        .email(appUserRequest.getEmail())
                        .nom(appUserRequest.getNom())
                        .prenom(appUserRequest.getPrenom())
                        .telephone(appUserRequest.getTelephone())
                        .role(appUserRequest.getRole())
                        .password(oneAppUser.getPassword())
                        .createdBy(oneAppUser.getCreatedBy())
                        .updatedBy(str.split(";")[0])
                        .build()
        );

        return AppUserResponse
                .builder()
                .avatar(save.getAvatar())
                .block(save.isBlock())
                .email(save.getEmail())
                .nom(save.getNom())
                .prenom(save.getPrenom())
                .telephone(save.getTelephone())
                .role(save.getRole())
                .id(save.getId())
                .build();
    }

    public boolean deleteAppUser(Long idAppUser) {
        AppUser oneAppUser = getOneAppUser(idAppUser);

        if (oneAppUser == null) {
            throw new RuntimeException("Aucun n'utilisateur n'existe avec l'identifiant " + idAppUser);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String str = String.valueOf(authentication.getPrincipal());

        AppUser save = appUserRepository.save(
                AppUser

                        .builder()
                        .avatar(oneAppUser.getAvatar())
                        .block(true)
                        .email(oneAppUser.getEmail())
                        .nom(oneAppUser.getNom())
                        .prenom(oneAppUser.getPrenom())
                        .telephone(oneAppUser.getTelephone())
                        .role(oneAppUser.getRole())
                        .password(oneAppUser.getPassword())
                        .createdBy(oneAppUser.getCreatedBy())
                        .updatedBy(str.split(";")[0])
                        .build()
        );

        return save.getId() > 0;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("Un utilisateur avec l'adresse email " + username + " n'existe pas"));
    }
}
