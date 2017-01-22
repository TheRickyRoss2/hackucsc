package com.beckoningtech.fastnsafe;

import java.util.ArrayList;

/**
 * Created by William on 1/21/2017.
 */

public class Recipient {
    public String name;
    public ArrayList<String> numbers;
    public ArrayList<String> emails;
    public Recipient(String name, ArrayList<String> numbers, ArrayList<String> emails){
        this.emails = emails;
        this.numbers = numbers;
        this.name = name;
    }
}
