package com.company;

import com.company.customexceptions.*;
import com.company.entities.Building;
import com.company.entities.Room;
import com.company.furniture.items.Chair;
import com.company.furniture.items.LightBulb;
import com.company.furniture.items.Table;

/**
 * This application is a module for building engineering system.
 * It calculates illuminance level and space usage level for rooms of the given building.
 */
public class Main {

    public static void main(String[] args) throws SpaceUsageTooMuchException, IlluminanceTooMuchException,
            NoSuchRoomException, UnknownItemException, IllegalNameException, IllegalValueException,
            IllegalFurnitureSizeException {
        System.out.println("Welcome to Space&Light library");
        System.out.println("Use addRoom() to add a room to the building.");
        System.out.println("Use building.getRoom(String roomName).add() to add items.");
        System.out.println("Use building.getRoom(String roomName).remoweWindows(int count) to remowe given number of windows.");
        System.out.println("Use building.getRoom(String roomName).removeFurnitureItem(String furniture_name) to remove items.");
        System.out.println("Use building.renameRoom(String roomName, String newName) to rename the room.");
        System.out.println("Use building.describe() to get building description.");
        System.out.println("Use validate() to check for unusual data.");

        //Demo-1. Building with two validated rooms. Rooms are sorted by name.
        System.out.println("Demo-1. Building with two validated rooms. Rooms are sorted by name.");
        Building building = new Building("Building 1");

        building.addRoom(new Room("Room 2", 30, 2));
        building.getRoom("Room 2").add(new Table(3));

        building.addRoom(new Room("Room 1", 130, 3));
        building.getRoom("Room 1").add(new Chair("Мягкое клетчатое кресло", 2, 3));
        building.getRoom("Room 1").add(new Table(3));
        building.getRoom("Room 1").add(new LightBulb("Лампочка-1", 444));
        System.out.println(building.describe());
        building.validate();

        System.out.println("Edit room: building.getRoom(\"Room 1\").removeFurnitureItem(\"Мягкое клетчатое кресло\");");
        building.getRoom("Room 1").removeFurnitureItem("Мягкое клетчатое кресло");
        building.getRoom("Room 1").removeLightItem("Лампочка-1");
        System.out.println(building.describe());
        building.validate();

        System.out.println("Edit room: remowe one window: building.getRoom(\"Room 1\").removeWindows(1);");
        building.getRoom("Room 1").removeWindows(1);
        System.out.println(building.describe());
        building.validate();

        System.out.println("Delete room Room 2: building.deleteRoom(\"Room 2\")");
        building.deleteRoom("Room 2");
        System.out.println(building.describe());
        building.validate();

        System.out.println("Editing room Room 1: building.renameRoom(\"Room 1\", \"Room 3\")");
        building.renameRoom("Room 1", "Room 3");
        System.out.println(building.describe());
        building.validate();

        System.out.println("Demo-2. Building with invalide room. " +
                "\nbuilding2.addRoom(new Room(\"Room 2\", 3, 2));\n" +
                "building2.getRoom(\"Room 2\").add(new Table(4));" +
                "\n Should throw a SpaceUsageTooMuchException");
        Building building2 = new Building("Building 2");
        building2.addRoom(new Room("Room 2", 3, 2));
        building2.getRoom("Room 2").add(new Table(4));
        building2.validate();
        System.out.println(building2.describe());
    }
}