package org.AirAPI.entity.json;


import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Polygon {
    @JsonAlias("X")
    private Float x;
    @JsonAlias("Y")
    private Float y;
}
