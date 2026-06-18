package com.subtrack.server.service.impl;

import com.subtrack.server.dao.UserDao;
import com.subtrack.server.messaging.OtpEventPublisher;
import com.subtrack.server.messaging.dto.OtpNotificationEvent;
import com.subtrack.server.model.User;
import com.subtrack.server.service.AuthService;
import com.subtrack.server.service.dto.LoginRequest;
import com.subtrack.server.service.dto.RegisterRequest;
import com.subtrack.server.service.security.PasswordUtil;

import java.util.Date;
import java.util.Random;

public class AuthServiceImpl implements AuthService {

    private final UserDao userDao;
    private final OtpEventPublisher otpEventPublisher = new OtpEventPublisher();
    private final Random random = new Random();

    public AuthServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User register(RegisterRequest request) {
        if (request == null || isBlank(request.getUsername()) || isBlank(request.getEmail()) || isBlank(request.getPassword())) {
            throw new IllegalArgumentException("Username, email, password are required.");
        }

        String normalizedUsername = request.getUsername().trim(); // keep case policy as your choice
        User existing = userDao.findByUsername(normalizedUsername);
        if (existing != null) throw new IllegalArgumentException("Username already exists.");

        User u = new User();
        u.setUsername(normalizedUsername);
        u.setEmail(request.getEmail().trim().toLowerCase());
        u.setPassword(PasswordUtil.hash(request.getPassword()));
        u.setCreatedAt(new Date());

        Integer id = userDao.save(u);
        return userDao.findById(id);
    }

    @Override
    public String loginAndSendOtp(LoginRequest request) {
        if (request == null || isBlank(request.getUsername()) || isBlank(request.getPassword())) {
            throw new IllegalArgumentException("Username and password are required.");
        }

        String username = request.getUsername().trim();
        User u = userDao.findByUsername(username);
        if (u == null || !PasswordUtil.verify(request.getPassword(), u.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        String otp = String.format("%06d", random.nextInt(1_000_000));
        Date expiry = new Date(System.currentTimeMillis() + 3 * 60 * 1000L);

        u.setOtpCode(otp);
        u.setOtpExpiry(expiry);
        userDao.update(u);

        System.out.println("[OTP-LOGIN] user=" + u.getUsername() + ", otp=" + otp + ", expiry=" + expiry);

        try {
            otpEventPublisher.publish(
                    new OtpNotificationEvent(u.getUsername(), u.getEmail(), otp, System.currentTimeMillis())
            );
            System.out.println("[OTP-PUBLISH] OK user=" + u.getUsername());
        } catch (Exception ex) {
            // do NOT break login flow if queue is down in this stage
            System.out.println("[OTP-PUBLISH] FAIL user=" + u.getUsername() + " reason=" + ex.getMessage());
            System.out.println("[EMAIL-SIM] To=" + u.getEmail() + " OTP=" + otp);
        }

        return "OTP_REQUIRED";
    }

    @Override
    public User verifyOtp(String username, String otp) {
        if (isBlank(username) || isBlank(otp)) {
            throw new IllegalArgumentException("Username and OTP are required.");
        }

        String userKey = username.trim();
        String otpInput = otp.trim();

        User u = userDao.findByUsername(userKey);
        if (u == null) throw new IllegalArgumentException("User not found.");

        String storedOtp = u.getOtpCode() == null ? "" : u.getOtpCode().trim();
        Date expiry = u.getOtpExpiry();
        Date now = new Date();

        System.out.println("[OTP-VERIFY] user=" + userKey
                + ", provided=" + otpInput
                + ", stored=" + storedOtp
                + ", expiry=" + expiry
                + ", now=" + now);

        if (storedOtp.isEmpty() || expiry == null) {
            throw new IllegalArgumentException("OTP not requested.");
        }

        if (now.after(expiry)) {
            throw new IllegalArgumentException("OTP expired.");
        }

        if (!otpInput.equals(storedOtp)) {
            throw new IllegalArgumentException("Invalid OTP.");
        }

        u.setOtpCode(null);
        u.setOtpExpiry(null);
        userDao.update(u);

        return u;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
