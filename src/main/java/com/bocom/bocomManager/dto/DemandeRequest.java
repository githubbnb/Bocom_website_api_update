package com.bocom.bocomManager.dto;

import com.bocom.bocomManager.enums.DemandeType;
import com.bocom.bocomManager.enums.Filiale;
import com.bocom.bocomManager.enums.Status;
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
public class DemandeRequest {
    @NotNull
    @NotEmpty
    @NotBlank
    private String nameOfApplicant;

    private String societeOfApplicant;

    @NotNull
    @NotEmpty
    @NotBlank
    private String telephoneOfApplicant;

    @NotNull
    @NotEmpty
    @NotBlank
    private String title;

    @NotNull
    @NotEmpty
    @NotBlank
    private String description;


    private Filiale filiale;

    private Status status;

    @Email
    @NotNull
    @NotEmpty
    private String emailOfApplicant;

    @NotNull
    @NotEmpty
    private DemandeType type;
}
