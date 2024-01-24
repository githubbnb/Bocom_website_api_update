package com.bocom.bocomManager.repositories;

import com.bocom.bocomManager.enums.Filiale;
import com.bocom.bocomManager.enums.Status;
import com.bocom.bocomManager.models.Candidature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidatureRepository extends JpaRepository<Candidature, Long> {
    List<Candidature> findByEmail(String email);
    List<Candidature> findAllByFilialeAndArchived(Filiale filiale, boolean archived);
    List<Candidature> findAllByStatusAndArchived(Status status, boolean archived);
    Page<Candidature> findAllByArchived(boolean archived, Pageable pageable);
    List<Candidature> findAllByArchived(boolean archived);

    List<Candidature> findAllByPosteAndArchived(String poste, boolean archived);

    List<Candidature> findByTelephone(String telephone);
}
