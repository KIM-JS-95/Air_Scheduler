package org.air.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDTO {
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
