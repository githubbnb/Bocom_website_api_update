package com.bocom.bocomManager.repositories;

import com.bocom.bocomManager.enums.UserRole;
import com.bocom.bocomManager.models.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByTelephone(String telephone);
    List<AppUser> findAllByRole(UserRole role);
    List<AppUser> findAllByRoleAndBlock(UserRole role, boolean block);
    List<AppUser> findAllByBlock(boolean block);
    Page<AppUser> findAllByBlock(boolean block, Pageable pageable);
}
