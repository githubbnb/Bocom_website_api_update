package com.bocom.bocomManager.services;

import com.bocom.bocomManager.dto.PromotionRequest;
import com.bocom.bocomManager.models.Promotion;
import com.bocom.bocomManager.repositories.PromotionRepository;
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
public class PromotionService {

    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public Page<Promotion> getAllPromotions(boolean archived, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        return promotionRepository.findAllByArchived(archived, paging);
    }

    public List<Promotion> getAllPromotions(boolean archived) {
        return promotionRepository.findAllByArchived(archived);
    }

    public Promotion getOnePromotion(Long idUser) {
        return promotionRepository.findById(idUser).orElse(null);
    }

    public Promotion getOnePromotionByTitle(String title) {
        return promotionRepository.findByTitle(title).orElse(null);
    }

    public Promotion createPromotion(PromotionRequest promotionRequest) {
        Promotion onePromotionByTitle = getOnePromotionByTitle(promotionRequest.getTitle());

        if (onePromotionByTitle != null) {
            throw new RuntimeException("Une promotion existe déjà avec le titre " + promotionRequest.getTitle());
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String str = String.valueOf(authentication.getPrincipal());

        return promotionRepository.save(
                Promotion
                        .builder()
                        .archived(false)
                        .title(promotionRequest.getTitle())
                        .description(promotionRequest.getDescription())
                        .image(promotionRequest.getImage())
                        .createdBy("")
                        .updatedBy("")
                        .build()
        );
    }

    public Promotion updatePromotion(Long idPromotion, PromotionRequest promotionRequest) {
        Promotion onePromotion = getOnePromotion(idPromotion);

        if (onePromotion == null) {
            throw new RuntimeException("Aucune promotion n'existe avec l'identifiant " + idPromotion);
        }
        Promotion onePromotionByTitle = getOnePromotionByTitle(promotionRequest.getTitle());

        if (onePromotionByTitle != null && onePromotionByTitle.getId().equals(idPromotion)) {
            throw new RuntimeException("Une autre promotion existe déjà avec le titre " + promotionRequest.getTitle());
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String str = String.valueOf(authentication.getPrincipal());

        return promotionRepository.save(
                Promotion
                        .builder()
                        .id(idPromotion)
                        .archived(onePromotion.isArchived())
                        .title(promotionRequest.getTitle())
                        .description(promotionRequest.getDescription())
                        .image(promotionRequest.getImage())
                        .createdBy(onePromotion.getCreatedBy())
                        .updatedBy(str.split(";")[0])
                        .build()
        );
    }

    public boolean deletePromotion(Long idPromotion) {
        Promotion onePromotion = getOnePromotion(idPromotion);

        if (onePromotion == null) {
            throw new RuntimeException("Aucune promotion n'existe avec l'identifiant " + idPromotion);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String str = String.valueOf(authentication.getPrincipal());

        Promotion save = promotionRepository.save(
                Promotion
                        .builder()
                        .archived(true)
                        .title(onePromotion.getTitle())
                        .description(onePromotion.getDescription())
                        .image(onePromotion.getImage())
                        .createdBy(onePromotion.getCreatedBy())
                        .updatedBy(str.split(";")[0])
                        .build()
        );

        return save.getId() > 0;
    }

}
