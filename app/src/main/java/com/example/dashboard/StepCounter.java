package com.example.dashboard;

// Will listen to step alerts
public interface StepCounter {

    public void step(long timeNs);

}
