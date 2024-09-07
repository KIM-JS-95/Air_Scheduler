package org.air.entity;


import com.google.firebase.database.annotations.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;


@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class User {

    @Id
    @NotNull
    @Size(max=20, message = "유저아이디는 20자리를 넘을 수 없습니다.")
    private String userid;

    private String password;

    @NotNull
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    @Column(length = 100)
    private String email;

    @Size(max = 10, message = "이름은 10자를 넘을 수 없습니다.")
    private String name;

    private String device_token; // 파이어베이스 토큰

    private String family;

    private int schedule_chk;

    @ColumnDefault("0")
    private int autologin;

    @Size(max = 20, message = "안드로이드 ID는 20자리를 넘을 수 없습니다.")
    private String androidid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "authority_id")
    private Authority authority;

    @OneToOne(orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "refresh_id")
    private Refresh refresh;

}
