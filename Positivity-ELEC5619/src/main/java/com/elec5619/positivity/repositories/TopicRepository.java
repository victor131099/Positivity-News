package com.elec5619.positivity.repositories;

import com.elec5619.positivity.domains.Topic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TopicRepository extends CrudRepository<Topic, Integer>{

    @Query("select topic_name from Topic")
    List<String> getAllTopicNames();

    @Query("SELECT t from Topic t WHERE t.topic_name=:topic_name")
    List<Topic> findByTopicName(@Param("topic_name") String topic_name);

}
