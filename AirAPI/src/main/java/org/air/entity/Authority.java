package org.air.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String authority;

    @OneToMany(mappedBy = "authority", fetch = FetchType.LAZY)
    @ToString.Exclude // Exclude from toString()
    @EqualsAndHashCode.Exclude // Exclude from equals() and hashCode()
    private List<User> users;

}
