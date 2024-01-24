package com.bocom.bocomManager.controllers;

import com.bocom.bocomManager.dto.PromotionRequest;
import com.bocom.bocomManager.interfaces.*;
import com.bocom.bocomManager.models.Promotion;
import com.bocom.bocomManager.services.PromotionService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("promotions")
public class PromotionController {
    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping
    public Page<Promotion> getAllPromotions(
            @RequestParam(name = "archived", defaultValue = "false") boolean archived,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        return promotionService.getAllPromotions(archived, page, size);
    }

    @GetMapping("all")
    public List<Promotion> getAllPromotions(
            @RequestParam(name = "archived", defaultValue = "false") boolean archived
    ) {
        return promotionService.getAllPromotions(archived);
    }

    @GetMapping("{idPromotion}")
    public Promotion getOnePromotion(@PathVariable Long idPromotion) {
        return promotionService.getOnePromotion(idPromotion);
    }

    @GetMapping("findByTitle/{title}")
    @hasRolePromotionRead
    public Promotion getOnePromotionByTitle(@PathVariable String title) {
        return promotionService.getOnePromotionByTitle(title);
    }

    @PostMapping
    @hasRolePromotionCreate
    public Promotion createPromotion(@RequestBody PromotionRequest promotionRequest) {
        return promotionService.createPromotion(promotionRequest);
    }

    @PutMapping("{idPromotion}")
    @hasRolePromotionUpdate
    public Promotion updatePromotion(@PathVariable Long idPromotion,@RequestBody PromotionRequest promotionRequest) {
        return promotionService.updatePromotion(idPromotion, promotionRequest);
    }

    @DeleteMapping("{idPromotion}")
    @hasRolePromotionDelete
    public boolean deletePromotion(@PathVariable Long idPromotion) {
        return promotionService.deletePromotion(idPromotion);
    }
}
