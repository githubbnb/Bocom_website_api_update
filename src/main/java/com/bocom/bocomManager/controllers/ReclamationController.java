package com.bocom.bocomManager.controllers;

import com.bocom.bocomManager.dto.ReclamationRequest;
import com.bocom.bocomManager.interfaces.hasRoleReclamationDelete;
import com.bocom.bocomManager.interfaces.hasRoleReclamationUpdate;
import com.bocom.bocomManager.models.Reclamation;
import com.bocom.bocomManager.services.ReclamationService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("reclamations")
public class ReclamationController {
    private final ReclamationService reclamationService;

    public ReclamationController(ReclamationService reclamationService) {
        this.reclamationService = reclamationService;
    }

    @GetMapping
    public Page<Reclamation> getAllReclamations(
            @RequestParam(name = "archived", defaultValue = "false") boolean archived,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        return reclamationService.getAllReclamations(archived, page, size);
    }

    @GetMapping("all")
    public List<Reclamation> getAllReclamations(
            @RequestParam(name = "archived", defaultValue = "false") boolean archived
    ) {
        return reclamationService.getAllReclamations(archived);
    }

    @GetMapping("{idReclamation}")
    public Reclamation getOneReclamationById(@PathVariable("idReclamation") Long idReclamation) {
        return reclamationService.getOneReclamationById(idReclamation);
    }

    @GetMapping("findAllByEmail/{email}")
    public List<Reclamation> findAllByEmail(@PathVariable("email") String email) {
        return reclamationService.findAllByEmail(email);
    }

    @GetMapping("findByEmail/{email}")
    public Page<Reclamation> findByEmail(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @PathVariable("email") String email) {
        return reclamationService.findByEmail(email, page, size);
    }

    @GetMapping("findAllBySubject/{subject}")
    public List<Reclamation> findAllBySubject(@PathVariable("subject") String subject,
                                              @RequestParam(name = "archived", defaultValue = "false") boolean archived) {
        return reclamationService.findAllBySubjectAndArchived(subject, archived);
    }

    @GetMapping("findAllByService/{service}")
    public List<Reclamation> findAllByServiceAndArchived(String service, @RequestParam(name = "archived", defaultValue = "false") boolean archived) {
        return reclamationService.findAllByServiceAndArchived(service, archived);
    }

    @GetMapping("findAllByStatus/{status}")
    public List<Reclamation> findAllByStatusAndArchived(@PathVariable("status") String status
            , @RequestParam(name = "archived", defaultValue = "false") boolean archived) {
        return reclamationService.findAllByStatusAndArchived(status, archived);
    }

    @PostMapping
    public Reclamation createReclamation(@RequestBody ReclamationRequest reclamationRequest) {
        return reclamationService.createReclamation(reclamationRequest);
    }
    

    @PutMapping("{idReclamation}")
    @hasRoleReclamationUpdate
    public Reclamation updateReclamation(@PathVariable Long idReclamation, @RequestBody ReclamationRequest reclamationRequest) {
        return reclamationService.updateReclamation(idReclamation, reclamationRequest);
    }

    @DeleteMapping("{idReclamation}")
    @hasRoleReclamationDelete
    public boolean deleteReclamation(@PathVariable Long idReclamation) {
        return reclamationService.deleteReclamation(idReclamation);
    }
}
