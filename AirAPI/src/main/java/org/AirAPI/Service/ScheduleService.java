package org.AirAPI.Service;

import org.AirAPI.Entity.Schedule;
import org.AirAPI.Repository.SchduleRepository;
import org.AirAPI.config.AWStextrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.Block;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private AWStextrack awstextrack;

    @Autowired
    private SchduleRepository schduleRepository;

    public Schedule findData(int id) {
        return schduleRepository.findById(id).orElseThrow();
    }


    // s3 저장 기능 추가 DB 스케줄 기능 추가 (AWS S3 + Textrack)
    public String save(InputStream source) {

        // 파일 추출 후 DB 저장
        TextractClient textractClient= awstextrack.awsceesser();
        Iterator<Block> blockIterator= awstextrack.analyzeDoc(textractClient, source);

        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();

            // 추출한 파일 엔티티에 저장
            //System.out.println("The block type is " + block.text());
        }

        // 이미지 S3에 저장

        return "OK";
    }


}
