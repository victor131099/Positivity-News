package com.elec5619.positivity.domains;

import javax.persistence.*;

@Entity
public class RegionPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    public RegionPreference() { }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RegionPreference(User user, Region region) {
        this.user = user;
        this.region = region;
    }
}

