package com.testaem.aem.core.models.impl;

import com.testaem.aem.core.models.NewsPage;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.text.SimpleDateFormat;
import java.util.Date;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, adapters = NewsPage.class)
public class SinglePageNewsImpl implements NewsPage {

    @ValueMapValue
    private String textHTML;
    @ValueMapValue
    private String text;
    @ValueMapValue(name = "image")
    private String imagePath;
    @ValueMapValue
    private Date date;

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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return this.date == null ? simpleDateFormat.format(new Date()) : simpleDateFormat.format(date);
    }
}
