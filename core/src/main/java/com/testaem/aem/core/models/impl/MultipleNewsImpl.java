package com.testaem.aem.core.models.impl;

import com.testaem.aem.core.models.MultipleNews;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Model(
        adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        adapters = MultipleNews.class
)
public class MultipleNewsImpl implements MultipleNews {

    private List<String> resourcePaths;
    @SlingObject
    private ResourceResolver resourceResolver;
    @Self
    private SlingHttpServletRequest request;

    @Override
    public List<String> getResourcesPaths() {

        int offset = StringUtils.isNumeric(request.getParameter("offset")) ?
                Integer.parseInt(request.getParameter("offset")) : 0;

        return CollectionUtils.isEmpty(resourcePaths) ? Collections.emptyList() : resourcePaths.stream()
                .skip(offset)
                .limit(1)
                .collect(Collectors.toList());
    }

    @PostConstruct
    private void init() {
        Resource resource = resourceResolver.getResource("/content/testaem/us/en/KonohaPage");

        if (resource == null) {
            return;
        }

        resourcePaths = StreamSupport.stream(resource.getChildren().spliterator(), false)
                .map(Resource::getPath)
                .filter(r -> !r.contains("jcr:content"))
                .collect(Collectors.toList());
    }
}
