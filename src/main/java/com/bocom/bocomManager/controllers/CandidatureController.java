package com.bocom.bocomManager.controllers;

import com.bocom.bocomManager.dto.CandidatureRequest;
import com.bocom.bocomManager.interfaces.hasRoleCandidatureDelete;
import com.bocom.bocomManager.interfaces.hasRoleCandidatureUpdate;
import com.bocom.bocomManager.models.Candidature;
import com.bocom.bocomManager.services.CandidatureService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("candidatures")
public class CandidatureController {
    private final CandidatureService candidatureService;

    public CandidatureController(CandidatureService candidatureService) {
        this.candidatureService = candidatureService;
    }

    @GetMapping
    public Page<Candidature> getAllCandidatures(
            @RequestParam(name = "archived", defaultValue = "false") boolean archived,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        return candidatureService.getAllCandidatures(archived, page, size);
    }

    @GetMapping("all")
    public List<Candidature> getAllCandidatures(
            @RequestParam(name = "archived", defaultValue = "false") boolean archived
    ) {
        return candidatureService.getAllCandidatures(archived);
    }

    @GetMapping("{idCandidature}")
    public Candidature getOneCandidatureById(@PathVariable("idCandidature") Long idCandidature) {
        return candidatureService.getOneCandidatureById(idCandidature);
    }

    @GetMapping("findAllByEmail/{email}")
    public List<Candidature> findAllByEmail(@PathVariable("email") String email) {
        return candidatureService.findAllByEmail(email);
    }

    @GetMapping("findAllByPoste/{poste}")
    public List<Candidature> findAllByPoste(@PathVariable("poste") String poste,
                                            @RequestParam(name = "archived", defaultValue = "false") boolean archived) {
        return candidatureService.findAllByPoste(poste,archived);
    }

    @GetMapping("findAllByTelephone/{telephone}")
    public List<Candidature> findAllByTelephone(@PathVariable("telephone") String telephone) {
        return candidatureService.findAllByTelephone(telephone);
    }

    @GetMapping("findAllByFiliale/{filiale}")
    public List<Candidature> findAllByFilialeAndArchived(@PathVariable("filiale") String filiale,
                                                         @RequestParam(name = "archived", defaultValue = "false") boolean archived) {
        return candidatureService.findAllByFilialeAndArchived(filiale,archived);
    }

    @GetMapping("findAllByStatus/{status}")
    public List<Candidature> findAllByStatusAndArchived(String status, boolean archived) {
        return candidatureService.findAllByStatusAndArchived(status,archived);
    }

    @PostMapping
    public Candidature createCandidature(@RequestBody CandidatureRequest candidatureRequest) {
        return candidatureService.createCandidature(candidatureRequest);
    }

    @PutMapping("{idCandidature}")
    @hasRoleCandidatureUpdate
    public Candidature updateCandidature(@PathVariable Long idCandidature, @RequestBody CandidatureRequest candidatureRequest) {
        return candidatureService.updateCandidature(idCandidature, candidatureRequest);
    }

    @DeleteMapping("{idCandidature}")
    @hasRoleCandidatureDelete
    public boolean deleteCandidature(@PathVariable Long idCandidature) {
        return candidatureService.deleteCandidature(idCandidature);
    }
}
