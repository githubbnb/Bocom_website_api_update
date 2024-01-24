package com.bocom.bocomManager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class Credential implements Serializable {
    @NotBlank
    @NotNull
    @Email
    @Value("christiankepya@gmail.com")
    private String email;
    @NotBlank
    @NotNull
    @Size(min = 0, max = 20)
    @Value("azerty@12")
    private String password;
}
