package com.bocom.bocomManager.services;

import com.bocom.bocomManager.dto.ActualityRequest;
import com.bocom.bocomManager.models.Actuality;
import com.bocom.bocomManager.repositories.ActualityRepository;
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
public class ActualityService {

    private final ActualityRepository actualityRepository;

    public ActualityService(ActualityRepository actualityRepository) {
        this.actualityRepository = actualityRepository;
    }

    public Page<Actuality> getAllActualities(boolean archived, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        return actualityRepository.findAllByArchived(archived, paging);
    }

    public List<Actuality> getAllActualities(boolean archived) {
        return actualityRepository.findAllByArchived(archived);
    }

    public Actuality getOneActuality(Long idActuality) {
        return actualityRepository.findById(idActuality).orElse(null);
    }

    public Actuality getOneActualityByTitle(String title) {
        return actualityRepository.findByTitle(title).orElse(null);
    }

    public Actuality createActuality(ActualityRequest actualityRequest) {
        Actuality oneActualityByTitle = getOneActualityByTitle(actualityRequest.getTitle());

        if (oneActualityByTitle != null) {
            throw new RuntimeException("Une actuality existe déjà avec le titre " + actualityRequest.getTitle());
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String str = String.valueOf(authentication.getPrincipal());

        return actualityRepository.save(
                Actuality
                        .builder()
                        .archived(false)
                        .title(actualityRequest.getTitle())
                        .description(actualityRequest.getDescription())
                        .images(actualityRequest.getImages())
                        .videos(actualityRequest.getVideos())
                        .createdBy(str.split(";")[0])
                        .updatedBy(str.split(";")[0])
                        .build()
        );
    }

    public Actuality updateActuality(Long idActuality, ActualityRequest actualityRequest) {
        Actuality oneActuality = getOneActuality(idActuality);

        if (oneActuality == null) {
            throw new RuntimeException("Aucune actuality n'existe avec l'identifiant " + idActuality);
        }
        Actuality oneActualityByTitle = getOneActualityByTitle(actualityRequest.getTitle());

        if (oneActualityByTitle != null && oneActualityByTitle.getId().equals(idActuality)) {
            throw new RuntimeException("Une autre actuality existe déjà avec le titre " + actualityRequest.getTitle());
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String str = String.valueOf(authentication.getPrincipal());

        return actualityRepository.save(
                Actuality
                        .builder()
                        .id(idActuality)
                        .archived(oneActuality.isArchived())
                        .title(actualityRequest.getTitle())
                        .description(actualityRequest.getDescription())
                        .images(actualityRequest.getImages())
                        .videos(actualityRequest.getVideos())
                        .createdBy(oneActuality.getCreatedBy())
                        .updatedBy(str.split(";")[0])
                        .build()
        );
    }

    public boolean deleteActuality(Long idActuality) {
        Actuality oneActuality = getOneActuality(idActuality);

        if (oneActuality == null) {
            throw new RuntimeException("Aucune actuality n'existe avec l'identifiant " + idActuality);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String str = String.valueOf(authentication.getPrincipal());

        Actuality save = actualityRepository.save(
                Actuality
                        .builder()
                        .archived(true)
                        .title(oneActuality.getTitle())
                        .description(oneActuality.getDescription())
                        .images(oneActuality.getImages())
                        .videos(oneActuality.getVideos())
                        .createdBy(oneActuality.getCreatedBy())
                        .updatedBy(str.split(";")[0])
                        .build()
        );

        return save.getId() > 0;
    }

}
