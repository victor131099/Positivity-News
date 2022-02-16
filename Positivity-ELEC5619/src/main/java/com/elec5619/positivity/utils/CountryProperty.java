package com.elec5619.positivity.utils;

public class CountryProperty {
    public String country;
    public int value;
    public String countryCode;

    public CountryProperty(String country, int value, String countryCode) {
        this.country = country;
        this.value = value;
        this.countryCode = countryCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
