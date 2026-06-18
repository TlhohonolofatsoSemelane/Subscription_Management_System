package com.subtrack.client;

import com.subtrack.client.ui.auth.LoginFrame;

import javax.swing.*;

public class ClientMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
