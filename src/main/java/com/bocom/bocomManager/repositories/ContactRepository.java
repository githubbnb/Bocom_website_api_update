package com.bocom.bocomManager.repositories;

import com.bocom.bocomManager.enums.Filiale;
import com.bocom.bocomManager.enums.Status;
import com.bocom.bocomManager.models.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository  extends JpaRepository<Contact, Long> {
    List<Contact> findByEmailOfApplicant(String emailOfApplicant);
    List<Contact> findAllByFilialeAndArchived(Filiale filiale, boolean archived);
    Page<Contact> findAllByArchived(boolean archived, Pageable pageable);
    List<Contact> findAllByArchived(boolean archived);
    List<Contact> findAllByStatusAndArchived(Status status, boolean archived);
    List<Contact> findAllByTelephoneOfApplicant(String telephone);
}
