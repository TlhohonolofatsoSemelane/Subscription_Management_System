package com.subtrack.client.ui.auth;

import com.subtrack.client.config.RmiClientConfig;
import com.subtrack.client.dto.LoginRequest;
import com.subtrack.client.service.AuthClientService;
import com.subtrack.client.service.impl.AuthClientServiceImpl;
import com.subtrack.client.ui.dashboard.DashboardFrame;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final JTextField txtUsername = new JTextField(20);
    private final JPasswordField txtPassword = new JPasswordField(20);
    private final JButton btnLogin = new JButton("Login");
    private final JButton btnGoRegister = new JButton("Register");

    private AuthClientService authClientService;
    private volatile boolean loginInProgress = false;

    public LoginFrame() {
        initServices();
        initUI();
        initActions();
    }

    private void initServices() {
        try {
            this.authClientService = new AuthClientServiceImpl(RmiClientConfig.getAuthRemoteService());
        } catch (Exception ex) {
            this.authClientService = null;
            JOptionPane.showMessageDialog(
                    this,
                    "Cannot connect to server.\nPlease start server first.\n\n" + ex.getMessage(),
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void initUI() {
        setTitle("SubTrack - Login");
        setSize(420, 240);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnGoRegister);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        add(panel);
        getRootPane().setDefaultButton(btnLogin);
    }

    private void initActions() {
        btnLogin.addActionListener(e -> doLogin());

        btnGoRegister.addActionListener(e -> {
            if (loginInProgress) return;
            new RegisterFrame().setVisible(true);
            dispose();
        });
    }

    private void setUiEnabled(boolean enabled) {
        btnLogin.setEnabled(enabled);
        btnGoRegister.setEnabled(enabled);
        txtUsername.setEnabled(enabled);
        txtPassword.setEnabled(enabled);
    }

    private void doLogin() {
        if (loginInProgress) return;

        if (authClientService == null) {
            JOptionPane.showMessageDialog(this, "Server is not connected.");
            return;
        }

        final String usernameInput = txtUsername.getText().trim();
        final String password = new String(txtPassword.getPassword()).trim();

        if (usernameInput.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password are required.");
            return;
        }

        loginInProgress = true;
        setUiEnabled(false);

        try {
            System.out.println("[CLIENT] Calling login() for username=" + usernameInput);
            String response = authClientService.login(new LoginRequest(usernameInput, password));
            System.out.println("[CLIENT] LOGIN STEP1 RESPONSE => " + response);

            if (!"OTP_REQUIRED".equals(response)) {
                JOptionPane.showMessageDialog(this, "Login failed: " + response);
                return;
            }

            // OTP input retry loop (no re-login, no OTP regeneration)
            while (true) {
                JPanel otpPanel = new JPanel(new GridLayout(2, 1, 5, 5));
                JTextField otpField = new JTextField();
                otpPanel.add(new JLabel("Enter the 6-digit OTP sent to your email:"));
                otpPanel.add(otpField);

                int choice = JOptionPane.showConfirmDialog(
                        this,
                        otpPanel,
                        "OTP Verification",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if (choice != JOptionPane.OK_OPTION) {
                    JOptionPane.showMessageDialog(this, "OTP verification cancelled.");
                    return;
                }

                String otp = otpField.getText() == null ? "" : otpField.getText().trim();
                if (otp.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "OTP is required.");
                    continue;
                }

                System.out.println("[CLIENT] Calling verifyOtp() user=" + usernameInput + ", otp=" + otp);
                String otpResponse = authClientService.verifyOtp(usernameInput, otp);
                System.out.println("[CLIENT] LOGIN STEP2 RESPONSE => " + otpResponse);

                if (otpResponse != null && otpResponse.startsWith("LOGIN_OK:")) {
                    String[] parts = otpResponse.split(":", 3);
                    if (parts.length < 3) {
                        JOptionPane.showMessageDialog(this, "Invalid login response format: " + otpResponse);
                        return;
                    }

                    Integer userId;
                    try {
                        userId = Integer.parseInt(parts[1].trim());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Invalid userId in response: " + otpResponse);
                        return;
                    }

                    String username = parts[2].trim();
                    if (username.isEmpty()) username = usernameInput;

                    JOptionPane.showMessageDialog(this, "Login successful!");
                    new DashboardFrame(userId, username).setVisible(true);
                    dispose();
                    return;
                }

                JOptionPane.showMessageDialog(this, "OTP verification failed: " + otpResponse);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Login error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } finally {
            if (isDisplayable()) {
                loginInProgress = false;
                setUiEnabled(true);
            }
        }
    }
}
