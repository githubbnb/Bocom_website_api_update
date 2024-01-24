package com.bocom.bocomManager.services;

import com.bocom.bocomManager.dto.CandidatureRequest;
import com.bocom.bocomManager.emailApi.EmailSender;
import com.bocom.bocomManager.enums.Filiale;
import com.bocom.bocomManager.enums.Status;
import com.bocom.bocomManager.models.Candidature;
import com.bocom.bocomManager.repositories.CandidatureRepository;
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
public class CandidatureService {
    private final CandidatureRepository candidatureRepository;
    private final EmailSender emailSender;

    public CandidatureService(CandidatureRepository candidatureRepository, EmailSender emailSender) {
        this.candidatureRepository = candidatureRepository;
        this.emailSender = emailSender;
    }

    public Candidature getOneCandidatureById(Long id) {
        return candidatureRepository.findById(id).orElse(null);
    }

    public Page<Candidature> getAllCandidatures(boolean archived, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        return candidatureRepository.findAllByArchived(archived, paging);
    }

    public List<Candidature> getAllCandidatures(boolean archived) {
        return candidatureRepository.findAllByArchived(archived);
    }

    public List<Candidature> findAllByEmail(String email) {
        return candidatureRepository.findByEmail(email);
    }

    public List<Candidature> findAllByTelephone(String telephone) {
        return candidatureRepository.findByTelephone(telephone);
    }

    public List<Candidature> findAllByPoste(String poste, boolean archived) {
        return candidatureRepository.findAllByPosteAndArchived(poste,archived);
    }

    public List<Candidature> findAllByFilialeAndArchived(String filiale, boolean archived) {
        return candidatureRepository.findAllByFilialeAndArchived(Filiale.valueOf(filiale), archived);
    }

    public List<Candidature> findAllByStatusAndArchived(String status, boolean archived) {
        return candidatureRepository.findAllByStatusAndArchived(Status.valueOf(status), archived);
    }

    public Candidature createCandidature(CandidatureRequest candidatureRequest) {
        if (Strings.isNullOrEmpty(candidatureRequest.getNom())) {
            throw new RuntimeException("Veuillez entrer votre nom");
        }

        if (candidatureRequest.getFiles().size() <= 1) {
            throw new RuntimeException("Veuillez associer les fichiers suivants à votre candidature : CV, Lettre de motivation, Photocopie d'acte de naissance");
        }
        emailSender.send(candidatureRequest.getEmail(), buildEmail(candidatureRequest.getNom()));

        return candidatureRepository.save(
                Candidature
                        .builder()
                        .archived(true)
                        .poste(candidatureRequest.getPoste())
                        .email(candidatureRequest.getEmail())
                        .status(Status.EN_COURS)
                        .filiale(candidatureRequest.getFiliale())
                        .telephone(candidatureRequest.getTelephone())
                        .nom(candidatureRequest.getNom())
                        .message(candidatureRequest.getMessage())
                        .files(candidatureRequest.getFiles())
                        .createdBy(candidatureRequest.getNom())
                        .updatedBy(candidatureRequest.getNom())
                        .build()
        );
    }

    public Candidature updateCandidature(Long idCandidature, CandidatureRequest candidatureRequest) {
        Candidature oneCandidature = getOneCandidatureById(idCandidature);

        if (oneCandidature == null) {
            throw new RuntimeException("Aucune candidature n'existe avec l'identifiant " + idCandidature);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String str = String.valueOf(authentication.getPrincipal());

        return candidatureRepository.save(
                Candidature
                        .builder()
                        .id(oneCandidature.getId())
                        .archived(oneCandidature.isArchived())
                        .poste(candidatureRequest.getPoste())
                        .email(candidatureRequest.getEmail())
                        .status(candidatureRequest.getStatus())
                        .filiale(candidatureRequest.getFiliale())
                        .telephone(candidatureRequest.getTelephone())
                        .nom(candidatureRequest.getNom())
                        .message(candidatureRequest.getMessage())
                        .files(candidatureRequest.getFiles())
                        .createdBy(oneCandidature.getCreatedBy())
                        .updatedBy(str.split(";")[0])
                        .build()
        );
    }

    public boolean deleteCandidature(Long idCandidature) {
        Candidature oneCandidature = getOneCandidatureById(idCandidature);

        if (oneCandidature == null) {
            throw new RuntimeException("Aucun candidature n'existe avec l'identifiant " + idCandidature);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String str = String.valueOf(authentication.getPrincipal());

        Candidature save = candidatureRepository.save(
                Candidature
                        .builder()
                        .id(oneCandidature.getId())
                        .archived(true)
                        .poste(oneCandidature.getPoste())
                        .email(oneCandidature.getEmail())
                        .status(oneCandidature.getStatus())
                        .filiale(oneCandidature.getFiliale())
                        .telephone(oneCandidature.getTelephone())
                        .nom(oneCandidature.getNom())
                        .message(oneCandidature.getMessage())
                        .files(oneCandidature.getFiles())
                        .createdBy(oneCandidature.getCreatedBy())
                        .updatedBy(str.split(";")[0])
                        .build()
        );

        return save.getId() > 0;
    }
    private String buildEmail(String name) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirmation EMAIL</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Your candidature has been received and is being processed by our team. We will get back to you as soon as possible. THANKS FOR YOUR TRUST: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}
