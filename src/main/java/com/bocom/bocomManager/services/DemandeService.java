package com.bocom.bocomManager.services;

import com.bocom.bocomManager.dto.DemandeRequest;
import com.bocom.bocomManager.enums.DemandeType;
import com.bocom.bocomManager.enums.Filiale;
import com.bocom.bocomManager.enums.Status;
import com.bocom.bocomManager.models.Demande;
import com.bocom.bocomManager.repositories.DemandeRepository;
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
public class DemandeService {
    private  final DemandeRepository demandeRepository;

    public DemandeService(DemandeRepository demandeRepository) {
        this.demandeRepository = demandeRepository;
    }

    public Demande getOneDemandeById(Long  id) {
        return demandeRepository.findById(id).orElse(null);
    }

    public Page<Demande> getAllDemandes(boolean archived, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        return demandeRepository.findAllByArchived(archived, paging);
    }

    public Page<Demande> findByTypeAndArchived(String type, int page, int size, boolean archived) {
        Pageable paging = PageRequest.of(page, size);
        return demandeRepository.findAllByTypeAndArchived(DemandeType.valueOf(type), archived,paging);
    }

    public List<Demande> getAllDemandes(boolean archived) {
        return demandeRepository.findAllByArchived(archived);
    }

    public List<Demande> findAllByEmail(String email) {
        return  demandeRepository.findByEmailOfApplicant(email);
    }

    public List<Demande> findAllByFilialeAndArchived(String filiale, boolean archived) {
        return demandeRepository.findAllByFilialeAndArchived(Filiale.valueOf(filiale), archived);
    }

    public List<Demande> findAllByStatusAndArchived(String status, boolean archived) {
        return demandeRepository.findAllByStatusAndArchived(Status.valueOf(status), archived);
    }

    public List<Demande> findAllByTypeAndArchived(String type, boolean archived) {
        return demandeRepository.findAllByTypeAndArchived(DemandeType.valueOf(type), archived);
    }


    public Demande createDemande(DemandeRequest demandeRequest) {
        if (Strings.isNullOrEmpty(demandeRequest.getSocieteOfApplicant()) && Strings.isNullOrEmpty(demandeRequest.getNameOfApplicant())) {
            throw new RuntimeException("Veuillez entrer le nom de la societé ou votre nom afin de valider la demande");
        }

        if (demandeRequest.getType().equals(DemandeType.DEVIS)) {
            if (Strings.isNullOrEmpty(demandeRequest.getSocieteOfApplicant())){
                throw new RuntimeException("Veuillez entrer le nom de la societé afin de valider la demande");
            }
        }

        if (demandeRequest.getType().equals(DemandeType.PATNER)) {
            if (Strings.isNullOrEmpty(demandeRequest.getNameOfApplicant())){
                throw new RuntimeException("Veuillez entre votre nom afin de valider la demande");
            }
        }

        return demandeRepository.save(
                Demande
                        .builder()
                        .archived(false)
                        .filiale(demandeRequest.getFiliale())
                        .title(demandeRequest.getTitle())
                        .emailOfApplicant(demandeRequest.getEmailOfApplicant())
                        .telephoneOfApplicant(demandeRequest.getTelephoneOfApplicant())
                        .nameOfApplicant(demandeRequest.getNameOfApplicant())
                        .societeOfApplicant(demandeRequest.getSocieteOfApplicant())
                        .type(demandeRequest.getType())
                        .status(Status.EN_COURS)
                        .description(demandeRequest.getDescription())
                        .createdBy(Strings.isNullOrEmpty(demandeRequest.getNameOfApplicant()) ? demandeRequest.getSocieteOfApplicant() : demandeRequest.getNameOfApplicant())
                        .updatedBy(Strings.isNullOrEmpty(demandeRequest.getNameOfApplicant()) ? demandeRequest.getSocieteOfApplicant() : demandeRequest.getNameOfApplicant())
                        .build()
        );
    }

    public Demande updateDemande(Long idDemande, DemandeRequest demandeRequest) {
        Demande oneDemande = getOneDemandeById(idDemande);

        if (oneDemande == null) {
            throw new RuntimeException("Aucune demande n'existe avec l'identifiant " + idDemande);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String str = String.valueOf(authentication.getPrincipal());

        return demandeRepository.save(
                Demande
                        .builder()
                        .id(oneDemande.getId())
                        .archived(oneDemande.isArchived())
                        .filiale(demandeRequest.getFiliale())
                        .title(demandeRequest.getTitle())
                        .emailOfApplicant(demandeRequest.getEmailOfApplicant())
                        .telephoneOfApplicant(demandeRequest.getTelephoneOfApplicant())
                        .nameOfApplicant(demandeRequest.getNameOfApplicant())
                        .societeOfApplicant(demandeRequest.getSocieteOfApplicant())
                        .type(demandeRequest.getType())
                        .status(demandeRequest.getStatus())
                        .description(demandeRequest.getDescription())
                        .createdBy(oneDemande.getCreatedBy())
                        .updatedBy(str.split(";")[0])
                        .build()
        );
    }

    public boolean deleteDemande(Long idDemande) {
        Demande oneDemande = getOneDemandeById(idDemande);

        if (oneDemande == null) {
            throw new RuntimeException("Aucune demande n'existe avec l'identifiant " + idDemande);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String str = String.valueOf(authentication.getPrincipal());

        Demande save = demandeRepository.save(
                Demande
                        .builder()
                        .id(oneDemande.getId())
                        .archived(true)
                        .filiale(oneDemande.getFiliale())
                        .title(oneDemande.getTitle())
                        .telephoneOfApplicant(oneDemande.getTelephoneOfApplicant())
                        .emailOfApplicant(oneDemande.getEmailOfApplicant())
                        .societeOfApplicant(oneDemande.getSocieteOfApplicant())
                        .nameOfApplicant(oneDemande.getNameOfApplicant())
                        .type(oneDemande.getType())
                        .status(oneDemande.getStatus())
                        .description(oneDemande.getDescription())
                        .createdBy(oneDemande.getCreatedBy())
                        .updatedBy(str.split(";")[0])
                        .build()
        );

        return save.getId() > 0;
    }

    public List<Demande> findAllByTelephoneAndArchived(String telephone) {
        return demandeRepository.findAllByTelephoneOfApplicant(telephone);
    }
}
