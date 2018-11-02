package com.company.entities;


import com.company.customexceptions.IllegalNameException;
import com.company.customexceptions.NoSuchRoomException;

import java.util.Map;
import java.util.TreeMap;

/**
 * Class for building. A building consists of rooms.
 */
public class Building {
    private boolean isValid;
    private String buildingName;
    private TreeMap<String, Room> rooms;

    public Building(String buildingName) throws IllegalNameException {
        if (buildingName.isEmpty()){
            throw new IllegalNameException();
        }
        this.buildingName = buildingName;
        this.rooms = new TreeMap<>();
        this.isValid = false;
    }

    /**
     * Method adds new room to the building.
     * @param room is a new room to add.
     */
    public void addRoom(Room room){
        rooms.put(room.getRoomName(), room);
    }

    /**
     * Method searches room by its name.
     * @param roomName is the name of the room to return.
     * @return room entity by it unique name.
     * @throws NoSuchRoomException when there is no room with given name in the building.
     */
    public Room getRoom(String roomName) throws NoSuchRoomException {
        if (rooms.get(roomName) == null){
            throw new NoSuchRoomException();
        }
        return rooms.get(roomName);
    }

    /**
     * Method formats description of the current building.
     * @return String with formatted description of the current building.
     */
    public String describe(){
        StringBuilder description = new StringBuilder(buildingName + "\n");
        for (Map.Entry<String, Room> room : rooms.entrySet()){
            description.append(room.getValue().describeRoom()).append("\n");
        }
        return description.toString();
    }

    /**
     * Method checks if the room furniture and lights are valid.
     */
    public void validate(){
        isValid = true;
        for (Map.Entry<String, Room> roomEntry : rooms.entrySet()){
            if (!roomEntry.getValue().isValid()) {
                isValid = false;
                System.out.println("Room " + roomEntry.getKey() + " is invalid \n");
            }
        }
        if (isValid){
            System.out.println("Building " + buildingName + " is valid \n");
        } else {
            System.out.println("Building " + buildingName + " is invalid \n");
        }
    }

    /**
     * Method deletes the room by name.
     * @param name the name of the room.
     */
    public void deleteRoom(String name){
        rooms.remove(name);
    }

    /**
     * Method wraps Room name setter.
     * @param roomName name of the Room to rename.
     * @param newName new name.
     */
    public void renameRoom(String roomName, String newName){
        rooms.get(roomName).setRoomName(newName);
    }
}
