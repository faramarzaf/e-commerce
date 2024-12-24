package org.example.ecommerce.service;


import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 3; // Max allowed attempts
    private static final long LOCK_TIME_MILLIS = TimeUnit.MINUTES.toMillis(1); // Lock duration

    private final Map<String, LoginAttempt> attempts = new ConcurrentHashMap<>();

    public void loginSucceeded(String username) {
        attempts.remove(username);
    }

    public void loginFailed(String username) {
        attempts.compute(username, (key, attempt) -> {
            if (attempt == null) {
                attempt = new LoginAttempt(1, System.currentTimeMillis());
            } else {
                attempt.incrementAttempts();
                attempt.updateTimestamp();
            }
            return attempt;
        });
    }

    public boolean isBlocked(String username) {
        LoginAttempt attempt = attempts.get(username);
        if (attempt == null) {
            return false;
        }
        if (attempt.getAttempts() >= MAX_ATTEMPTS &&
                System.currentTimeMillis() - attempt.getLastAttemptTimestamp() < LOCK_TIME_MILLIS) {
            return true;
        }
        if (System.currentTimeMillis() - attempt.getLastAttemptTimestamp() >= LOCK_TIME_MILLIS) {
            attempts.remove(username); // Reset after lock duration
        }
        return false;
    }

    private static class LoginAttempt {
        private int attempts;
        private long lastAttemptTimestamp;

        public LoginAttempt(int attempts, long lastAttemptTimestamp) {
            this.attempts = attempts;
            this.lastAttemptTimestamp = lastAttemptTimestamp;
        }

        public int getAttempts() {
            return attempts;
        }

        public void incrementAttempts() {
            this.attempts++;
        }

        public long getLastAttemptTimestamp() {
            return lastAttemptTimestamp;
        }

        public void updateTimestamp() {
            this.lastAttemptTimestamp = System.currentTimeMillis();
        }
    }

}
