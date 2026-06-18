package com.subtrack.client.config;

import com.subtrack.client.util.AppConstants;
import com.subtrack.server.rmi.remote.AuthRemoteService;
import com.subtrack.server.rmi.remote.SubscriptionRemoteService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public final class RmiClientConfig {

    private RmiClientConfig() {}

    private static Registry getRegistry() throws Exception {
        return LocateRegistry.getRegistry(
                AppConstants.RMI_HOST,
                AppConstants.RMI_PORT
        );
    }

    public static AuthRemoteService getAuthRemoteService() {
        try {
            Registry registry = getRegistry();
            return (AuthRemoteService) registry.lookup(AppConstants.AUTH_SERVICE_NAME);
        } catch (Exception e) {
            throw new RuntimeException("Auth RMI connection failed: " + e.getMessage(), e);
        }
    }

    public static SubscriptionRemoteService getSubscriptionRemoteService() {
        try {
            Registry registry = getRegistry();

            String serviceName = "SubscriptionService";
            try {
                serviceName = (String) AppConstants.class
                        .getField("SUBSCRIPTION_SERVICE_NAME")
                        .get(null);
            } catch (Exception ignored) {
                // fallback if constant not yet created
            }

            return (SubscriptionRemoteService) registry.lookup(serviceName);
        } catch (Exception e) {
            throw new RuntimeException("Subscription RMI connection failed: " + e.getMessage(), e);
        }
    }
}
