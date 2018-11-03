package com.company.customexceptions;

public class IllegalNameException extends Throwable {
    public IllegalNameException() {
        super("У комнаты должно быть имя");
    }
}
