package org.AirAPI.entity.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
//@JsonIgnoreProperties
public class Geometry {

  private BoundingBox boundingBox;
  private Polygon[] polygon;
}
