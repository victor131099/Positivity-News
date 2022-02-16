package com.elec5619.positivity.utils;

public class Country {
    public String type;
    public CountryProperty properties;
    public CountryGeometry geometry;

    public Country(String type, CountryProperty properties, CountryGeometry geometry) {
        this.type = type;
        this.properties = properties;
        this.geometry = geometry;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CountryProperty getProperties() {
        return properties;
    }

    public void setProperties(CountryProperty properties) {
        this.properties = properties;
    }

    public CountryGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(CountryGeometry geometry) {
        this.geometry = geometry;
    }
}
