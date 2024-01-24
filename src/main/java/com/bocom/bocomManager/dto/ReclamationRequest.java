package com.bocom.bocomManager.dto;

import com.bocom.bocomManager.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReclamationRequest {
    @NotNull
    @NotEmpty
    @NotBlank
    private String nameOfApplicant;

    @NotNull
    @NotEmpty
    @NotBlank
    private String subject;

    @NotNull
    @NotEmpty
    @NotBlank
    private String message;

    @NotNull
    @NotEmpty
    private String service;

    @Email
    @NotNull
    @NotEmpty
    private String emailOfApplicant;

    @NotNull
    @NotEmpty
    private Status status;
}
