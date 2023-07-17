package api;

import algorithm.Algorithm;
import algorithm.SlidingWindow;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class RateLimiter<RESOURCE> {
    private RateLimiterServer rateLimiterServer;

    private String serviceName;

    private Function<RESOURCE, CompletableFuture<Void>> onAccept;
    private Function<RESOURCE, CompletableFuture<Void>> onReject;

    private ExecutorService executorService;

    private Algorithm algorithm;

    public RateLimiter(String serviceName, int threadCount) throws Exception {
        this(serviceName, null,null, null, threadCount);
    }

    public RateLimiter(String serviceName,
                       Function<RESOURCE, CompletableFuture<Void>> onAccept,
                       Function<RESOURCE, CompletableFuture<Void>> onReject,
                       Algorithm algorithm,
                       int threadCount) throws Exception {
        this.serviceName = serviceName;
        this.onAccept = onAccept;
        this.onReject = onReject;
        this.rateLimiterServer = new RateLimiterServer();
        this.algorithm = algorithm == null ?
                new SlidingWindow(rateLimiterServer.getServiceConfig(serviceName).get())
                : algorithm;
        executorService = Executors.newFixedThreadPool(threadCount);
    }

    public CompletableFuture<Map<String,Limit>> readKey(String json) {
        return CompletableFuture.supplyAsync(() -> rateLimiterServer.readKey(), executorService)
                .thenCompose(Function.identity());
    }

    public CompletableFuture<Void> updateKey(String key, int amount) {
        return CompletableFuture.supplyAsync(() -> rateLimiterServer.updateKey(key, amount, System.currentTimeMillis()), executorService)
                .thenCompose(Function.identity());
    }

    public CompletableFuture<Void> onAccept(RESOURCE resource) {
        algorithm.forwardPressure(System.currentTimeMillis());
        return onAccept.apply(resource);
    }

    public CompletableFuture<Void> onReject(RESOURCE resource) {
        algorithm.backPressure(System.currentTimeMillis());
        return onReject.apply(resource);
    }

    public boolean shouldAccept(RESOURCE resource) {
        return algorithm.shouldAccept(System.currentTimeMillis());
    }
}

class Limit {
    int amount;
    String timeUnit;
}

