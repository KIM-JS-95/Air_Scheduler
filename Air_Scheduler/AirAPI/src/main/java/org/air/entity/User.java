package org.air.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;


@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String userid;
    private String email;
    private String name;
    private String picUrl;
    private String password;

    @Transient
    private boolean enabled;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "authority_id")
    @ToString.Exclude // Exclude from toString()
    @EqualsAndHashCode.Exclude // Exclude from equals() and hashCode()
    private Authority authority;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @ToString.Exclude // Exclude from toString()
    @EqualsAndHashCode.Exclude // Exclude from equals() and hashCode()
    private Refresh refresh;

    public void setRefreshToken(Refresh refresh) {
        this.refresh = refresh;
        refresh.setUser(this); // 양방향 관계 설정
    }

    // 편의 메서드: 사용자의 리프레시 토큰 삭제
    public void removeRefreshToken() {
        if (this.refresh != null) {
            this.refresh.setUser(null); // 양방향 관계 해제
            this.refresh = null;
        }
    }

}
