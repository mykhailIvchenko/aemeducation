package com.testaem.aem.core.models.impl;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.testaem.aem.core.models.NewsPage;
import com.testaem.aem.core.utills.impl.HTMLTruncator;
import org.apache.commons.lang3.StringUtils;
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
    private final HTMLTruncator htmlTruncator = new HTMLTruncator();

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

        boolean isFromGrid = selectors.contains("fromGrid");

        PageManager pageManager = request.getResourceResolver().adaptTo(PageManager.class);

        Page containingPage = pageManager.getContainingPage(request.getResource());

        String suffix = request.getRequestPathInfo().getSuffix();

        Page landingPage = null;

        if(!StringUtils.isEmpty(suffix)) {
            landingPage = pageManager.getContainingPage(suffix);
        }

        String path = isFromGrid  ? containingPage.getPath() : landingPage.getPath();

        return path.concat(".html");
    }

    @Override
    public String getTruncatedText() {
        return this.truncatedText;
    }

    @Override
    public String getSuffix() {
        return request.getAttribute("parentPage") != null ?
                String.valueOf(request.getAttribute("parentPage")) : StringUtils.EMPTY;

    }
    @PostConstruct
    private void init() {
        if(!StringUtils.isEmpty(textHTML)) {
            truncatedText = htmlTruncator.truncate(textHTML, 20);
        }
    }
}
