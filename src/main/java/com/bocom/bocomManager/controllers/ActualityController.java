package com.bocom.bocomManager.controllers;

import com.bocom.bocomManager.dto.ActualityRequest;
import com.bocom.bocomManager.dto.PromotionRequest;
import com.bocom.bocomManager.interfaces.*;
import com.bocom.bocomManager.models.Actuality;
import com.bocom.bocomManager.services.ActualityService;
import com.bocom.bocomManager.services.PromotionService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("actualities")
public class ActualityController {
    private final ActualityService actualityService;

    public ActualityController(ActualityService actualityService) {
        this.actualityService = actualityService;
    }

    @GetMapping
    public Page<Actuality> getAllActualitys(
            @RequestParam(name = "archived", defaultValue = "false") boolean archived,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        return actualityService.getAllActualities(archived, page, size);
    }

    @GetMapping("all")
    public List<Actuality> getAllActualitys(
            @RequestParam(name = "archived", defaultValue = "false") boolean archived
    ) {
        return actualityService.getAllActualities(archived);
    }

    @GetMapping("{idActuality}")
    public Actuality getOneActuality(@PathVariable Long idActuality) {
        return actualityService.getOneActuality(idActuality);
    }

    @GetMapping("findByTitle/{title}")
    @hasRoleActualityRead
    public Actuality getOneActualityByTitle(@PathVariable String title) {
        return actualityService.getOneActualityByTitle(title);
    }

    @PostMapping
    @hasRoleActualityCreate
    public Actuality createActuality(@RequestBody ActualityRequest actualityRequest) {
        return actualityService.createActuality(actualityRequest);
    }

    @PutMapping("{idActuality}")
    @hasRoleActualityUpdate
    public Actuality updateActuality(@PathVariable Long idActuality, @RequestBody ActualityRequest actualityRequest) {
        return actualityService.updateActuality(idActuality, actualityRequest);
    }

    @DeleteMapping("{idActuality}")
    @hasRoleActualityDelete
    public boolean deleteActuality(@PathVariable Long idActuality) {
        return actualityService.deleteActuality(idActuality);
    }
}
