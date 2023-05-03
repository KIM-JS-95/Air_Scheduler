package org.AirAPI.entity.json;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class Blocks {
    @JsonAlias("BlockType")
    private String blockType;
    /*
    private Long confidence;
    private Geometry geometry;
    private String textType;
    private String id;
    private int page;
    private String searchKey;
    private String childText;
    private Relationships[] relationships;
*/
}
