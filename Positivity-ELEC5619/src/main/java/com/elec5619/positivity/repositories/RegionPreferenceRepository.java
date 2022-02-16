package com.elec5619.positivity.repositories;

import com.elec5619.positivity.domains.RegionPreference;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface RegionPreferenceRepository extends CrudRepository<RegionPreference, Integer>{


//    @Query(value = "Select r.regionName from RegionPreference p join Region r on p.region_id=r.id where p.user=:user", nativeQuery = true)
//    List<String> getUserPreferences(@Param("user") User user);

    @Modifying
    @Transactional
    @Query(value = "delete from RegionPreference p where p.region_id=:region_id and p.user_id=:user_id", nativeQuery = true)
    Integer removeRegionPreferencesByRegionId(@Param("user_id") Integer user_id,@Param("region_id") Integer region_id);

    @Query(value ="select p.region_id from RegionPreference p join Region r on p.region_id=r.id where p.user_id=:user_id and p.region_id =:region_id",nativeQuery = true)
    Integer getRegionPreferenceById(@Param("user_id") Integer user_id,@Param("region_id") Integer region_id);

    @Query(value ="select r.regionName from RegionPreference p join Region r on p.region_id=r.id where p.user_id=:user_id ",nativeQuery = true)
    List<String> getRegionPreferencesByUserId(@Param("user_id") Integer user_id );


    @Query("delete from RegionPreference p where p.user=:user_id")
    Integer removeRegionPreferencesByUserId(@Param("user_id") Integer user_id);

}
