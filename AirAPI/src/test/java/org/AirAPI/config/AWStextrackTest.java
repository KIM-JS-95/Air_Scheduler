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
        String filePath = "C:\\Users\\JAESEUNG\\IdeaProjects\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\sample.jpg";
        FileInputStream fileInputStream = new FileInputStream(filePath);
        TextractClient textractClient = awStextrack.awsceesser();
        Iterator<Block> blockIterator = AWStextrack.analyzeDoc(textractClient, fileInputStream);
        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            if (block.blockType().toString() == "LINE") {
                // 객체에 리스트로 넣어주고 저장 시키기
                LOGGER.info(block.geometry().boundingBox().top().toString());
                LOGGER.info(block.text());
            }
        }
    }
}


/*

Block(
    BlockType=LINE,
    Confidence=99.84063,
    Text=Date,
    Geometry=Geometry(
            BoundingBox=BoundingBox(Width=0.03465374, Height=0.011191052, Left=0.01116037, Top=0.033650413),
            Polygon=[
                Point(X=0.01116037, Y=0.033671685),
                Point(X=0.04581153, Y=0.033650413),
                Point(X=0.045814108, Y=0.044820268),
                Point(X=0.011162853, Y=0.04484147)
                ]
     ),
    Id=14e5b094-716b-4abb-9667-52135ace8ca2,
    Relationships=[
    Relationship(
        Type=CHILD,
        Ids=[7e453b43-b3f4-4e2e-97de-69798dce14f2])
    ],
    EntityTypes=[]
)

* */