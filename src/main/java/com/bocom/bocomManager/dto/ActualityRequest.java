package com.bocom.bocomManager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActualityRequest {
    private String title;
    private String description;
    private Set<String> images = new HashSet<>();
    private Set<String> videos = new HashSet<>();
}
