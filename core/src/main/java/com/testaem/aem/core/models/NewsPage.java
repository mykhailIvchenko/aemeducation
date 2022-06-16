package com.testaem.aem.core.models;

public interface NewsPage {
    String getTextHTML();
    String getText();
    String getImagePath();
    String getDate();

    String getPagePath();

    String getTruncatedText();
}
