package org.air.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Slf4j
@Configuration
public class FirebaseInitializer {

    @PostConstruct
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

}
