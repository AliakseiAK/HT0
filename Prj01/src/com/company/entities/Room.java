package com.company.entities;


import com.company.customexceptions.*;
import com.company.furniture.RoomEquipment;
import com.company.furniture.interfaces.FurnitureItem;
import com.company.furniture.interfaces.LightItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import static com.company.MyConstants.*;

/**
 * Class for rooms. A room can contain furniture and light bulbs.
 */
public class Room {
    private String roomName;
    private double roomSpace;
    private double currentUsedSpace;
    private double currentRoomIlluminance;
    private int windowsCount;
    private ArrayList<FurnitureItem> roomFurnitureItems;
    private ArrayList<LightItem> roomLights;

    public Room(String roomName, double roomSpace, int windowsCount) throws IllegalNameException, IllegalValueException {
        if (roomName.isEmpty()){
            throw new IllegalNameException();
        }
        if (roomSpace <= 0 || windowsCount < 0){
            throw new IllegalValueException();
        }
        this.currentRoomIlluminance = windowsCount * WINDOW_LUMINOCITY;
        this.currentUsedSpace = 0;
        this.roomName = roomName;
        this.roomSpace = roomSpace;
        this.windowsCount = windowsCount;
        this.roomFurnitureItems = new ArrayList<>();
        this.roomLights = new ArrayList<>();
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    /**
     * Method to add new furniture item or light bulb to the room.
     * If the way to use this method wasn't so strictly put in guidelines,
     * we should strongly consider using Factory pattern.
     * @param item any furniture to add to the room.
     * @throws UnknownItemException when there is unknown class of furniture.
     */
    public void add(RoomEquipment item) throws UnknownItemException {
        if (item instanceof LightItem){
            roomLights.add((LightItem)item);
            currentRoomIlluminance += ((LightItem) item).getLuminocity();
        } else if (item instanceof FurnitureItem){
            roomFurnitureItems.add((FurnitureItem)item);
            currentUsedSpace += ((FurnitureItem) item).getOccupiedSpace();
        } else {
            throw new UnknownItemException();
        }
    }

    /**
     * Method removes given Furniture item by name.
     * @param name name of the item.
     */
    public void removeFurnitureItem(String name) {
        for (FurnitureItem furnitureItem : roomFurnitureItems){
            if (furnitureItem.getName().equals(name)){
                this.roomFurnitureItems.remove(furnitureItem);
                // As we can have FurnitureItem with equal names,
                // deleting only the first FurnitureItem with the given name.
                break;
            }
        }
    }

    /**
     * Method removes given number of windows from room.
     * @param count number of windows to remove.
     * @throws IllegalValueException in case of wrong number of windows.
     */
    public void removeWindows(int count) throws IllegalValueException {
        if (this.windowsCount > 0 && count <= this.windowsCount){
            this.windowsCount -= count;
            currentRoomIlluminance -= (count * WINDOW_LUMINOCITY);
        } else {
            throw new IllegalValueException();
        }
    }

    /**
     * Method returns currend Room validity
     * @return true if the Room is valid.
     * @throws IlluminanceTooMuchException when lewel of the luminocity exeeds maximum value.
     * @throws SpaceUsageTooMuchException when space occupied by furniture exeeds 70% of the room available space.
     */
    public boolean validate() throws IlluminanceTooMuchException, SpaceUsageTooMuchException {
        if (checkIlluminance() && checkSpace()){
            return true;
        } else if (!checkSpace()){
            throw new SpaceUsageTooMuchException(this.roomName);
        } else {
            throw new IlluminanceTooMuchException(this.roomName);
        }
    }

    /**
     * Method returns information on room as formatted String.
     * @return formatted String with information on room.
     */
    public String describeRoom(){
        return roomName + "\n"
                + formatLightsDescription()
                + formatFurnitureDescription();
    }

    private String formatLightsDescription(){
        StringBuilder formattedLights = new StringBuilder("Освещенность = " + currentRoomIlluminance
                + " (" + windowsCount  + " окна по " + WINDOW_LUMINOCITY + " лк., ");
        if (!roomLights.isEmpty()){
            formattedLights.append("лампочки: ");
            for (LightItem light : roomLights){
                formattedLights.append(light.getLuminocity()).append(" лк. ");
            }
            formattedLights.append("). \n");
        } else {
            formattedLights.append("лампочек нет)." + "\n");
        }
        return formattedLights.toString();
    }

    private String formatFurnitureDescription(){
        StringBuilder formattedFurniture = new StringBuilder("Площадь = "
                + roomSpace + "м^2 (занято " + currentUsedSpace + "м^2, "
                + "гарантированно свободно " + (roomSpace - currentUsedSpace)
                + "м^2 или " + getSpacePercentage() + "% площади)." + "\n");
        formattedFurniture.append("Мебель: \n");
        if (!roomFurnitureItems.isEmpty()) {
            for (FurnitureItem furnitureItem : roomFurnitureItems) {
                formattedFurniture.append(furnitureItem.formatForPrint());
            }
        } else {
            formattedFurniture = new StringBuilder("Мебели нет.");
        }
        return formattedFurniture.toString();
    }

    private double getSpacePercentage() {
        if (currentUsedSpace > 0){
            return new BigDecimal(100 - (currentUsedSpace / roomSpace) * 100)
                    .setScale(3, RoundingMode.UP)
                    .doubleValue();
        } else {
            return 100;
        }
    }

    private boolean checkIlluminance(){
        return currentRoomIlluminance >= 0
                && currentRoomIlluminance > MIN_ROOM_LUMINOCITY
                && currentRoomIlluminance < MAX_ROOM_LUMINOCITY;
    }

    private boolean checkSpace(){
        return currentUsedSpace >= 0 && currentUsedSpace < (roomSpace * MAX_FURNITURE_SPACE);
    }

    public void removeLightItem(String name) {
        for (LightItem light : roomLights){
            if (light.getName().equals(name)){
                this.roomLights.remove(light);
                // As we can have LightItem with equal names, exiting the loop after
                // deleting only the first FurnitureItem with the given name.
                break;
            }
        }
        for(LightItem item : roomLights) {
            if(item.getName().equals(name)) {
                roomLights.remove(item);
            }
        }
    }
}