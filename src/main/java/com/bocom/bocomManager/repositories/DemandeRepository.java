package com.bocom.bocomManager.repositories;

import com.bocom.bocomManager.enums.DemandeType;
import com.bocom.bocomManager.enums.Filiale;
import com.bocom.bocomManager.enums.Status;
import com.bocom.bocomManager.models.Demande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeRepository  extends JpaRepository<Demande, Long> {
    List<Demande> findByEmailOfApplicant(String emailOfApplicant);
    List<Demande> findAllByFilialeAndArchived(Filiale filiale, boolean archived);
    List<Demande> findAllByStatusAndArchived(Status status, boolean archived);
    List<Demande> findAllByTypeAndArchived(DemandeType type, boolean archived);
    Page<Demande> findAllByTypeAndArchived(DemandeType type, boolean archived, Pageable pageable);
    List<Demande> findAllByArchived(boolean archived);
    Page<Demande> findAllByArchived(boolean archived, Pageable pageable);

    List<Demande> findAllByTelephoneOfApplicant(String telephone);
}
