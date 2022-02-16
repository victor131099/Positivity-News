package com.elec5619.positivity.domains;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "outlet_id"})})
public class OutletPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "outlet_id")
    private Outlet outlet;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OutletPreference() {
    }

    public OutletPreference(User user, Outlet outlet) {
        this.user = user;
        this.outlet = outlet;
    }
}

