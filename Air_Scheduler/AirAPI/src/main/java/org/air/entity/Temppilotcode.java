package org.air.entity;

import com.google.firebase.database.annotations.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Temppilotcode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max=20, message = "유저 아이디는 20자리를 넘을 수 없습니다.")
    private String userid;

    private String password;

    @Column(length = 100)
    private String email;

    @Size(max = 10)
    private String username;

    @Size(max=10)
    private String randomkey;

    @Size(max=20, message = "유저 아이디는 20자리를 넘을 수 없습니다.")
    private String pilotid;

    @Column(length = 20)
    private String androidid;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;

}
