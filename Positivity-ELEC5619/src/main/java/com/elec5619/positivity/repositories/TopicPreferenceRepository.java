package com.elec5619.positivity.repositories;

import com.elec5619.positivity.domains.Topic;
import com.elec5619.positivity.domains.TopicPreference;
import com.elec5619.positivity.domains.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface TopicPreferenceRepository extends CrudRepository<TopicPreference, Integer> {

    @Query("select t.topic_name from TopicPreference p join Topic t on p.topic.id=t.id where p.user.id=:user_id")
    List<String> getTopicPreferenceNamesByUserId(@Param("user_id") Integer user_id);

    @Query("select t from TopicPreference p join Topic t on p.topic.id=t.id where p.user.id=:user_id")
    List<Topic> getTopicPreferencesByUserId(@Param("user_id") Integer user_id);

    @Query("delete from TopicPreference p where p.user=:user_id")
    Integer removeTopicPreferencesByUserId(@Param("user_id") Integer user_id);

    @Transactional
    @Modifying
    @Query("DELETE from TopicPreference t WHERE t.user.id=:user_id and t.topic.id=:topic_id")
    Integer removeTopicPreference(@Param("user_id") Integer user_id, @Param("topic_id") Integer topic_id);

}
