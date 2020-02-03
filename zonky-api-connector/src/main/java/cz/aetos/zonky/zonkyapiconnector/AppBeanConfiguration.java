package cz.aetos.zonky.zonkyapiconnector;

import cz.aetos.zonky.zonkyapiconnector.log.LoggingInterceptor;
import cz.aetos.zonky.zonkyapiconnector.request.GETMarketplace;
import cz.aetos.zonky.zonkyapiconnector.service.ZonkyAPIService;
import cz.aetos.zonky.zonkyapiconnector.task.ZonkyMarketplaceTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Configuration
public class AppBeanConfiguration {

    private static final Log logger = LogFactory.getLog(ZonkyAPIService.class);

    @Autowired
    private ConfigurableApplicationContext ctx;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(1);
        threadPoolTaskScheduler.setThreadNamePrefix("ZonkyTaskScheduler");
        threadPoolTaskScheduler.initialize();

        ZonkyMarketplaceTask task = ctx.getBean(ZonkyMarketplaceTask.class);
        long fixDelay = TimeUnit.MINUTES.toMillis(5);
        threadPoolTaskScheduler.scheduleWithFixedDelay(task, fixDelay);

        logger.info("ZonkyMarketplaceTask was set with fix delay= " + fixDelay);
        return threadPoolTaskScheduler;
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public GETMarketplace createGETMarketplace(LoggingInterceptor loggingInterceptor) {
        logger.info("Creating a GETMarketplace prototype bean...");
        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());

        GETMarketplace restTemplate = new GETMarketplace(factory);
        restTemplate.setInterceptors(Collections.singletonList(loggingInterceptor));

        logger.info("The GETMarketplace prototype bean was created. [" + restTemplate + "]");
        return restTemplate;
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("zonky");
    }

}
