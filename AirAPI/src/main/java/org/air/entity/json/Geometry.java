package org.air.entity.json;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Geometry {
    @JsonAlias("BoundingBox")
    private BoundingBox boundingBox;
    @JsonAlias("Polygon")
    private Polygon[] polygon;
}
