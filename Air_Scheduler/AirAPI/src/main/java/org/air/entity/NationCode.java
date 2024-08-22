package org.air.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NationCode {

    @Id
    @Size(max = 30)
    private String code;
    @Size(max = 3)
    private String country;

    @Size(max = 10)
    private String lat;
    @Size(max = 10)
    private String lon;
}
