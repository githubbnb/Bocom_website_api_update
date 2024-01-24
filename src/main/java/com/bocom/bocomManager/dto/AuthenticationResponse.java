package com.bocom.bocomManager.dto;

import com.bocom.bocomManager.dto.appUser.AppUserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    public String accessToken;
    public String refreshToken;
    private AppUserResponse user;
}
