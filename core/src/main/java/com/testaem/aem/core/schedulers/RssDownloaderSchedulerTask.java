package com.testaem.aem.core.schedulers;

import com.testaem.aem.core.services.NewsReaderService;
import com.testaem.aem.core.services.ResourceBuilderService;
import com.testaem.aem.core.utills.parser.Feed;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Component(immediate = true, service = Runnable.class)
@Designate(ocd = RssDownloaderSchedulerTask.RssNewsSchedulerConfig.class)
public class RssDownloaderSchedulerTask implements Runnable {

    private static final String URL = "https://mangaplanet.com/feed/";
    private static final Logger LOGGER = LoggerFactory.getLogger(RssDownloaderSchedulerTask.class);

    @Reference
    private Scheduler scheduler;
    @Reference
    private NewsReaderService newsReaderService;
    @Reference
    private ResourceBuilderService<Feed> resourceBuilderService;
    private int schedulerID;



    @ObjectClassDefinition(name = "A scheduled news reader task",
            description = "schedule job for download RSS feed from the web resources")
    public @interface RssNewsSchedulerConfig {
        @AttributeDefinition(name = "Scheduler name", description = "Scheduler name", type = AttributeType.STRING)
        public String schedulerName() default "Rss downloader scheduler";

        @AttributeDefinition(name = "Cron-job expression")
        String scheduler_expression() default "*/1 * * * *";

        @AttributeDefinition(name = "Concurrent task",
                description = "Whether or not to schedule this task concurrently")
        boolean scheduler_concurrent() default false;

        /**
         * serviceEnabled
         *
         * @return serviceEnabled
         */
        @AttributeDefinition(name = "Enabled", description = "Enable Scheduler", type = AttributeType.BOOLEAN)
        boolean serviceEnabled() default true;
    }

    @Activate
    protected void activate(final RssNewsSchedulerConfig config) {

        schedulerID = config.schedulerName().hashCode();
        addScheduler(config);
        LOGGER.info("Scheduler Job '{}' activated", schedulerID);

        LOGGER.info("expression: " + config.scheduler_expression() + "\n " +
                "Enabled: " + config.serviceEnabled() +"\n " +
                "Concurrent mode:  " + config.scheduler_concurrent());
    }

    @Deactivate
    protected void deactivate(RssNewsSchedulerConfig config) {
        removeScheduler();
        LOGGER.info("Scheduler Job '{}' deactivated", schedulerID);
    }

    @Modified
    protected void modified(RssNewsSchedulerConfig config) {
        removeScheduler();
        schedulerID = config.schedulerName().hashCode(); // update schedulerID
        addScheduler(config);
    }

    private void addScheduler(RssNewsSchedulerConfig config) {
        if (config.serviceEnabled()) {
            ScheduleOptions sopts = scheduler.EXPR(config.scheduler_expression());
            sopts.name(String.valueOf(schedulerID));
            sopts.canRunConcurrently(false);
            scheduler.schedule(this, sopts);
            LOGGER.info("Scheduler added succesfully");
        } else {
            LOGGER.info("RssDownloaderSchedulerTask is Disabled, no scheduler job created");
        }
    }

    private void removeScheduler() {
        LOGGER.info("Removing Scheduler Job '{}'", schedulerID);
        scheduler.unschedule(String.valueOf(schedulerID));
    }

    @Override
    public void run() {
        Feed read = newsReaderService.read(URL);
        try {
            List<Resource> build = resourceBuilderService.build(read);
        } catch (PersistenceException e) {
            throw new RuntimeException(e);
        }

    }


}
