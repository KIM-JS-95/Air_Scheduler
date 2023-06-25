package org.air.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue
    private int id;

    private String date;
    private String pairing;

    private String dc;
    private String ci;
    private String co;

    private String activity;

    private String cnt_from;
    private String std;

    private String cnt_to;
    private String sta;

    private String achotel;

    private String blk;

}
