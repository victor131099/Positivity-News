package com.elec5619.positivity.repositories;

import com.elec5619.positivity.domains.Story;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface StoryRepository extends CrudRepository<Story, Integer>{

    @Query("SELECT s.id FROM Story s")
    List<String> getAllIds();

    Optional<Story> findById(String id);

}
