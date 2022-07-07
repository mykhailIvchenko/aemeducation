package com.testaem.aem.core.utills.trancator.html;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLTruncator  {

    private static final Pattern WORDS_PATTERN = Pattern.compile("&.*?;|<.*?>|(\\w[\\w-]*)");
    private static final Pattern TAGS_PATTERN = Pattern.compile("<(/)?([^ ]+?)(?: | .*?)?(/)?>");
    private static final List<String> HTML_FOR_SINGLETS = Arrays.asList("br", "col", "link", "base", "img", "param", "area", "hr", "input");

    private static String truncateHtmlWords(String html, int length, String end) {
        if (length <= 0) return end;

        Matcher mWords = WORDS_PATTERN.matcher(html);
        // Count non-HTML words and keep note of open tags
        int endTextPos = 0;
        int curLength = 0;
        boolean tContinue = curLength < length;
        List<String> openTags = new ArrayList<>();
        while (tContinue) {
            if (!mWords.find())
                break;
            if (mWords.group(1) != null || mWords.group().startsWith("&")) {
                // It's an actual non-HTML word
                curLength += (mWords.group(1) == null ? 1 : mWords.group(1).length()); // Html encoding is one char
                if (curLength > length) {
                    tContinue = false;
                } else {
                    endTextPos = mWords.end();
                }
            } else {
                // Check for tag
                Matcher tag = TAGS_PATTERN.matcher(mWords.group());
                if (tag.find()) {
                    String closingTag = tag.group(1);
                    // Element names are always case-insensitive
                    String tagName = tag.group(2).toLowerCase();
                    String selfClosing = tag.group(3);
                    if (closingTag != null) {

                        int i = openTags.indexOf(tagName);

                        if (i != -1) {
                            openTags = openTags.subList(i + 1, openTags.size());
                            endTextPos = mWords.end();
                        }

                    } else if (selfClosing == null && !HTML_FOR_SINGLETS.contains(tagName)) {
                        openTags.add(0, tagName);
                        endTextPos = mWords.end();
                    }
                }
            }
        }

        if (curLength <= length){
            return html;
        }

        StringBuilder out = new StringBuilder(html.substring(0, endTextPos)).append(end);
        for (String tag : openTags)
            out.append("</").append(tag).append(">");

        return out.toString();
    }

    public String truncate(String source, int limit) {
        return truncateHtmlWords(source, limit, "&nbsp;...");
    }


}
