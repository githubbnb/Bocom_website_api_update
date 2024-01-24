package com.bocom.bocomManager.services;

import com.bocom.bocomManager.dto.ReclamationRequest;
import com.bocom.bocomManager.emailApi.EmailSender;
import com.bocom.bocomManager.enums.Status;
import com.bocom.bocomManager.models.Reclamation;
import com.bocom.bocomManager.repositories.ReclamationRepository;
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
public class ReclamationService {

    private final ReclamationRepository reclamationRepository;
    private final EmailSender emailSender;

    public ReclamationService(ReclamationRepository reclamationRepository, EmailSender emailSender) {
        this.reclamationRepository = reclamationRepository;
        this.emailSender = emailSender;
    }

    public Reclamation getOneReclamationById(Long id) {
        return reclamationRepository.findById(id).orElse(null);
    }

    public Page<Reclamation> getAllReclamations(boolean archived, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        return reclamationRepository.findAllByArchived(archived, paging);
    }

    public Page<Reclamation> findByEmail(String email, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        return reclamationRepository.findByEmailOfApplicant(email, paging);
    }

    public List<Reclamation> getAllReclamations(boolean archived) {
        return reclamationRepository.findAllByArchived(archived);
    }

    public List<Reclamation> findAllByEmail(String email) {
        return reclamationRepository.findByEmailOfApplicant(email);
    }

    public List<Reclamation> findAllBySubjectAndArchived(String subject, boolean archived) {
        return reclamationRepository.findAllBySubjectAndArchived(subject,archived);
    }

    public List<Reclamation> findAllByServiceAndArchived(String service, boolean archived) {
        return reclamationRepository.findAllByServiceAndArchived(service, archived);
    }

    public List<Reclamation> findAllByStatusAndArchived(String status, boolean archived) {
        return reclamationRepository.findAllByStatusAndArchived(Status.valueOf(status), archived);
    }

    public Reclamation createReclamation(ReclamationRequest reclamationRequest) {
        if (Strings.isNullOrEmpty(reclamationRequest.getNameOfApplicant())) {
            throw new RuntimeException("Veuillez entrer votre nom afin de valider la reclamation");
        }
        emailSender.send(reclamationRequest.getEmailOfApplicant(), buildEmail(reclamationRequest.getNameOfApplicant()));
        return reclamationRepository.save(
                Reclamation
                        .builder()
                        .archived(false)
                        .subject(reclamationRequest.getSubject())
                        .emailOfApplicant(reclamationRequest.getEmailOfApplicant())
                        .service(reclamationRequest.getService())
                        .nameOfApplicant(reclamationRequest.getNameOfApplicant())
                        .message(reclamationRequest.getMessage())
                        .status(Status.EN_COURS)
                        .createdBy(reclamationRequest.getNameOfApplicant())
                        .updatedBy(reclamationRequest.getNameOfApplicant())
                        .build()
        );

    }

    public Reclamation updateReclamation(Long idReclamation, ReclamationRequest reclamationRequest) {
        Reclamation oneReclamation = getOneReclamationById(idReclamation);

        if (oneReclamation == null) {
            throw new RuntimeException("Aucune reclamation n'existe avec l'identifiant " + idReclamation);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String str = String.valueOf(authentication.getPrincipal());

        return reclamationRepository.save(
                Reclamation
                        .builder()
                        .id(oneReclamation.getId())
                        .archived(oneReclamation.isArchived())
                        .subject(reclamationRequest.getSubject())
                        .emailOfApplicant(reclamationRequest.getEmailOfApplicant())
                        .service(reclamationRequest.getService())
                        .nameOfApplicant(reclamationRequest.getNameOfApplicant())
                        .message(reclamationRequest.getMessage())
                        .status(reclamationRequest.getStatus())
                        .createdBy(oneReclamation.getCreatedBy())
                        .updatedBy(str.split(";")[0])
                        .build()
        );

    }

    public boolean deleteReclamation(Long idReclamation) {
        Reclamation oneReclamation = getOneReclamationById(idReclamation);

        if (oneReclamation == null) {
            throw new RuntimeException("Aucune reclamation n'existe avec l'identifiant " + idReclamation);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String str = String.valueOf(authentication.getPrincipal());

        Reclamation save = reclamationRepository.save(
                Reclamation
                        .builder()
                        .id(oneReclamation.getId())
                        .archived(true)
                        .subject(oneReclamation.getSubject())
                        .emailOfApplicant(oneReclamation.getEmailOfApplicant())
                        .service(oneReclamation.getService())
                        .nameOfApplicant(oneReclamation.getNameOfApplicant())
                        .message(oneReclamation.getMessage())
                        .status(oneReclamation.getStatus())
                        .createdBy(oneReclamation.getCreatedBy())
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
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Your Reclamation has been received and is being processed by our team. We will get back to you as soon as possible. THANKS FOR YOUR TRUST: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" +
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
