package org.air.service;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.*;
import org.air.entity.Authority;
import org.air.entity.Messege;
import org.air.entity.User;
import org.air.repository.AuthorityRepository;
import org.air.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FcmServiceImpl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private GoogleCredentials googleCredentials;

    private String getAccessToken() throws IOException {
        googleCredentials.refreshIfExpired();
        AccessToken accessToken = googleCredentials.getAccessToken();
        if (accessToken == null) {
            throw new IOException("Failed to obtain access token.");
        }
        return accessToken.getTokenValue();
    }

    public int sendMessageTo(String date, String cntto, User user) throws FirebaseMessagingException {
        Logger logger = LoggerFactory.getLogger(getClass());

        List<User> users = userRepository.findByFamily(user.getUserid());

        String title = "🛩️ 비행 일정이 변경되었어요! 🛩️";
        String body = "- 날짜: " + date + "\n- 목적지: " + cntto;

        // Collect all device tokens from users
        List<String> deviceTokens = users.stream()
                .map(User::getDevice_token)
                .collect(Collectors.toList());
        deviceTokens.add(user.getDevice_token());

        if (!deviceTokens.isEmpty()) {
            // Create the multicast message
            MulticastMessage message = MulticastMessage.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .addAllTokens(deviceTokens)
                    .build();

            // Send the multicast message
            FirebaseMessaging.getInstance().sendMulticast(message);
        }
        // Log the results and count the successful sends
        int successCount = 0;
        for (String token : deviceTokens) {
            try {
                logger.info("Message sent to token: " + token);
                successCount++;
            } catch (Exception e) {
                logger.error("Failed to send message to token: " + token, e);
            }
        }

        return successCount;
    }


    // 일정 저장 성공시 처리
    public boolean completeSave_Schedule(String userid) {
        User user = userRepository.findByUserid(userid);

        String title = "🛩️ 비행 일정이 등록되었어요! 🛩️";
        String body = "";
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .setToken(user.getDevice_token())
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            return false;
        }

        return true;
    }

    public boolean sendMessageAll(Messege message) throws FirebaseMessagingException {
        List<User> users = userRepository.findAll();


        List<String> deviceTokens = users.stream()
                .map(User::getDevice_token)
                .collect(Collectors.toList());

        if (!deviceTokens.isEmpty()) {
            MulticastMessage fcm = MulticastMessage.builder()
                    .setNotification(Notification.builder()
                            .setTitle(message.getTitle())
                            .setBody(message.getMessage())
                            .build())
                    .addAllTokens(deviceTokens)
                    .build();

            FirebaseMessaging.getInstance().sendMulticast(fcm);
            return true;
        }

        return false;

    }

    public boolean request_schedule_save() throws FirebaseMessagingException {

        String title = "🛩️ 일정을 아직 등록하지 않으셨나요? 🛩️";
        String body=" 일정을 등록하고 다음달 일정을 확인해 보세요.";

        Authority authority = Authority.builder()
                .id(1L)
                .authority("USER")
                .build();

        List<User> users = userRepository.findByAuthority(authority);
        List<String> deviceTokens = users.stream()
                .map(User::getDevice_token)
                .collect(Collectors.toList());

        if (!deviceTokens.isEmpty()) {
            MulticastMessage fcm = MulticastMessage.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .addAllTokens(deviceTokens)
                    .build();

            FirebaseMessaging.getInstance().sendMulticast(fcm);
            return true;
        }

        return false;
    }
}
