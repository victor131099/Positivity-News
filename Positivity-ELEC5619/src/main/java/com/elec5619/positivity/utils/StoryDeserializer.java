package com.elec5619.positivity.utils;

import com.elec5619.positivity.domains.Story;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StoryDeserializer implements JsonDeserializer<Story> {

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Story deserialize
            (JsonElement jElement, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject storyJSON = jElement.getAsJsonObject();

        JsonArray authorsArr = storyJSON.get("authors").getAsJsonArray();
        if (authorsArr.size() == 0) {
            throw new IllegalArgumentException();
        } else if (authorsArr.size() > 3) { // Truncate to three authors
            JsonArray temp = new JsonArray();
            temp.add(authorsArr.get(0));
            temp.add(authorsArr.get(1));
            temp.add(authorsArr.get(2));
            temp.add("...");
            authorsArr = temp;
        }
        String authors = "";

        for (int j = 0; j < authorsArr.size(); j++) {
            String author = authorsArr.get(j).getAsString();
            int spacesCount = 0;
            for (int i = 0; i < author.length(); i++) {
                if (author.charAt(i) == ' ') {
                    spacesCount+= 1;
                }
            }
            if (spacesCount < 2 && author != "Written By") {
                authors = authors.concat(author);
                authors = authors.concat(", ");
            }
        }

        if (authors.length() == 0) {
            throw new IllegalArgumentException();
        }
        authors = authors.substring(0, authors.length()-2);

        String summary = storyJSON.get("summary") != JsonNull.INSTANCE ? storyJSON.get("summary").getAsString() : String.valueOf(JsonNull.INSTANCE);

        String excerpt = summary.substring(0,Math.min(200, summary.length()));
        if (excerpt.length() == 200) {
            int cutoff = excerpt.lastIndexOf(" ");
            excerpt = excerpt.substring(0, cutoff) + " ...";
        }

        excerpt = excerpt.replace("\"", "'");
        excerpt = excerpt.replace("'", "&apos;");

        excerpt = excerpt.replace("\"", "'");
        summary = summary.replace("'", "&apos;");

        String media = storyJSON.get("media") != JsonNull.INSTANCE ? storyJSON.get("media").getAsString() : String.valueOf(JsonNull.INSTANCE);
        media = media.replace("\"", "&quot;");
        media = media.replace("'", "&apos;");

        if (excerpt.equals("null") || summary.equals(String.valueOf(JsonNull.INSTANCE)) || media.equals(String.valueOf(JsonNull.INSTANCE))) {
            throw new IllegalArgumentException();
        }

        String country = storyJSON.get("country") != JsonNull.INSTANCE ? storyJSON.get("country").getAsString() : String.valueOf(JsonNull.INSTANCE);
        String language = storyJSON.get("language") != JsonNull.INSTANCE ? storyJSON.get("language").getAsString() : String.valueOf(JsonNull.INSTANCE);
        String title = storyJSON.get("title") != JsonNull.INSTANCE ? storyJSON.get("title").getAsString() : String.valueOf(JsonNull.INSTANCE);
        title = title.replace("\"", "&quot;");
        title = title.replace("'", "&apos;");

        Date publishedDate = null;
        try {
            publishedDate = dateFormatter.parse(storyJSON.get("published_date").getAsString());
        } catch (ParseException e) {
            throw new IllegalArgumentException();
        }
        String link = storyJSON.get("link") != JsonNull.INSTANCE ? storyJSON.get("link").getAsString() : String.valueOf(JsonNull.INSTANCE);
        link = link.replace("\"", "&quot;");
        link = link.replace("'", "&apos;");
        String source = storyJSON.get("clean_url") != JsonNull.INSTANCE ? storyJSON.get("clean_url").getAsString() : String.valueOf(JsonNull.INSTANCE);

        String id = storyJSON.get("_id") != JsonNull.INSTANCE ? storyJSON.get("_id").getAsString() : String.valueOf(JsonNull.INSTANCE);
        return new Story(id, country, language, title, authors, publishedDate, link, excerpt, source, summary, media, 0, 0);

    }


}
