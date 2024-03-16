package org.air.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String date;
    private String pairing;

    private String dc;
    private String ci;
    private String co;

    private String activity;

    private String cntFrom;
    private String stdL;
    private String stdB;

    private String cntTo;
    private String staL;
    private String staB;

    private String achotel;

    private String blk;

}
