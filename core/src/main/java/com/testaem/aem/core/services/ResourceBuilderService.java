package com.testaem.aem.core.services;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;

import java.util.List;

public interface ResourceBuilderService<T> {
    List<Resource> build(T source) throws PersistenceException;
}
