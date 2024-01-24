package com.bocom.bocomManager.services;

import com.bocom.bocomManager.dto.ContactRequest;
import com.bocom.bocomManager.enums.Filiale;
import com.bocom.bocomManager.enums.Status;
import com.bocom.bocomManager.models.Contact;
import com.bocom.bocomManager.repositories.ContactRepository;
import com.google.common.base.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ContactService {

    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Contact getOneContactById(Long id) {
        return contactRepository.findById(id).orElse(null);
    }

    public Page<Contact> getAllContacts(boolean archived, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        return contactRepository.findAllByArchived(archived, paging);
    }

    public List<Contact> getAllContacts(boolean archived) {
        return contactRepository.findAllByArchived(archived);
    }

    public List<Contact> findAllByEmail(String email) {
        return contactRepository.findByEmailOfApplicant(email);
    }

    public List<Contact> findAllByFilialeAndArchived(String filiale, boolean archived) {
        return contactRepository.findAllByFilialeAndArchived(Filiale.valueOf(filiale), archived);
    }

    public List<Contact> findAllByStatusAndArchived(String status, boolean archived) {
        return contactRepository.findAllByStatusAndArchived(Status.valueOf(status), archived);
    }

    public List<Contact> findAllByTelephone(String telephone) {
        return contactRepository.findAllByTelephoneOfApplicant(telephone);
    }

    public Contact createContact(ContactRequest contactRequest) {
        if (Strings.isNullOrEmpty(contactRequest.getNom())) {
            throw new RuntimeException("Veuillez entrer votre nom");
        }

        return contactRepository.save(
                Contact
                        .builder()
                        .archived(false)
                        .title(contactRequest.getSujet())
                        .emailOfApplicant(contactRequest.getEmail())
                        .pays(contactRequest.getPays())
                        .filiale(contactRequest.getFiliale())
                        .telephoneOfApplicant(contactRequest.getTelephone())
                        .nameOfApplicant(contactRequest.getNom())
                        .message(contactRequest.getMessage())
                        .createdBy(contactRequest.getNom())
                        .updatedBy(contactRequest.getNom())
                        .build()
        );
    }

    public Contact updateContact(Long idContact, ContactRequest contactRequest) {
        Contact oneContact = getOneContactById(idContact);

        if (oneContact == null) {
            throw new RuntimeException("Aucun contact n'existe avec l'identifiant " + idContact);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String str = String.valueOf(authentication.getPrincipal());

        return contactRepository.save(
                Contact
                        .builder()
                        .id(oneContact.getId())
                        .archived(oneContact.isArchived())
                        .title(contactRequest.getSujet())
                        .emailOfApplicant(contactRequest.getEmail())
                        .pays(contactRequest.getPays())
                        .filiale(contactRequest.getFiliale())
                        .telephoneOfApplicant(contactRequest.getTelephone())
                        .nameOfApplicant(contactRequest.getNom())
                        .message(contactRequest.getMessage())
                        .createdBy(oneContact.getCreatedBy())
                        .updatedBy(str.split(";")[0])
                        .build()
        );
    }

    public boolean deleteContact(Long idContact) {
        Contact oneContact = getOneContactById(idContact);

        if (oneContact == null) {
            throw new RuntimeException("Aucune contact n'existe avec l'identifiant " + idContact);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String str = String.valueOf(authentication.getPrincipal());

        Contact save = contactRepository.save(
                Contact
                        .builder()
                        .id(oneContact.getId())
                        .archived(true)
                        .title(oneContact.getTitle())
                        .emailOfApplicant(oneContact.getEmailOfApplicant())
                        .pays(oneContact.getPays())
                        .filiale(oneContact.getFiliale())
                        .telephoneOfApplicant(oneContact.getTelephoneOfApplicant())
                        .nameOfApplicant(oneContact.getNameOfApplicant())
                        .message(oneContact.getMessage())
                        .createdBy(oneContact.getCreatedBy())
                        .updatedBy(str.split(";")[0])
                        .build()
        );

        return save.getId() > 0;
    }
}
