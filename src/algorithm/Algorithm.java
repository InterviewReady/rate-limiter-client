package algorithm;

import models.ServiceConfiguration;

public abstract class Algorithm {

    ServiceConfiguration serviceConfiguration;

    public Algorithm(ServiceConfiguration serviceConfiguration) {
        this.serviceConfiguration = serviceConfiguration;
    }

    public abstract void backPressure(long timestamp);
    public abstract void forwardPressure(long timestamp);

    public abstract boolean shouldAccept(long timestamp);
}
