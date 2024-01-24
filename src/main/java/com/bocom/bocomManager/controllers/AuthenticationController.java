package com.bocom.bocomManager.controllers;

import com.bocom.bocomManager.dto.AuthenticationResponse;
import com.bocom.bocomManager.dto.Credential;
import com.bocom.bocomManager.dto.appUser.AppUserResponse;
import com.bocom.bocomManager.services.AuthService;
import com.bocom.bocomManager.services.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin("*")
@RequestMapping("auth")
@Slf4j
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthService authService;

    public AuthenticationController(JwtService jwtService, AuthService authService) {
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @GetMapping("/generated/first/admin")
    public AppUserResponse generatedFirstAdmin() {
        return authService.generatedFirstAdmin();
    }

    @PostMapping("login")
    @Operation(summary = "Login to application for have access access to application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponse.class))}),
            @ApiResponse(responseCode = "500", description = "the server found problems",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "user not found",
                    content = @Content)})
    public ResponseEntity<?> login(@RequestBody @Valid Credential credential) {
        AuthenticationResponse authenticationResponse = authService.login(credential);

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + authenticationResponse.getAccessToken())
                .body(authenticationResponse);
    }

}
