package org.air.entity.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoundingBox {
    private Long width;
    private Long height;
    private Long left;
    private Long top;
}
