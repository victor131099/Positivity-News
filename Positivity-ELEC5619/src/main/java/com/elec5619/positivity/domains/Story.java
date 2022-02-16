package com.elec5619.positivity.domains;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Story {
    @Id
    private String id;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String authors;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishedDate;

    @Column(nullable = false)
    private String link;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String excerpt;

    @Column(nullable = false)
    private String source;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String summary;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String media;

    @Column(nullable = false)
    private Integer likes;

    @Column(nullable = false)
    private Integer views;

    public Story(
            String id,
            String country,
            String language,
            String title,
            String authors,
            Date publishedDate,
            String link,
            String excerpt,
            String source,
            String summary,
            String media,
            Integer likes,
            Integer views
    ) {
        this.id = id;
        this.country = country;
        this.language = language;
        this.title = title;
        this.authors = authors;
        this.publishedDate = publishedDate;
        this.link = link;
        this.excerpt = excerpt;
        this.source = source;
        this.summary = summary;
        this.media = media;
        this.likes = likes;
        this.views = views;
    }

    public Story() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }
}
