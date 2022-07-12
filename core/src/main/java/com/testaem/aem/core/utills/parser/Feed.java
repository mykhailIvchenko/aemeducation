package com.testaem.aem.core.utills.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Feed {

    private final String title;
    private final String link;
    private final String description;
    private final String language;
    private final String copyright;
    private final String pubDate;

    private final List<FeedMessage> entries = new ArrayList<>();

    public Feed(String title, String link, String description, String language,
                String copyright, String pubDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.language = language;
        this.copyright = copyright;
        this.pubDate = pubDate;
    }

    private Feed(FeedBuilder builder) {
        this.title = builder.title;
        this.description = builder.description;
        this.link = builder.link;

        this.language = builder.language;
        this.copyright = builder.copyright;
        this.pubDate = builder.pubDate;
    }

    public List<FeedMessage> getMessages() {
        return entries;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getPubDate() {
        return pubDate;
    }

    @Override
    public String toString() {
        return "Feed [copyright=" + copyright + ", description=" + description
                + ", language=" + language + ", link=" + link + ", pubDate="
                + pubDate + ", title=" + title + "]";
    }

    public static class FeedBuilder {
        private final String title;
        private final String description;
        private final String link;

        private String language;
        private String copyright;
        private String pubDate;


        public FeedBuilder(String title, String description, String link) {

            if (Objects.isNull(title) || Objects.isNull(description) || Objects.isNull(link)) {
                throw new IllegalArgumentException("title, description and link cannot be null");
            }

            this.title = title;
            this.description = description;
            this.link = link;
        }

        public FeedBuilder language(String language) {
            this.language = language;
            return this;
        }
        public FeedBuilder copyright(String copyright) {
            this.language = copyright;
            return this;
        }
        public FeedBuilder pubDate(String pubDate) {
            this.language = pubDate;
            return this;
        }

        public Feed build() {
            return new Feed(this);
        }



    }

}
