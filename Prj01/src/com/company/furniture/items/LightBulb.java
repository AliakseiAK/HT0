package com.company.furniture.items;

import com.company.furniture.RoomEquipment;
import com.company.furniture.interfaces.LightItem;

/**
 * Class for light bulbs items.
 */
public class LightBulb extends RoomEquipment implements LightItem {
    private String lightBulbName;
    private int bulbLuminocity;

    public LightBulb(String lightBulbName, int bulbLuminocity){
        this.lightBulbName = lightBulbName;
        this.bulbLuminocity = bulbLuminocity;
    }

    @Override
    public String getName() {
        return lightBulbName;
    }

    @Override
    public int getLuminocity() {
        return this.bulbLuminocity;
    }
}
