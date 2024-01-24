package com.bocom.bocomManager.repositories;

import com.bocom.bocomManager.enums.Status;
import com.bocom.bocomManager.models.Reclamation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {
    List<Reclamation> findByEmailOfApplicant(String emailOfApplicant);
    List<Reclamation> findAllByStatusAndArchived(Status status, boolean archived);

    List<Reclamation> findAllByServiceAndArchived(String service, boolean archived);
    List<Reclamation> findAllByArchived(boolean archived);
    Page<Reclamation> findAllByArchived(boolean archived, Pageable pageable);

    List<Reclamation> findAllBySubjectAndArchived(String subject, boolean archived);

    Page<Reclamation> findByEmailOfApplicant(String email, Pageable paging);
}
