package com.elec5619.positivity.utils;

public class CountryGeometry<T> {
    public T coordinates;
    public String type;

    public CountryGeometry(T coordinates, String type) {
        this.coordinates = coordinates;
        this.type = type;
    }

    public T getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(T coordinates) {
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
