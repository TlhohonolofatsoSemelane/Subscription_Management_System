package com.subtrack.server.rmi;

import com.subtrack.server.messaging.OtpNotificationConsumer;
import com.subtrack.server.rmi.impl.AuthRemoteServiceImpl;
import com.subtrack.server.rmi.impl.SubscriptionRemoteServiceImpl;
import com.subtrack.server.util.AppConstants;
import com.subtrack.server.util.HibernateUtil;
import org.hibernate.Session;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {

    private static void printDbIdentity() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Object db = session.createSQLQuery("select current_database()").uniqueResult();
            Object usr = session.createSQLQuery("select current_user").uniqueResult();
            System.out.println("HIBERNATE DB => " + db + " | USER => " + usr);
        } catch (Exception e) {
            System.out.println("Could not read DB identity: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) {
                try { session.close(); } catch (Exception ignored) {}
            }
        }
    }

    private static void startOtpConsumer() {
        try {
            new OtpNotificationConsumer().start();
            System.out.println("OTP Notification Consumer started (queue: otp.queue)");
        } catch (Exception e) {
            System.err.println("Failed to start OTP Notification Consumer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("=== SubTrack RMI Server Starting ===");

            
            printDbIdentity();

            
            startOtpConsumer();

            Registry registry;
            try {
                registry = LocateRegistry.createRegistry(AppConstants.RMI_PORT);
                System.out.println("RMI Registry created on port " + AppConstants.RMI_PORT);
            } catch (Exception e) {
                registry = LocateRegistry.getRegistry(AppConstants.RMI_PORT);
                System.out.println("Using existing RMI Registry on port " + AppConstants.RMI_PORT);
            }

            registry.rebind(AppConstants.AUTH_SERVICE_NAME, new AuthRemoteServiceImpl());
            System.out.println("Auth service bound as: " + AppConstants.AUTH_SERVICE_NAME);

            registry.rebind(AppConstants.SUBSCRIPTION_SERVICE_NAME, new SubscriptionRemoteServiceImpl());
            System.out.println("Subscription service bound as: " + AppConstants.SUBSCRIPTION_SERVICE_NAME);

            System.out.println("=== SubTrack RMI Server Ready ===");

        } catch (Exception e) {
            System.err.println("Server failed to start: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
