package com.example.pickup.enums;

public enum QueryType {
    ALL("All"), USER("User");

    private String queryType;

    QueryType(String type) {
        queryType = type;
    }

    public String toString() {
        return queryType;
    }
}
