package com.cecs453.myassignment_02;

import java.io.Serializable;

/**
 * Created by Michael on 10/14/2015.
 */
public class Animal implements Serializable {
    private String animalName;
    private String animalDescription;
    private int iconId;

    public Animal() {
    }

    public Animal(String animalName, String animalDescription, int iconId) {
        this.animalName = animalName;
        this.animalDescription = animalDescription;
        this.iconId = iconId;
    }

    public String getAnimalName() {
        return animalName;
    }

    public String getAnimalDescription() {
        return animalDescription;
    }

    public int getIconId() {    return iconId;  }

    @Override
    public String toString() {
        return "Animal [animalName=" + animalName + ", animalDescription=" + animalDescription + ", iconId=" + iconId + "]";
    }
}
