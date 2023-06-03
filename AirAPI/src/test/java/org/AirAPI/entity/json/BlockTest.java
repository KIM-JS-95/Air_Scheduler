package org.AirAPI.entity.json;

import org.AirAPI.entity.Schedule;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Jsonschedules.class)
class BlockTest {
    @Autowired
    private Jsonschedules jsonschedules;
    @Test
    public void setEntity() throws IOException, ParseException {
        HashMap<String, String> map = new HashMap<>();
        List<Blocks> blocks = jsonschedules.readJsonFile();
        blocks.forEach(callback -> {
            if (callback.getBlockType().equals("WORD")) {
                map.put(callback.getId(), callback.getText());
            }
        });
        List<Schedule> scheduleList = jsonschedules.getschedules(map, blocks);
        assertThat(scheduleList.get(0).getCnt_from(), is("ICN"));
    }
}