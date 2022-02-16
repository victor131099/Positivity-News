package com.elec5619.positivity.domains;

import javax.persistence.*;

@Entity
public class Outlet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String outlet_name;

    public Outlet(String outlet_name) {
        this.outlet_name = outlet_name;
    }

    public Outlet() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOutlet_name() {
        return outlet_name;
    }

    public void setOutlet_name(String outlet_name) {
        this.outlet_name = outlet_name;
    }
}
