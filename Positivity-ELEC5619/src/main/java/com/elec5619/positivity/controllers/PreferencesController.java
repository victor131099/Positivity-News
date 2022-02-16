package com.elec5619.positivity.controllers;

import com.elec5619.positivity.domains.*;
import com.elec5619.positivity.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping(path = "/api")
public class PreferencesController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private TopicPreferenceRepository topicPreferenceRepository;

    @Autowired
    private OutletRepository outletRepository;

    @Autowired
    private OutletPreferenceRepository outletPreferenceRepository;

    @Autowired
    private RegionPreferenceRepository regionPreferenceRepository;

    @Autowired
    private RegionRepository regionRepository;

    @PutMapping(path = "outletPreference")
    public @ResponseBody
    String addOutletPreference(@RequestParam String outlet_name, HttpSession session) {
        Integer userId;
        try {
            userId = (Integer) session.getAttribute("user");
        } catch (Exception e) {
            return "401";
        }
        if (userId == null || !userRepository.findById(userId).isPresent()) {
            return "401";
        }

        User u = userRepository.findById(userId).get();
        Outlet o = outletRepository.findByOutletName(outlet_name).get(0);

        OutletPreference outletPref = new OutletPreference(u, o);
        try {
            outletPreferenceRepository.save(outletPref);
        } catch (Exception e) {
            return "201";
        }
        return "201";
    }

    @DeleteMapping(path = "outletPreference")
    public @ResponseBody
    String removeOutletPreference(@RequestParam String outlet_name, HttpSession session) {
        // Do some check based on the session
        Integer userId;
        try {
            userId = (Integer) session.getAttribute("user");
        } catch (Exception e) {
            return "401";
        }
        if (userId == null || !userRepository.findById(userId).isPresent()) {
            return "401";
        }

        Outlet o = outletRepository.findByOutletName(outlet_name).get(0);
        try {
            outletPreferenceRepository.removeOutletPreference(userId, o.getId());
        } catch (Exception e) {
            return "200";
        }
        return "200";
    }

    @PutMapping(path = "topicPreference")
    public @ResponseBody
    String addTopicPreference(@RequestParam String topic_name, HttpSession session) {
        Integer userId;
        try {
            userId = (Integer) session.getAttribute("user");
        } catch (Exception e) {
            return "401";
        }
        if (userId == null || !userRepository.findById(userId).isPresent()) {
            return "401";
        }

        User u = userRepository.findById(userId).get();
        Topic t = topicRepository.findByTopicName(topic_name).get(0);

        TopicPreference topicPref = new TopicPreference(u, t);
        try {
            topicPreferenceRepository.save(topicPref);
        } catch (Exception e) {
            return "201";
        }
        return "201";
    }

    @DeleteMapping(path = "topicPreference")
    public @ResponseBody
    String removeTopicPreference(@RequestParam String topic_name, HttpSession session) {
        // Do some check based on the session
        Integer userId;
        try {
            userId = (Integer) session.getAttribute("user");
        } catch (Exception e) {
            return "401";
        }
        if (userId == null || !userRepository.findById(userId).isPresent()) {
            return "401";
        }

        Topic t = topicRepository.findByTopicName(topic_name).get(0);
        try {
            topicPreferenceRepository.removeTopicPreference(userId, t.getId());
        } catch (Exception e) {
            return "200";
        }
        return "200";
    }

    @PostMapping("/countries")
    public String postCountries(@RequestParam(value = "country") String country, HttpSession session){
        Integer currentUserId = (Integer) session.getAttribute("user");
        Optional<User> user = userRepository.findById(currentUserId);
        if (country == null){
            return "preferences";
        }
        Optional<Region> region = Optional.ofNullable(regionRepository.findByRegionName(country));
        Integer region_id = 0;

        if (!region.isPresent()){
            Region r = new Region(country);
            region_id = r.getId();
            regionRepository.save(r);
        } else {
            region_id= region.get().getId();
        }
        Optional<Integer> rp = Optional.ofNullable(regionPreferenceRepository.getRegionPreferenceById(currentUserId, region_id));
        if (rp.isPresent()){
            regionPreferenceRepository.removeRegionPreferencesByRegionId(currentUserId,region_id );
            return "preferences";
        }

        Region r = regionRepository.findByRegionName(country);

        RegionPreference new_rp= new RegionPreference(user.get(), r);

        regionPreferenceRepository.save(new_rp);
        return "preferences";
    }
}