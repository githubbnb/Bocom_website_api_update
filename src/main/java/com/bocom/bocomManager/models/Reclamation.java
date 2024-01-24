package com.bocom.bocomManager.models;

import com.bocom.bocomManager.enums.Status;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reclamation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, name = "nom_du_soumetteur")
    private String nameOfApplicant;

    @Column(nullable = false, name = "sujet")
    private String subject;

    @Column(nullable = false)
    private String service;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false,name = "email_du_soumetteur")
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
