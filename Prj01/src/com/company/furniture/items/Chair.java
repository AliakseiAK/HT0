package com.company.furniture.items;

import com.company.customexceptions.IllegalFurnitureSizeException;
import com.company.furniture.RoomEquipment;
import com.company.furniture.interfaces.FurnitureItem;

/**
 * Class for chairs. Each chair has square in m^2. There are two constructors: for chairs with and without names.
 */
public class Chair extends RoomEquipment implements FurnitureItem {
    private String chairName;
    private int chairSpaceMin;
    private int chairSpaceMax;

    public Chair(String chairName, int chairSpaceMin, int chairSpaceMax) throws IllegalFurnitureSizeException {
        this.chairName = chairName;
        if (chairSpaceMin > 0 && chairSpaceMax > 0 && chairSpaceMax >= chairSpaceMin){
            this.chairSpaceMin = chairSpaceMin;
            this.chairSpaceMax = chairSpaceMax;
        } else {
            throw new IllegalFurnitureSizeException();
        }
    }

    @Override
    public int getOccupiedSpace() {
        return this.chairSpaceMax;
    }

    @Override
    public String getName() {
        return this.chairName;
    }

    /**
     * Method formats chair description.
     * @return String with formatted description of the chair.
     */
    @Override
    public String formatForPrint() {
        StringBuilder chairForPrint = new StringBuilder();
        if (!this.chairName.isEmpty()){
            chairForPrint.append(this.chairName).append(" ");
        } else {
            chairForPrint.append("Безымянное кресло ");
        }
        chairForPrint.append(" (площадь от ")
                .append(this.chairSpaceMin)
                .append("м^2")
                .append(" до ")
                .append(this.chairSpaceMax)
                .append("м^2) \n");
        return chairForPrint.toString();
    }
}
