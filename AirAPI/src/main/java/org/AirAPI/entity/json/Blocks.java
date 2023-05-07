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
    private String id;
    @JsonAlias("BlockType")
    private String blockType;
    @JsonAlias("Confidence")
    private Long confidence;
    @JsonAlias("RowIndex")
    private int rowIndex;
    @JsonAlias("RowSpan")
    private int rowSpan;

    @JsonAlias("ColumnSpan")
    private int columnSpan;

    @JsonAlias("ColumnIndex")
    private int columnIndex;

    @JsonAlias("Geometry")
    private Geometry geometry;

    @JsonAlias("EntityTypes")
    private String[] entityTypes;


    @JsonAlias("Page")
    private int page;

    @JsonAlias("SearchKey")
    private String searchKey;

    private String childText;

    @JsonAlias("Relationships")
    private Relationships[] relationships;


}
