package org.air.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Size;

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

    @Size(max = 7)
    private String date;
    @Size(max = 10)
    private String pairing;

    @Size(max = 5)
    private String dc;

    @Size(max = 4)
    private String ci;

    @Size(max = 4)
    private String co;

    @Size(max = 20)
    private String activity;

    @Size(max = 4)
    private String stdl;

    @Size(max = 4)
    private String stdb;

    @Size(max = 3)
    private String cntfrom;
    @Size(max = 3)
    private String cntto;

    @Size(max = 4)
    private String stal;
    @Size(max = 4)
    private String stab;

    @Size(max = 20)
    private String achotel;
    @Size(max = 20)
    private String blk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_userid") // 외래 키 컬럼명
    private User user;
}
