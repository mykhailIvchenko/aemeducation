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

    private final Logger logger = LoggerFactory.getLogger(ResourceResolverProviderSupplier.class);

    @Reference
    private ResourceResolverFactory factory;
    private static final String SYS_USER_NAME = "sys";

    public ResourceResolverProviderSupplier() {
    }

    @Override
    public ResourceResolver get() {
        try {
            return factory
                    .getServiceResourceResolver(Map.of(factory.SUBSERVICE, SYS_USER_NAME));
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
    }
}
