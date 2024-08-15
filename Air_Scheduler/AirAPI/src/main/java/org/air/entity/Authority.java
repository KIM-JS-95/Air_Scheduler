package org.air.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    // USER(1) / FAMILY(2) / ADMIN(3)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String authority;
}
