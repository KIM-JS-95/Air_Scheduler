package org.AirAPI.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Table
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    private String username;
    private String token;

}
