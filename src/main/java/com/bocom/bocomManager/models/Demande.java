package com.bocom.bocomManager.models;

import com.bocom.bocomManager.enums.DemandeType;
import com.bocom.bocomManager.enums.Filiale;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Demande implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, name = "nom_du_soumetteur")
    private String nameOfApplicant;

    @Column(name = "societe_du_soumetteur")
    private String societeOfApplicant;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false,name = "telephone_du_soumetteur")
    private String telephoneOfApplicant;

    @Column(nullable = false, name = "filiale_associee")
    @Enumerated(EnumType.STRING)
    private Filiale filiale;

    @Column(nullable = false,name = "email_du_soumetteur")
    private String emailOfApplicant;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DemandeType type;

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
