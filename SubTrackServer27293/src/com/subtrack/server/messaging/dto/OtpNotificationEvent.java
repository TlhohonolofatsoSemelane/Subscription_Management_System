package com.subtrack.server.messaging.dto;

import java.io.Serializable;

public class OtpNotificationEvent implements Serializable {
    private String username;
    private String email;
    private String otp;
    private long createdAt;

    public OtpNotificationEvent() {}

    public OtpNotificationEvent(String username, String email, String otp, long createdAt) {
        this.username = username;
        this.email = email;
        this.otp = otp;
        this.createdAt = createdAt;
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getOtp() { return otp; }
    public long getCreatedAt() { return createdAt; }

    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setOtp(String otp) { this.otp = otp; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
