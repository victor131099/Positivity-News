package com.elec5619.positivity;

import com.elec5619.positivity.controllers.CountryController;
import com.elec5619.positivity.utils.Country;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;


public class CountryJsonTest {
    @Test
    public void testDeserialize() throws Exception {
        CountryController c = new CountryController();
        List<Country> countries = c.getCountries("src/test/resources/static/testcountry.geo.json", "src/main/resources/static/countryCode.json");
        assertEquals(countries.size(), 2);
        assertEquals(countries.get(1).getProperties().country, "United States");
        assertEquals(countries.get(0).getProperties().country, "Canada");
        assertEquals(countries.get(0).getGeometry().type, "MultiPolygon");
        assertEquals(countries.get(1).getGeometry().type, "MultiPolygon");
    }
}
