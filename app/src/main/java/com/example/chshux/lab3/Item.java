package com.example.chshux.lab3;

/**
 * Created by chshux on 2017/10/23.
 */

public class Item {
    private String Name;
    private String first;
    private int index;
    public Item(String name, String first, int index) {
        this.Name = name;
        this.first = first;
        this.index = index;
    }
    public String getName() {
        return Name;
    }
    public String getFirst() {
        return first;
    }
    public int getIndex() {
        return index;
    }
}