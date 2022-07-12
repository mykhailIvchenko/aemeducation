package com.testaem.aem.core.utills.path;

public enum PathConstants {

    MULTIPLE_PAGE("/content/testaem/us/en/KonohaPage"), LAYOUT_CONFIG("/conf/testaem/settings/wcm/templates/narutopage");

    final String path;
    PathConstants(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }
}
