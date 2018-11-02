package com.company.furniture.items;


import com.company.furniture.RoomEquipment;
import com.company.furniture.interfaces.FurnitureItem;

/**
 * Class for tables. Each table has square in m^2. There are two constructors: for tables with and without names.
 */
public class Table extends RoomEquipment implements FurnitureItem {
    private int tableSquare;
    private String tableName;


    public Table(int tableSpace){
        this.tableName = "Безымянный стол";
        this.tableSquare = tableSpace;
    }

    public Table(String tableName, int tableSquare){
        this.tableName = tableName;
        this.tableSquare = tableSquare;
    }

    @Override
    public int getOccupiedSpace() {
        return this.tableSquare;
    }

    @Override
    public String getName() {
        return this.tableName;
    }

    /**
     * Method creates text description of the Table. Alternative solution is to override toString() method.
     * @return
     */
    @Override
    public String formatForPrint() {
        StringBuilder tableForPrint = new StringBuilder();
        if (this.tableName != null){
            tableForPrint.append(this.tableName).append(" ");
        } else {
            tableForPrint.append("Безымянный стол ");
        }
        tableForPrint.append(" (площадь ")
                .append(this.tableSquare)
                .append("м^2) \n");
        return tableForPrint.toString();
    }
}
