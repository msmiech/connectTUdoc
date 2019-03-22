package at.connectTUdoc.backend.service.impl;

import at.connectTUdoc.backend.exception.UserNotAuthenticatedException;
import at.connectTUdoc.backend.service.FirebaseService;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseServiceImpl implements FirebaseService {

    @Override
    public String getUserIdFromIdToken(String idToken) throws UserNotAuthenticatedException {
        String userId;

        try {
            userId = FirebaseAuth.getInstance().verifyIdTokenAsync(idToken).get().getUid();
        } catch (InterruptedException | ExecutionException e) {
            throw new UserNotAuthenticatedException("User nicht angemeldet");
        }

        return userId;
    }

    @Override
    public UserRecord getUserByUid(String uid) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().getUser(uid);
    }

    @Override
    public String sendNotification(String notificationTitle, String notificationBody, String data, String topic) throws FirebaseException {

    /* Push Notification not needed for android
    Message message = Message.builder()
        .setNotification(new Notification(notificationTitle, notificationBody))
        .setAndroidConfig(AndroidConfig.builder()
            .setPriority(AndroidConfig.Priority.NORMAL)
            .setNotification(AndroidNotification.builder()
                .setTitle(notificationTitle)
                .setBody(notificationBody)
                .setSound("default")
                .build())
            .build())
        .setTopic(topic)
        .build();*/
        Message message = Message.builder()
            .putData("notificationTitle", notificationTitle)
            .putData("notificationBody", notificationBody)
            .putData("data", data)
            .setTopic(topic)
            .build();

        return FirebaseMessaging.getInstance().send(message);
    }

    @Override
    public void subscribeToTopic(String registrationToken, String topic) throws FirebaseException {
        FirebaseMessaging.getInstance().subscribeToTopic(List.of(registrationToken), topic);
    }
}
