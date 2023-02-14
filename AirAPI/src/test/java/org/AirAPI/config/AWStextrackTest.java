package org.AirAPI.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.Block;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class AWStextrackTest {
    private final Logger LOGGER = LoggerFactory.getLogger(AWStextrackTest.class);
    @Autowired
    public AWStextrack awStextrack;

    @Test
    public void docTest() throws IOException {
        //String filePath = "C:\\Users\\JAESEUNG\\IdeaProjects\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\sample.jpg";
        String filePath = "D:\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\sample.jpg";
        FileInputStream fileInputStream = new FileInputStream(filePath);
        TextractClient textractClient = awStextrack.awsceesser();
        Iterator<Block> blockIterator = AWStextrack.analyzeDoc(textractClient, fileInputStream);
        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            float left = block.geometry().boundingBox().left();
            float top = block.geometry().boundingBox().top();

            if (block.blockType().toString() == "WORD" && top > 0.05 && left < 0.7) {
                //LOGGER.info(block.text() + " , " + block.geometry().boundingBox().left() + "/" + block.geometry().boundingBox().top());

                if (left <= 0.1) {
                    LOGGER.info("1 " + block.text());
                } else if (0.1 < left && left <= 0.18) {
                    LOGGER.info("2 " + block.text());
                } else if (0.18 < left && left <= 0.24) {
                    LOGGER.info("3 " + block.text());
                } else if (0.24 < left && left <= 0.3) {
                    LOGGER.info("4 " + block.text());
                } else if (0.3 < left && left <= 0.37) {
                    LOGGER.info("5 " + block.text());
                } else if (0.37 < left && left <= 0.46) {
                    LOGGER.info("6 " + block.text());
                } else if (0.46 < left && left <= 0.52) {
                    LOGGER.info("7 " + block.text());
                } else if (0.52 < left && left <= 0.58) {
                    LOGGER.info("8 " + block.text());
                } else if (0.58 < left && left <= 0.65) {
                    LOGGER.info("9 " + block.text());
                } else if (0.65 < left && left <= 0.7) {
                    LOGGER.info("10 " + block.text());
                }else{
                    LOGGER.info("null");
                }

            }
        }

    }
}

/*
date <0.1

*/