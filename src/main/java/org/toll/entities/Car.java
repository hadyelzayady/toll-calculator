package org.toll.entities;

public class Car implements Vehicle {
    @Override
    public String getType() {
        return "Car";
    }

    @Override
    public boolean isFeeFree() {
        return false;
    }
}
