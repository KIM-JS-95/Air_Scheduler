package org.air.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightData {
    private Long id;
    private String departureShort;
    private String departure;
    private String date;
    private String destinationShort;
    private String destination;
    private String flightNumber;
    private String stal;
    private String stab;
    private String stdl;
    private String stdb;
    private String activity;
    private String ci;
    private String co;
    private String lat;
    private String lon;
}
