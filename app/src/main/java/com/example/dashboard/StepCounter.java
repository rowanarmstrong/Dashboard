package com.example.dashboard;
/*
Welcome to the Step Counter!
We use this class to listen to step alerts. It deserves its own class to let the value of timeNs stabilise.

Student Name: Rowan Armstrong
Student ID: s1541585

 */

// listen to step alerts
public interface StepCounter {

    public void step(long timeNs);

}
