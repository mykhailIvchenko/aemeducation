package com.testaem.aem.core.models;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Model(adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Mapper {

    @RequestAttribute
    private Object[] values;

    public Map<String, String> getMap() {
        if (ArrayUtils.isEmpty(values)) {
            return Collections.emptyMap();
        }
        HashMap<String, String> requestAttributes = new HashMap<>();

        for(int i = 0; i < values.length; i+=2) {
            requestAttributes.put(String.valueOf(values[i]), String.valueOf(values[i + 1]));
        }
        return requestAttributes;
    }
}
