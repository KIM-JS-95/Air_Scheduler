package org.AirAPI.Entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    private int id;

    private String date;
    private String pairing;
    private String activity;

    private String from;
    private String std;

    private String to;
    private String sta;

    private String blk;
    private String duty;

}
