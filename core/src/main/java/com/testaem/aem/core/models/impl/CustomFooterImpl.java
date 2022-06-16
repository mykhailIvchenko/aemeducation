package com.testaem.aem.core.models.impl;

import com.testaem.aem.core.models.CustomFooter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        adapters = CustomFooter.class
)
public class CustomFooterImpl implements CustomFooter {

    @ValueMapValue
    private String text;

    @Override
    public String getText() {
        return text;
    }
}
