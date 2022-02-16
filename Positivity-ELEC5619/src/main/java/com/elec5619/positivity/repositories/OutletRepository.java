package com.elec5619.positivity.repositories;

import com.elec5619.positivity.domains.Outlet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutletRepository extends CrudRepository<Outlet, Integer> {

    @Query("select outlet_name from Outlet")
    List<String> getAllOutletNames();

    @Query("SELECT o from Outlet o WHERE o.outlet_name=:outlet_name")
    List<Outlet> findByOutletName(@Param("outlet_name") String outlet_name);

}
