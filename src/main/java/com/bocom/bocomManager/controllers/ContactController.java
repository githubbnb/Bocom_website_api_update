package com.bocom.bocomManager.controllers;

import com.bocom.bocomManager.dto.CandidatureRequest;
import com.bocom.bocomManager.dto.ContactRequest;
import com.bocom.bocomManager.interfaces.hasRoleCandidatureDelete;
import com.bocom.bocomManager.interfaces.hasRoleCandidatureUpdate;
import com.bocom.bocomManager.interfaces.hasRoleContactDelete;
import com.bocom.bocomManager.interfaces.hasRoleContactUpdate;
import com.bocom.bocomManager.models.Candidature;
import com.bocom.bocomManager.models.Contact;
import com.bocom.bocomManager.models.Reclamation;
import com.bocom.bocomManager.services.CandidatureService;
import com.bocom.bocomManager.services.ContactService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("contacts")
public class ContactController {
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public Page<Contact> getAllContacts(
            @RequestParam(name = "archived", defaultValue = "false") boolean archived,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        return contactService.getAllContacts(archived, page, size);
    }

    @GetMapping("all")
    public List<Contact> getAllContacts(
            @RequestParam(name = "archived", defaultValue = "false") boolean archived
    ) {
        return contactService.getAllContacts(archived);
    }

    @GetMapping("{idContact}")
    public Contact getOneContactById(@PathVariable("idContact") Long idContact) {
        return contactService.getOneContactById(idContact);
    }


    @GetMapping("findAllByTelephone/{telephone}")
    public List<Contact> findAllByTelephone(@PathVariable("telephone") String telephone) {
        return contactService.findAllByTelephone(telephone);
    }


    @GetMapping("findAllByStatus/{status}")
    public List<Contact> findAllByStatusAndArchived(@PathVariable("status") String status, @RequestParam(name = "archived", defaultValue = "false") boolean archived) {
        return contactService.findAllByStatusAndArchived(status,archived);
    }

    @GetMapping("findAllByEmail/{email}")
    public List<Contact> findAllByEmail(@PathVariable("email") String email) {
        return contactService.findAllByEmail(email);
    }

    @GetMapping("findAllByFiliale/{filiale}")
    public List<Contact> findAllByFilialeAndArchived(@PathVariable("filiale") String filiale, @RequestParam(name = "archived", defaultValue = "false") boolean archived) {
        return contactService.findAllByFilialeAndArchived(filiale,archived);
    }

    @PostMapping
    public Contact createContact(@RequestBody ContactRequest contactRequest) {
        return contactService.createContact(contactRequest);
    }

    @PutMapping("{idContact}")
    @hasRoleContactUpdate
    public Contact updateContact(@PathVariable Long idContact, @RequestBody ContactRequest contactRequest) {
        return contactService.updateContact(idContact, contactRequest);
    }

    @DeleteMapping("{idContact}")
    @hasRoleContactDelete
    public boolean deleteContact(@PathVariable Long idContact) {
        return contactService.deleteContact(idContact);
    }
}
