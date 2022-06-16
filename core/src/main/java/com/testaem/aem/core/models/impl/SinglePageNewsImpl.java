package com.testaem.aem.core.models.impl;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.testaem.aem.core.models.NewsPage;
import com.testaem.aem.core.utills.impl.HTMLTruncator;
import com.testaem.aem.core.utills.Truncator;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Model(
        adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        adapters = NewsPage.class
)
public class SinglePageNewsImpl implements NewsPage {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Truncator htmlTruncator = new HTMLTruncator();

    @ValueMapValue
    private String textHTML;

    @ValueMapValue
    private String text;

    @ValueMapValue(name = "image")
    private String imagePath;

    @ValueMapValue
    private String truncatedText;

    @ValueMapValue
    private Date date;

    @Self
    private SlingHttpServletRequest request;

    public static String truncate1(String input, int size) {
        if (input.length() < size) return input;

        int pos = input.lastIndexOf('>', size);
        int pos2 = input.indexOf('<', pos);

        if (pos2 < 0 || pos2 >= size) {
            return input.substring(0, size);
        } else {
            return input.substring(0, pos2);
        }
    }

    private static String truncate(String input, int size) {
        if (input.length() < size) return input;

        int lastTagStart = 0;
        boolean inString = false;
        boolean inTag = false;

        for (int pos = 0; pos < size; pos++) {
            switch (input.charAt(pos)) {
                case '<':
                    if (!inString && !inTag) {
                        lastTagStart = pos;
                        inTag = true;
                    }
                    break;
                case '>':
                    if (!inString) inTag = false;
                    break;
                case '\"':
                    if (inTag) inString = !inString;
                    break;
            }
        }
        if (!inTag) lastTagStart = size;
        return input.substring(0, lastTagStart);
    }

    @PostConstruct
    private void init() {

        truncatedText = htmlTruncator.truncate(textHTML, 20);
    }

    @Override
    public String getTextHTML() {
        return this.textHTML;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public String getImagePath() {
        return this.imagePath;
    }

    @Override
    public String getDate() {
        return this.date == null ? simpleDateFormat.format(new Date()) : simpleDateFormat.format(date);
    }

    public String getPagePath() {

        List<String> selectors = Arrays.asList(request.getRequestPathInfo().getSelectors());

        boolean fullPage = selectors.contains("fullPage");

        PageManager pageManager = request.getResourceResolver().adaptTo(PageManager.class);
        Page containingPage = pageManager.getContainingPage(request.getResource());
        ;
        Page landingPage = containingPage.getParent(2);

        String path = fullPage ? containingPage.getPath() : landingPage.getPath();
        return path.concat(".html");
    }

    @Override
    public String getTruncatedText() {
        return this.truncatedText;
    }
}
