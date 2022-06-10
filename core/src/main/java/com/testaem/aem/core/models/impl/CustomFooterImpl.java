package com.testaem.aem.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.testaem.aem.core.models.CustomFooter;
import com.testaem.aem.core.models.CustomHeader;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class ,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, adapters = CustomFooter.class)
public class CustomFooterImpl implements CustomFooter {

    static final String RESOURCE_TYPE = "testaem/components/customfooter";

    @ValueMapValue
    private String text;

    // @Override
    public String getText() {
        return text;
    }
}
