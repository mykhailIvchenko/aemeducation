package com.testaem.aem.core.models.impl;

import com.testaem.aem.core.models.CustomHeader;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, adapters = CustomHeader.class)
public class CustomHeaderImpl implements CustomHeader {

    @ValueMapValue(name = "image")
    private String imagePath;
    @ValueMapValue
    private String text;


    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getImagePath() {
        return this.imagePath;
    }

}
