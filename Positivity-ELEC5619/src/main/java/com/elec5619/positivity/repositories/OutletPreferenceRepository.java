package com.elec5619.positivity.repositories;

import com.elec5619.positivity.domains.Outlet;
import com.elec5619.positivity.domains.OutletPreference;
import com.elec5619.positivity.domains.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface OutletPreferenceRepository extends CrudRepository<OutletPreference, Integer>{


    @Query("select o.outlet_name from OutletPreference p join Outlet o on p.outlet.id=o.id where p.user.id=:user_id")
    List<String> getOutletPreferenceNamesByUserId(@Param("user_id") Integer user_id);

    @Query("select o from OutletPreference p join Outlet o on p.outlet.id=o.id where p.user.id=:user_id")
    List<Outlet> getOutletPreferencesByUserId(@Param("user_id") Integer user_id);

    @Query("delete from OutletPreference p where p.user=:user_id")
    Integer removeOutletPreferencesByUserId(@Param("user_id") Integer user_id);

    @Transactional
    @Modifying
    @Query("DELETE from OutletPreference p WHERE p.user.id=:user_id and p.outlet.id=:outlet_id")
    Integer removeOutletPreference(@Param("user_id") Integer user_id, @Param("outlet_id") Integer outlet_id);

}
