package com.example.chshux.lab3;

/**
 * Created by chshux on 2017/10/23.
 */

public class TrolleyItem {
    private String name;
    private String firstLetter;
    private String price;
    private int index;

    public TrolleyItem(String name, String firstLetter, String price, int index) {
        this.name = name;
        this.firstLetter = firstLetter;
        this.price = price;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public String getPrice() {
        return price;
    }

    public int getIndex() {
        return index;
    }
}
