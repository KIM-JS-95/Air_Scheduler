package org.AirAPI.service;

import org.AirAPI.config.AWStextrack;
import org.AirAPI.entity.Schedule;
import org.AirAPI.repository.SchduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.Block;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

@Service
public class ScheduleService {
    @Autowired
    private AWStextrack awstextrack;

    //@Autowired
    private SchduleRepository schduleRepository;

    public Schedule findData(int id) {
        return schduleRepository.findById(id).orElseThrow();
    }

    public String save(InputStream source) {
        TextractClient textractClient= awstextrack.awsceesser();
        List<Block> blockIterator= awstextrack.analyzeDoc(textractClient, source);

        System.out.println( "사이즈!!!!!!"+ blockIterator.size());

        return "OK";
    }


}
