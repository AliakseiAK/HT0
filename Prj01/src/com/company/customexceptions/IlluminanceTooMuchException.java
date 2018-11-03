package com.company.customexceptions;

public class IlluminanceTooMuchException extends Exception {
    public IlluminanceTooMuchException(String roomName) {
        super("in Room " + roomName);
    }
}
