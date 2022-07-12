package com.testaem.aem.core.services.impl;

import com.testaem.aem.core.services.NewsReaderService;
import com.testaem.aem.core.utills.parser.DataParser;
import com.testaem.aem.core.utills.parser.Feed;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service=NewsReaderService.class)
public class RssFeedNewsReaderService implements NewsReaderService {

    @Reference
    private DataParser rssFeedDataParser;

    @Override
    public Feed read(String url) {
        return rssFeedDataParser.parse(url);
    }
}
