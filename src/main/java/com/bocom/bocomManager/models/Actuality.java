package com.bocom.bocomManager.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Actuality implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    @Lob
    private String description;

    @ElementCollection
    @CollectionTable(name = "actuality_images", joinColumns = @JoinColumn(name = "actuality_id"))
    @Column(name = "image")
    private Set<String> images = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "actuality_videos", joinColumns = @JoinColumn(name = "actuality_id"))
    @Column(name = "video")
    private Set<String> videos = new HashSet<>();

    @CreationTimestamp
    private Date dateMiseEnLigne;

    @Value("false")
    private boolean archived;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private  String updatedBy;
}
