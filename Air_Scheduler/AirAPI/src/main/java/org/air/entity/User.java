package org.air.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.firebase.database.annotations.NotNull;
import lombok.*;

import javax.persistence.*;


@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String pilotcode;

    @NotNull
    private String userid;

    @NotNull
    private String password;

    private String email;

    private String name;
    private String picUrl;

    private String device_token;

    private String family;

    @Transient
    private boolean enabled;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "authority_id")
    private Authority authority;

    @OneToOne(orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "refresh_id")
    private Refresh refresh;

}
