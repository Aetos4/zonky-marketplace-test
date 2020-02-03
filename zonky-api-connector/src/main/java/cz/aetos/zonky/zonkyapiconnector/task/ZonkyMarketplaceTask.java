package cz.aetos.zonky.zonkyapiconnector.task;


import cz.aetos.zonky.api.model.Marketplace;
import cz.aetos.zonky.zonkyapiconnector.service.ZonkyAPIService;
import cz.aetos.zonky.zonkyapiconnector.util.ZonkyDateWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class ZonkyMarketplaceTask implements Runnable {

    private static final Log logger = LogFactory.getLog(ZonkyMarketplaceTask.class);

    @Autowired
    private ZonkyAPIService zonkyAPIService;

    @Autowired
    private CacheManager cacheManager;

    @Override
    public void run() {
        logger.info("Start ZonkyMarketplaceTask ...");

        List<Marketplace> marketplaces = getNewMarketplaces();
        displayMarketplaces(marketplaces);

        logger.info("ZonkyMarketplaceTask was finished.");
    }

    private List<Marketplace> getNewMarketplaces() {

        ZonkyDateWrapper lastPublishedDate  = getLastPublishedDate();
        lastPublishedDate.addSecondsToDate(1);

        List<Marketplace> marketplaces = zonkyAPIService.getMarketplaces(lastPublishedDate);

        storeLastPublishedDate(marketplaces, lastPublishedDate);

        return marketplaces;
    }

    private void displayMarketplaces(List<Marketplace> marketplaces) {
        System.out.println("\nNové půjčky:");
        marketplaces.stream().forEach(System.out::println);
        System.out.println();
    }

    private ZonkyDateWrapper getLastPublishedDate() {
        Cache.ValueWrapper cacheWrapper = cacheManager.getCache("zonky").get("lastPublishedDate");
         if (cacheWrapper == null || cacheWrapper.get() == null || cacheWrapper.get().toString().isEmpty()) {

             ZonkyDateWrapper now = new ZonkyDateWrapper(ZonkyDateWrapper.Type.REQUEST, new Date());

             logger.info("The lastPublishedDate is null or empty. Using now=" + now.getRequestDate());
             return now;
         }

         return (ZonkyDateWrapper) cacheWrapper.get();
    }

    private void storeLastPublishedDate(List<Marketplace> marketplaces, ZonkyDateWrapper lastDate) {
        if (marketplaces.size() > 0) {
            lastDate = new ZonkyDateWrapper(ZonkyDateWrapper.Type.RESPONSE, marketplaces.get(0).getDatePublished());

        }
        cacheManager.getCache("zonky").put("lastPublishedDate", lastDate);
        logger.info("Storing lastPublishedDate=" + lastDate.getDate());
    }

}
