package com.elec5619.positivity.repositories;

import com.elec5619.positivity.domains.History;
import com.elec5619.positivity.domains.Story;
import com.elec5619.positivity.domains.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository extends CrudRepository<History, Integer>{
    public Optional<History> findByUserAndStory(User user, Story story);

    public List<History> findByUserOrderByTimeDesc(User user);

//    List<History> historyList();

}
