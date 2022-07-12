package com.testaem.aem.core.services.impl;

import com.adobe.aem.formsndocuments.util.FMConstants;
import com.adobe.aemds.guide.utils.JcrResourceConstants;
import com.adobe.aemds.guide.utils.NodeStructureUtils;
import com.adobe.cq.social.scf.community.CommunitySiteTranslationConstants;
import com.adobe.cq.social.srp.internal.AbstractSchemaMapper;
import com.day.cq.commons.jcr.JcrConstants;
import com.testaem.aem.core.services.ResourceBuilderService;
import com.testaem.aem.core.utills.parser.Feed;
import com.testaem.aem.core.utills.parser.FeedMessage;
import com.testaem.aem.core.utills.path.PathConstants;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component(name = "rssNewsResourceBuilderService", service = ResourceBuilderService.class)
public class RssNewsResourceBuilderService implements ResourceBuilderService<Feed> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RssNewsResourceBuilderService.class);
    @Reference
    private Supplier<ResourceResolver> sysUserResourceResolverProvider;
    private ResourceResolver resourceResolver;


    @Override
    public List<Resource> build(Feed feed) {

        resourceResolver = sysUserResourceResolverProvider.get();

        Resource aggregatorPage = resourceResolver.getResource(PathConstants.MULTIPLE_PAGE);

        if (Objects.isNull(aggregatorPage)) {
            return null;
        }

        List<FeedMessage> hotNews = feed.getMessages()
                .stream()
                .filter(item -> Objects.isNull(aggregatorPage.getChild(item.getGuid())))
                .collect(Collectors.toList());


        return hotNews.stream()
                .map(news -> createPage(news, aggregatorPage))
                .collect(Collectors.toList());
    }

    private Resource createPage(FeedMessage message, Resource parentResource) {
        resourceResolver = sysUserResourceResolverProvider.get();
        Resource contentPage;
        LOGGER.info("Creating page with guid: " + message.getGuid());
        try {
            Resource page = resourceResolver.create(parentResource, message.getGuid(), prepareParamMap(Type.PAGE, message));
            Resource content = resourceResolver.create(page, JcrConstants.JCR_CONTENT, prepareParamMap(Type.CONTENT, message));
            Resource root = resourceResolver.create(content, "root", prepareParamMap(Type.CONTAINER_NODE, message));
            Resource container = resourceResolver.create(root, "container", prepareParamMap(Type.CONTAINER_NODE, message));
            contentPage = resourceResolver.create(container, "contentpage", prepareParamMap(Type.CONTENT_PAGE, message));
            LOGGER.info("page with id = " + message.getGuid() + " has been created");
        } catch (PersistenceException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return contentPage;
    }

    private Map<String, Object> prepareParamMap(Type type, FeedMessage message) {
        switch (type) {
            case PAGE:
                return Map.of(JcrConstants.JCR_PRIMARYTYPE, FMConstants.CQ_PAGE_NODETYPE);

            case CONTENT:
                return Map.of(
                        FMConstants.CQ_TEMPLATE_NODETYPE, PathConstants.LAYOUT_CONFIG,
                        JcrConstants.JCR_PRIMARYTYPE, JcrResourceConstants.CQ_PAGE_CONTENT,
                        JcrConstants.JCR_TITLE, message.getTitle(),
                        AbstractSchemaMapper.CQ_RESOURCE_TYPE, "testaem/components/page"
                );

            case CONTAINER_NODE:
            case ROOT_NODE:
                return Map.of(
                        JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED,
                        NodeStructureUtils.LAYOUT_NODENAME, "responsiveGrid",
                        AbstractSchemaMapper.CQ_RESOURCE_TYPE, "testaem/components/container"
                );
            case CONTENT_PAGE:
                return Map.of(
                        JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED,
                        FMConstants.PROPERTYNAME_TEXT_IS_RICH, CommunitySiteTranslationConstants.TRUE_STRING,
                        AbstractSchemaMapper.CQ_RESOURCE_TYPE, "testaem/components/singlenewspage",
                        "text", message.getTitle(),
                        "textHTML", message.getDescription(),
                        "image", message.getImg()
                );

        }
        return new HashMap<>();
    }

   private enum Type {
        PAGE, CONTENT, CONTAINER_NODE, ROOT_NODE, CONTENT_PAGE
    }
}
