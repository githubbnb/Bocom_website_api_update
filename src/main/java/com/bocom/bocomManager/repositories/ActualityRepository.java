package com.bocom.bocomManager.repositories;

import com.bocom.bocomManager.enums.UserRole;
import com.bocom.bocomManager.models.Actuality;
import com.bocom.bocomManager.models.AppUser;
import com.bocom.bocomManager.models.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActualityRepository extends JpaRepository<Actuality, Long> {
    Optional<Actuality> findByTitle(String title);
    List<Actuality> findAllByArchived(boolean archived);
    Page<Actuality> findAllByArchived(boolean archived, Pageable pageable);
}
