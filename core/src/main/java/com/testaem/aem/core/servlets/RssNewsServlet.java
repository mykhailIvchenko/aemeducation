package com.testaem.aem.core.servlets;

import aQute.bnd.annotation.component.Component;
import com.testaem.aem.core.services.NewsReaderService;
import com.testaem.aem.core.services.ResourceBuilderService;
import com.testaem.aem.core.utills.parser.Feed;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MethodNotSupportedException;
import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

@Component
@SlingServletResourceTypes(resourceTypes = {"/apps/resourceCreator"}, methods = "POST")
@Designate(ocd = RssNewsServlet.RssNewsServletConfig.class)
public class RssNewsServlet extends SlingAllMethodsServlet {

    private static final String URL = "https://mangaplanet.com/feed/";
    private static final Logger LOGGER = LoggerFactory.getLogger(RssNewsServlet.class);

    @Reference
    private NewsReaderService newsReaderService;
    @Reference
    private ResourceBuilderService<Feed> resourceBuilderService;
    private boolean enabled = false;

    @ObjectClassDefinition(name = "RssNewsServletConfig", description = "Configuration for news creator servlet")
    public @interface RssNewsServletConfig {
        @AttributeDefinition(
                name = "Enabled",
                description = "configure servlet mode (on/off)",
                type = AttributeType.BOOLEAN)
        boolean enabled() default false;
    }

    @Activate
    protected void activate(final RssNewsServletConfig config) {
        enabled = config.enabled();
    }

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException {
        LOGGER.debug("GET endpoint has been called. Suspended.");
        response.setStatus(405);
        throw new ServletException("GET method for the endpoint is not supported. Try to use POST");
    }

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        if (!enabled) {
            LOGGER.debug("configuration for the servlet is disabled. Access suspended.");
            response.setStatus(503);
            return;
        }

        Feed read = newsReaderService.read(URL);
        resourceBuilderService.build(read);
        response.setStatus(200);
    }

}
