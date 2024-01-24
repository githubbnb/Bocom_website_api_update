package com.bocom.bocomManager.dto;

import com.bocom.bocomManager.enums.Filiale;
import com.bocom.bocomManager.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CandidatureRequest {
    @NotNull
    @NotEmpty
    @NotBlank
    private String nom;

    @NotNull
    @NotEmpty
    @NotBlank
    private String poste;

    @NotNull
    @NotEmpty
    @NotBlank
    private String telephone;


    private String message;

    private Status status;

    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private Filiale filiale;

    @NotNull
    @NotEmpty
    private Set<String> files = new HashSet<>();
}
