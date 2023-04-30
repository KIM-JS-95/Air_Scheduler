package org.AirAPI.entity.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AWS_Block {
    private DocumentMetadata DocumentMetadata;
    private Blocks[] Blocks;
}
