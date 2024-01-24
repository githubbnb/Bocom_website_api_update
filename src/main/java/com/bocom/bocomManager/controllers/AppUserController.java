package com.bocom.bocomManager.controllers;

import com.bocom.bocomManager.dto.appUser.AppUserRequest;
import com.bocom.bocomManager.dto.appUser.AppUserResponse;
import com.bocom.bocomManager.interfaces.*;
import com.bocom.bocomManager.models.AppUser;
import com.bocom.bocomManager.services.AppUserService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("users")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping
    @hasRoleUserRead
    public Page<AppUser> getAllAppUsers(
            @RequestParam(name = "block", defaultValue = "false") boolean block,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
            ) {
        return appUserService.getAllAppUsers(block, page, size);
    }

    @GetMapping("all")
    @hasRoleUserRead
    public List<AppUser> getAllAppUsers(@RequestParam(name = "block", defaultValue = "false") boolean block) {
        return appUserService.getAllAppUsers(block);
    }

    @GetMapping("findByRole/{role}")
    @hasRoleUserRead
    public List<AppUser> getAllAppUsersByRole(@PathVariable("role") String role) {
        return appUserService.getAllAppUsersByRole(role);
    }

    @GetMapping("find/role/{role}/block/{block}")
    @hasRoleUserRead
    public List<AppUser> getAllAppUsersByRole(@PathVariable("role") String role, @PathVariable("block") boolean block) {
        return appUserService.getAllAppUsersByRole(role, block);
    }

    @GetMapping("{idUser}")
    @hasRoleUserRead
    public AppUser getOneAppUser(@PathVariable Long idUser) {
        return appUserService.getOneAppUser(idUser);
    }

    @GetMapping("findByEmail/{email}")
    @hasRoleUserRead
    public AppUser getOneAppUserByEmail(@PathVariable String email) {
        return appUserService.getOneAppUserByEmail(email);
    }

    @GetMapping("findByTelephone/{telephone}")
    @hasRoleUserRead
    public AppUser getOneAppUserByTelephone(@PathVariable String telephone) {
        return appUserService.getOneAppUserByTelephone(telephone);
    }

    @PostMapping
    @hasRoleUserCreate
    public AppUserResponse createAppUser(@RequestBody AppUserRequest appUserRequest) {
        return appUserService.createAppUser(appUserRequest);
    }

    @PutMapping("{idAppUser}")
    @hasRoleUserUpdate
    public AppUserResponse updateAppUser(@PathVariable("idAppUser") Long idAppUser, @RequestBody AppUserRequest appUserRequest) {
        return appUserService.updateAppUser(idAppUser, appUserRequest);
    }

    @DeleteMapping("{idAppUser}")
    @hasRoleUserDelete
    public boolean deleteAppUser(@PathVariable Long idAppUser) {
        return appUserService.deleteAppUser(idAppUser);
    }
}
