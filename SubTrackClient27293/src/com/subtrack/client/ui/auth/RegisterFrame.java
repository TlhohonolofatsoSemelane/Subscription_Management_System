package com.subtrack.client.ui.auth;

import com.subtrack.client.config.RmiClientConfig;
import com.subtrack.client.dto.RegisterRequest;
import com.subtrack.client.service.AuthClientService;
import com.subtrack.client.service.impl.AuthClientServiceImpl;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private final JTextField txtUsername = new JTextField(20);
    private final JTextField txtEmail = new JTextField(20);
    private final JPasswordField txtPassword = new JPasswordField(20);

    private final JButton btnRegister = new JButton("Create Account");
    private final JButton btnBackLogin = new JButton("Back to Login");

    private final AuthClientService authClientService;

    public RegisterFrame() {
        this.authClientService = new AuthClientServiceImpl(RmiClientConfig.getAuthRemoteService());
        initUI();
        initActions();
    }

    private void initUI() {
        setTitle("SubTrack - Register");
        setSize(460, 280);
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
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnBackLogin);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        add(panel);
    }

    private void initActions() {
        btnRegister.addActionListener(e -> doRegister());

        btnBackLogin.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }

    private void doRegister() {
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        String response = authClientService.register(new RegisterRequest(username, email, password));

        if (response != null && response.startsWith("REGISTER_OK")) {
            JOptionPane.showMessageDialog(this, "Registration successful. Please login.");
            new LoginFrame().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed: " + response);
        }
    }
}
