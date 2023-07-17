package api;

import models.ServiceConfiguration;
import request.ReadConfigRequest;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class RateLimiterServer {

    String ipAddress;

    public CompletableFuture<ServiceConfiguration> getServiceConfig(String serviceName) {
        new ReadConfigRequest(serviceName, UUID.randomUUID().toString());
        // readServiceConfig (ReadConfigRequest)
        //send message to ipAddress
        return null;
    }

    public CompletableFuture<Map<String, Limit>> readKey() {
        return null;
    }

    public CompletableFuture<Void> updateKey(String key, int amount, long currentTimeMillis) {
        return null;
    }
}
