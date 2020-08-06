package com.example.pickup.enums;

public enum Availability {
    NA("Not Specified"), GOING("Going"), MAYBE("Maybe"), NO("No");

    private final String availabilityStatus;

    Availability(String availability) {
        this.availabilityStatus = availability;
    }

    public String text() {
        return availabilityStatus;
    }
}
