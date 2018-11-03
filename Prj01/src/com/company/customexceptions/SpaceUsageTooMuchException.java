package com.company.customexceptions;

public class SpaceUsageTooMuchException extends Exception {
    public SpaceUsageTooMuchException(String roomName) {
        super("in Room " + roomName);
    }
}
