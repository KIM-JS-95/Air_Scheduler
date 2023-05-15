
package org.AirAPI.config;

import org.AirAPI.entity.Schedule;
import org.AirAPI.entity.json.Blocks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class AWStextrack {

    @Value("${accesskey}")
    private String accesskey;

    @Value("${secretkey}")
    private String secretkey;

    public TextractClient awsceesser(){
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accesskey, secretkey);
        TextractClient textractClient = TextractClient.builder()
                .region(Region.US_WEST_2)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
        return textractClient;
    }

    public static List<Block> analyzeDoc(TextractClient textractClient, InputStream sourceDoc) {
        List<Block> docInfo=null;
        try {
            SdkBytes sourceBytes = SdkBytes.fromInputStream(sourceDoc);
            Document myDoc = Document.builder()
                    .bytes(sourceBytes)
                    .build();

            List<FeatureType> featureTypes = new ArrayList<FeatureType>();
            featureTypes.add(FeatureType.TABLES);
            featureTypes.add(FeatureType.FORMS);

            AnalyzeDocumentRequest analyzeDocumentRequest = AnalyzeDocumentRequest.builder()
                    .featureTypes(featureTypes)
                    .document(myDoc)
                    .build();
            AnalyzeDocumentResponse analyzeDocument = textractClient.analyzeDocument(analyzeDocumentRequest);
            docInfo = analyzeDocument.blocks();

        } catch (TextractException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return docInfo;
    }

    public static AnalyzeDocumentResponse analyzeDoc2(TextractClient textractClient, InputStream sourceDoc) {
        AnalyzeDocumentResponse analyzeDocument = null;
        try {
            SdkBytes sourceBytes = SdkBytes.fromInputStream(sourceDoc);
            Document myDoc = Document.builder()
                    .bytes(sourceBytes)
                    .build();
            List<FeatureType> featureTypes = new ArrayList<FeatureType>();
            featureTypes.add(FeatureType.TABLES);
            //featureTypes.add(FeatureType.FORMS);

            AnalyzeDocumentRequest analyzeDocumentRequest = AnalyzeDocumentRequest.builder()
                    .featureTypes(featureTypes)
                    .document(myDoc)
                    .build();

            analyzeDocument = textractClient.analyzeDocument(analyzeDocumentRequest);
            //docInfo = analyzeDocument.blocks().iterator();
        } catch (TextractException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return analyzeDocument;
    }
    public static void detectDocTextS3(TextractClient textractClient, String bucketName, String docName) {

        try {
            S3Object s3Object = S3Object.builder()
                    .bucket(bucketName)
                    .name(docName)
                    .build();

            // Create a Document object and reference the s3Object instance
            Document myDoc = Document.builder()
                    .s3Object(s3Object)
                    .build();

            DetectDocumentTextRequest detectDocumentTextRequest = DetectDocumentTextRequest.builder()
                    .document(myDoc)
                    .build();

            DetectDocumentTextResponse textResponse = textractClient.detectDocumentText(detectDocumentTextRequest);
            for (Block block : textResponse.blocks()) {
                System.out.println("The block type is " + block.blockType().toString());
            }

            DocumentMetadata documentMetadata = textResponse.documentMetadata();
            System.out.println("The number of pages in the document is " + documentMetadata.pages());

        } catch (TextractException e) {

            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
    public static void detectDocText(TextractClient textractClient, String sourceDoc) {

        try {
            InputStream sourceStream = new FileInputStream(new File(sourceDoc));
            SdkBytes sourceBytes = SdkBytes.fromInputStream(sourceStream);

            // Get the input Document object as bytes
            Document myDoc = Document.builder()
                    .bytes(sourceBytes)
                    .build();

            DetectDocumentTextRequest detectDocumentTextRequest = DetectDocumentTextRequest.builder()
                    .document(myDoc)
                    .build();

            // Invoke the Detect operation
            DetectDocumentTextResponse textResponse = textractClient.detectDocumentText(detectDocumentTextRequest);
            List<Block> docInfo = textResponse.blocks();
            for (Block block : docInfo) {
                System.out.println("The block type is " + block.blockType().toString());
            }

            DocumentMetadata documentMetadata = textResponse.documentMetadata();
            System.out.println("The number of pages in the document is " + documentMetadata.pages());

        } catch (TextractException | FileNotFoundException e) {

            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public void TexttoEntity(List<Block> list) {
        List<Schedule> schedules = new ArrayList<>();
        Schedule schedule = new Schedule();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).blockType().toString()=="CELL" && list.get(i).rowIndex() != 1) {
                Block block = list.get(i);
                int index = block.columnIndex();
                String chileText = block.text();
                if (index == 1) {
                    schedule.setDate(chileText);
                } else if (index == 2) {
                    schedule.setPairing(chileText);
                } else if (index == 3) {
                    schedule.setDc(chileText);
                } else if (index == 4) {
                    schedule.setCi(chileText);
                } else if (index == 5) {
                    String[] units = chileText.split(" ");
                    if (units.length == 1) {
                        schedule.setActivity(units[0]);
                    } else {
                        schedule.setCo(units[0]);
                        schedule.setActivity(units[1]);
                    }
                } else if (index == 6) {
                    schedule.setCnt_from(chileText);
                } else if (index == 7) {
                    schedule.setStd(chileText);
                } else if (index == 8) {
                    schedule.setCnt_to(chileText);
                } else if (index == 9) {
                    schedule.setSta(chileText);
                } else if (index == 10) {
                    schedule.setAchotel(chileText);
                } else if (index == 11) {
                    schedule.setBlk(chileText);
                    schedules.add(schedule);
                    schedule = new Schedule();
                }
            }
        }
        schedules.forEach((n) -> System.out.println(n));
    }
}