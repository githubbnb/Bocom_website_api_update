package com.bocom.bocomManager.repositories;

import com.bocom.bocomManager.enums.UserRole;
import com.bocom.bocomManager.models.Actuality;
import com.bocom.bocomManager.models.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    Optional<Promotion> findByTitle(String title);
    List<Promotion> findAllByArchived(boolean archived);
    Page<Promotion> findAllByArchived(boolean archived, Pageable pageable);
}
