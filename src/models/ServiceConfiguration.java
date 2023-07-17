package models;

import algorithm.RLTimeUnit;

import java.util.concurrent.TimeUnit;

public class ServiceConfiguration {
    int amount;
    RLTimeUnit timeUnit;

    public int getAmount() {
        return amount;
    }

    public RLTimeUnit getTimeUnit() {
        return timeUnit;
    }
}
