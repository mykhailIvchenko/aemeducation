package com.testaem.aem.core.utills;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Supplier;

@Component(name = "sysUserResourceResolverProvider")
public class ResourceResolverProviderSupplier implements Supplier<ResourceResolver> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceResolverProviderSupplier.class);
    private static final String SYS_USER_NAME = "sys";
    @Reference
    private ResourceResolverFactory factory;

    @Override
    public ResourceResolver get() {
        try {
            return factory
                    .getServiceResourceResolver(Map.of(factory.SUBSERVICE, SYS_USER_NAME));
        } catch (LoginException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
