package com.testaem.aem.core.services;

import com.testaem.aem.core.utills.parser.Feed;

public interface NewsReaderService {

    Feed read(String url);
}
