package com.elec5619.positivity.forms;

public class SearchForm {

    private String keywords;
    private String topic;
    private String outlet;
    private String region;
    private String language;
    private String orderBy;

    public String getKeywords() { return keywords; }
    public String getTopic() { return topic; }
    public String getOutlet() { return outlet; }
    public String getRegion() { return region; }
    public String getLanguage() { return language; }
    public String getOrderBy() { return orderBy; }

    public void setKeywords(String keywords) { this.keywords = keywords.toLowerCase(); }
    public void setTopic(String topic) { this.topic = topic.toLowerCase(); }
    public void setOutlet(String outlet) { this.outlet = outlet.toLowerCase(); }
    public void setRegion(String region) { this.region = region; }
    public void setLanguage(String language) { this.language = language; }
    public void setOrderBy(String orderBy) { this.orderBy = orderBy.toLowerCase(); }

    public String toString() {
        return "searchForm(keywords: " + this.keywords + ", topic:" + this.topic + ", outlet:" + this.outlet + ", region:" + this.region + ", language:" + this.language + ", orderBy:" + this.orderBy + ")";
    }

}
