package com.subtrack.client.ui.dashboard;

import com.subtrack.client.ui.subscription.SubscriptionFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private final Integer currentUserId;
    private final String currentUsername;

    public DashboardFrame(Integer currentUserId, String currentUsername) {
        this.currentUserId = currentUserId;
        this.currentUsername = currentUsername;

        setTitle("SubTrack Dashboard");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JLabel lblWelcome = new JLabel(
                "Welcome, " + currentUsername + " (ID: " + currentUserId + ")",
                SwingConstants.CENTER
        );
        lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 16));

        JButton btnSubscriptions = new JButton("Manage Subscriptions");
        JButton btnLogout = new JButton("Logout");

        btnSubscriptions.addActionListener(e -> {
            new SubscriptionFrame(currentUserId, currentUsername).setVisible(true);
            dispose();
        });

        btnLogout.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logged out successfully.");
            new com.subtrack.client.ui.auth.LoginFrame().setVisible(true);
            dispose();
        });

        JPanel center = new JPanel(new GridLayout(2, 1, 10, 10));
        center.setBorder(new EmptyBorder(20, 40, 20, 40));
        center.add(btnSubscriptions);
        center.add(btnLogout);

        setLayout(new BorderLayout(10, 10));
        add(lblWelcome, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
    }
}
