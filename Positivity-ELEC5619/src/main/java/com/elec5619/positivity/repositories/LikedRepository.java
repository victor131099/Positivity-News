package com.elec5619.positivity.repositories;

import com.elec5619.positivity.domains.Liked;
import com.elec5619.positivity.domains.Story;
import com.elec5619.positivity.domains.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LikedRepository extends CrudRepository<Liked, Integer> {
    public Optional<Liked> findByUserAndStory(User user, Story story);
}
