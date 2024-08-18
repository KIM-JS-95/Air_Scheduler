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
    private String stdl;
    private String stdb;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="nationcode_cntFrom")
    private NationCode cntFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="nationcode_cntTo")
    private NationCode cntTo;

    private String stal;
    private String stab;

    private String achotel;

    private String blk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_userid") // 외래 키 컬럼명
    private User user;
}
