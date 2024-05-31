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

    public FlightData(String departureShort, String departure, String date, String destinationShort, 
                      String destination, String flightNumber, String stal, String stab, 
                      String stdl, String stdb, String activity, Long id, String ci,
                      String co, String lat, String lon) {
        this.departureShort = departureShort;
        this.departure = departure;
        this.date = date;
        this.destinationShort = destinationShort;
        this.destination = destination;
        this.flightNumber = flightNumber;
        this.stal = stal;
        this.stab = stab;
        this.stdl = stdl;
        this.stdb = stdb;
        this.activity = activity;
        this.id = id;
        this.ci = ci;
        this.co = co;
        this.lat = lat;
        this.lon = lon;
    }

    // Getters and setters
}
