package org.AirAPI.entity.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Relationships {

    private String  type;
    private String[] ids;

}
