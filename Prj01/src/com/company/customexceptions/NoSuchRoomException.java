package com.company.customexceptions;

public class NoSuchRoomException extends Exception {
    public NoSuchRoomException(String roomName) {
        super(roomName);
    }
}
