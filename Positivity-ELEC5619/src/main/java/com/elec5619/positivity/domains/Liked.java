package com.elec5619.positivity.domains;

import javax.persistence.*;

@Entity
public class Liked {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "story_id")
    private Story story;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Liked() {}

    public Liked(User user, Story story) {
        this.user = user;
        this.story = story;
    }

    public Story getStory() {
        return story;
    }

    public User getUser() { return user;}
}
