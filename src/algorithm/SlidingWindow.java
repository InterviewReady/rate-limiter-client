package algorithm;

import models.ServiceConfiguration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SlidingWindow extends Algorithm {

    double threshold;
    RLTimeUnit timeUnit;
    List<Long> timestamps;

    public SlidingWindow(ServiceConfiguration serviceConfiguration) {
        super(serviceConfiguration);
        threshold = serviceConfiguration.getAmount();
        timeUnit = serviceConfiguration.getTimeUnit();
    }

    @Override
    public void backPressure(long timestamp) {
        if (timestamp > System.currentTimeMillis() + 100) {
            threshold--;
        } else {
            threshold -= threshold*0.1;
        }
    }

    @Override
    public void forwardPressure(long timestamp) {
        threshold = threshold + 0.1;
    }

    @Override
    public boolean shouldAccept(long timestamp) {
        timestamps = timestamps.stream()
                .filter(this::hasExpired)
                .collect(Collectors.toList());
        if (threshold > timestamps.size()) {
            timestamps.add(timestamp);
            return true;
        } else {
            return false;
        }
    }

    private boolean hasExpired(Long ts) {
        long timeElapsed = System.currentTimeMillis() - ts;
        long maxTimeAllowed = Duration.of(1, ChronoUnit.valueOf(timeUnit.toString())).toMillis();
        return timeElapsed > maxTimeAllowed;
    }
}

