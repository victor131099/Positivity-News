package com.elec5619.positivity.controllers;

import com.elec5619.positivity.repositories.RegionPreferenceRepository;
import com.elec5619.positivity.utils.Country;
import com.elec5619.positivity.utils.CountryGeometry;
import com.elec5619.positivity.utils.CountryProperty;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/countries")
public class CountryController {
    @Autowired
    private RegionPreferenceRepository regionPreferenceRepository;

    public List<Country> getCountries(String file, String file1) throws JSONException, IOException {
        String fileName = file;
        String text = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
        String fileName1 =file1;
        String text1 = new String(Files.readAllBytes(Paths.get(fileName1)), StandardCharsets.UTF_8);
        JSONArray arr2= new JSONArray(text1);

        JSONObject obj = new JSONObject(text);
        JSONArray arr = obj.getJSONArray("features");
        List<Country> countries = new ArrayList<Country>();
        for (int i =0 ; i < arr.length();i++){
            String name = arr.getJSONObject(i).getJSONObject("properties").getString("sovereignt");
            String countryCode= null;
            for (int index =0 ; index < arr2.length();index++){
               if (arr2.getJSONObject(index).getString("Country_Name").equals(name)){

                   countryCode= arr2.getJSONObject(index).getString("ISO3166_Alpha2_Code");
                   break;
               }

            }

            Country country=  new Country("Feature", null, null);
            int number = (int) (Math.random()*110);
            country.properties = new CountryProperty(name, number, countryCode);
            ArrayList<Double[]> coordinates = new ArrayList<Double[]>();
            JSONArray arr1= arr.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates");
            String geotype = arr.getJSONObject(i).getJSONObject("geometry").getString("type");

            if (geotype.startsWith( "Polygon")){
                ArrayList<ArrayList<Double[]>> new_coordinates= new ArrayList<ArrayList<Double[]>>();
                for (int k1=0; k1 < arr1.length(); k1++){
                    ArrayList<Double[]> new_coordinates1= new ArrayList<Double[]>();
                    for(int k2=0; k2< arr1.optJSONArray(k1).length(); k2++){
                        Double[] new_double = new Double[2];
                        new_double[0]= arr1.optJSONArray(k1).optJSONArray(k2).getDouble(0);
                        new_double[1]= arr1.optJSONArray(k1).optJSONArray(k2).getDouble(1);
                        new_coordinates1.add(new_double);
                    }
                    new_coordinates.add(new_coordinates1);
                }
                CountryGeometry<ArrayList<ArrayList<Double[]>> > new_geometry = new CountryGeometry<ArrayList<ArrayList<Double[]>>>( new_coordinates,geotype);
                country.geometry = new_geometry;

            }else if(geotype.startsWith("MultiPolygon")){
                ArrayList<ArrayList<ArrayList<Double[]>>> new_coordinates= new ArrayList< ArrayList<ArrayList<Double[]>>>();
                for (int k1=0; k1 < arr1.length(); k1++){
                    ArrayList< ArrayList<Double[]>> new_coordinates1= new ArrayList< ArrayList<Double[]>>();
                    for(int k2=0; k2< arr1.optJSONArray(k1).length(); k2++){
                        ArrayList<Double[]> new_coordinates2= new ArrayList<Double[]>();
                        for(int k3=0; k3< arr1.optJSONArray(k1).getJSONArray(k2).length(); k3++) {
                            Double[] new_double = new Double[2];
                            new_double[0] = arr1.optJSONArray(k1).optJSONArray(k2).getJSONArray(k3).getDouble(0);
                            new_double[1] = arr1.optJSONArray(k1).optJSONArray(k2).getJSONArray(k3).getDouble(1);
                            new_coordinates2.add(new_double);
                        }
                        new_coordinates1.add(new_coordinates2);
                    }
                    new_coordinates.add(new_coordinates1);
                }
                CountryGeometry<ArrayList< ArrayList<ArrayList<Double[]>>>> new_geometry = new CountryGeometry<ArrayList< ArrayList<ArrayList<Double[]>>>>( new_coordinates,geotype);
                country.geometry = new_geometry;
            }

            countries.add(country);

        }
        return countries;

    }
    @GetMapping("/get")
    public List<Country> getCountryjson() throws JSONException, IOException {
        return getCountries( "src/main/resources/static/new_data.geo.json","src/main/resources/static/countryCode.json");
    }

    @GetMapping("/selectedCountries")
    public List<String> getSelectedCountries(HttpSession session){
        return regionPreferenceRepository.getRegionPreferencesByUserId( (Integer) session.getAttribute("user"));
    }

}
