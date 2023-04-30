package org.AirAPI.entity.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class Blocks {

    private String blockType;
    private Long confidence;
    private String text;
    private Geometry geometry;
    private String textType;
    private String id;
    private int page;
    private String searchKey;
    private String childText;
    private Relationships[] relationships;
}
