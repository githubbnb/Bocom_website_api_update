package com.bocom.bocomManager.dto;

import com.bocom.bocomManager.enums.Filiale;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactRequest {
    @NotNull
    @NotEmpty
    @NotBlank
    private String nom;

    private String pays;

    @NotNull
    @NotEmpty
    @NotBlank
    private String sujet;

    @NotNull
    @NotEmpty
    @NotBlank
    private String telephone;

    @NotNull
    @NotEmpty
    @NotBlank
    private String message;


    @Email
    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private Filiale filiale;
}
