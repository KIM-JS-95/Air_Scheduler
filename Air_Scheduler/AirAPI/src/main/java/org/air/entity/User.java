package org.air.entity;


import com.google.firebase.database.annotations.NotNull;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;



@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
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

    private String family; // 기장은 없어도 됨

    private int schedule_chk;

    @Transient
    private boolean enabled;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "authority_id")
    private Authority authority;

    @OneToOne(orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "refresh_id")
    private Refresh refresh;

}
