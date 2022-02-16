package com.elec5619.positivity.repositories;

import com.elec5619.positivity.domains.Region;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface RegionRepository extends CrudRepository<Region, Integer> {

    Region findByRegionName(String regionName);

    @Modifying
    @Transactional
    @Query(value = "delete from Region r where r.id=:id", nativeQuery = true)
    Integer removeRegionById(@Param("id") Integer region_id);

    @Query("select regionName from Region")
    List<String> getAllRegionNames();
}
