package com.example.pickup.enums;

public enum TimelineTab {
    ALL("ALL"), GOING("GOING"), MAYBE("MAYBE"), NO("NO"), YOUR_EVENTS("YOUR_EVENTS");

    private final String timelineTab;

    TimelineTab(String tab) {
        this.timelineTab = tab;
    }

    public String text() {
        return timelineTab;
    }
}
