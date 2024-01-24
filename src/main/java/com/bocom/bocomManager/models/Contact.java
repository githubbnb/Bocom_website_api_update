package com.bocom.bocomManager.models;

import com.bocom.bocomManager.enums.Filiale;
import com.bocom.bocomManager.enums.Status;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Contact implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String nameOfApplicant;

    private String pays;

    @Column(nullable = false)
    private String telephoneOfApplicant;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Filiale filiale;

    @Column(nullable = false, unique = true)
    @Email
    private String emailOfApplicant;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

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
