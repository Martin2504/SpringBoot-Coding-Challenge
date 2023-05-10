package com.example.claytoncodingassessment.model;

public enum Status {
    SUCCESS,
    RUNNING,
    FAILURE;


    public static boolean contains(String test) {

        for (Status s : Status.values()) {
            if (s.name().equals(test)) {
                return true;
            }
        }

        return false;
    }
}
