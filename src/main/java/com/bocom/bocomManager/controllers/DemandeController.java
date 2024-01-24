package com.bocom.bocomManager.controllers;

import com.bocom.bocomManager.dto.DemandeRequest;
import com.bocom.bocomManager.interfaces.hasRoleDemandeCreate;
import com.bocom.bocomManager.interfaces.hasRoleDemandeDelete;
import com.bocom.bocomManager.interfaces.hasRoleDemandeUpdate;
import com.bocom.bocomManager.models.Demande;
import com.bocom.bocomManager.services.DemandeService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("demandes")
public class DemandeController {
    private final DemandeService demandeService;

    public DemandeController(DemandeService demandeService) {
        this.demandeService = demandeService;
    }

    @GetMapping
    public Page<Demande> getAllDemandes(
            @RequestParam(name = "archived", defaultValue = "false") boolean archived,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        return demandeService.getAllDemandes(archived, page, size);
    }

    @GetMapping("findByType/{type}")
    public Page<Demande> findAllByTypeAndArchived(
            @RequestParam(name = "archived", defaultValue = "false") boolean archived,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @PathVariable("type") String type) {
        return demandeService.findByTypeAndArchived(type, page, size,archived);
    }

    @GetMapping("all")
    public List<Demande> getAllDemandes(
            @RequestParam(name = "archived", defaultValue = "false") boolean archived
    ) {
        return demandeService.getAllDemandes(archived);
    }

    @GetMapping("{idDemande}")
    public Demande getOneDemandeById(@PathVariable("idDemande") Long idDemande) {
        return demandeService.getOneDemandeById(idDemande);
    }

    @GetMapping("findAllByEmail/{email}")
    public List<Demande> findAllByEmail(@PathVariable("email") String email) {
        return demandeService.findAllByEmail(email);
    }

    @GetMapping("findAllByFiliale/{filiale}")
    public List<Demande> findAllByFilialeAndArchived(@PathVariable("filiale") String filiale, @RequestParam(name = "archived", defaultValue = "false") boolean archived) {
        return demandeService.findAllByFilialeAndArchived(filiale,archived);
    }

    @GetMapping("findAllByStatus/{status}")
    public List<Demande> findAllByStatusAndArchived(@PathVariable("status") String status, @RequestParam(name = "archived", defaultValue = "false") boolean archived) {
        return demandeService.findAllByStatusAndArchived(status,archived);
    }

    @GetMapping("findAllByType/{type}")
    public List<Demande> findAllByTypeAndArchived(@PathVariable("type") String type,
                                                  @RequestParam(name = "archived", defaultValue = "false") boolean archived) {
        return demandeService.findAllByTypeAndArchived(type,archived);
    }

    @GetMapping("findAllByTelephone/{telephone}")
    public List<Demande> findAllByTelephoneAndArchived(@PathVariable("telephone") String telephone) {
        return demandeService.findAllByTelephoneAndArchived(telephone);
    }

    @PostMapping
    public Demande createDemande(@RequestBody DemandeRequest demandeRequest) {
        return demandeService.createDemande(demandeRequest);
    }

    @PutMapping("{idDemande}")
    @hasRoleDemandeUpdate
    public Demande updateDemande(@PathVariable Long idDemande,@RequestBody DemandeRequest demandeRequest) {
        return demandeService.updateDemande(idDemande, demandeRequest);
    }

    @DeleteMapping("{idDemande}")
    @hasRoleDemandeDelete
    public boolean deleteDemande(@PathVariable Long idDemande) {
        return demandeService.deleteDemande(idDemande);
    }
}
