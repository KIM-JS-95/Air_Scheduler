package org.air.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.air.entity.DTO.FcmMessageDto;
import org.air.entity.DTO.FcmSendDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class FcmServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private GoogleCredentials googleCredentials;

    @InjectMocks
    private FcmServiceImpl fcmService;



    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        //initializeFirebaseApp();
    }

    private void initializeFirebaseApp() throws IOException {
        String firebaseConfigPath = "firebase/auth.json";

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                        .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform")))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/auth.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        AccessToken accessToken = googleCredentials.getAccessToken();
        if (accessToken == null) {
            throw new IOException("Failed to obtain access token.");
        }
        return accessToken.getTokenValue();
    }

    private String makeMessage(FcmSendDto fcmSendDto) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        FcmMessageDto fcmMessageDto = FcmMessageDto.builder()
                .message(FcmMessageDto.Message.builder()
                        .token(fcmSendDto.getToken())
                        .notification(FcmMessageDto.Notification.builder()
                                .title(fcmSendDto.getTitle())
                                .body(fcmSendDto.getBody())
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return om.writeValueAsString(fcmMessageDto);
    }

    public int sendMessageTo(FcmSendDto fcmSendDto, String getAccessToken) throws IOException {
        String message = makeMessage(fcmSendDto);
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getAccessToken);

        HttpEntity entity = new HttpEntity<>(message, headers);

        String API_URL = "https://fcm.googleapis.com/v1/projects/schedulenotification-f09d4/messages:send";
        ResponseEntity response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);

        System.out.println(response.getStatusCode());

        return response.getStatusCode() == HttpStatus.OK ? 1 : 0;
    }

    @Test
    public void testSendMessageTo() throws IOException {
        String accessToken = getAccessToken();
        assertNotNull(accessToken, "Access token should not be null");

        // Mock the behavior of RestTemplate
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>("Success", HttpStatus.OK));

        String token = "cGgucWbUQnG4u7kK4LKcPU:APA91bHao5UkYZRbzuMcuFDEMG1JCFKNAU0AztJd9QdxiRjR9VOtNSO9Hu0lOiUABZ_h4bTHgFPm8cCUcLzxo-CSXRIhkgzreP_kuBthIA9IytIC8ctEoPyflfrpu1wcT_yHxg6batxE";
        String title = "Test Title";
        String body = "Test Body";

        FcmSendDto fcmSendDto = FcmSendDto.builder()
                .token(token)
                .title(title)
                .body(body)
                .build();

        sendMessageTo(fcmSendDto, accessToken);

    }

}
