package cz.aetos.zonky.zonkyapiconnector.service;

import cz.aetos.zonky.api.model.Marketplace;
import cz.aetos.zonky.zonkyapiconnector.request.GETMarketplace;
import cz.aetos.zonky.zonkyapiconnector.util.ZonkyDateWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZonkyAPIService {

    private static final Log logger = LogFactory.getLog(ZonkyAPIService.class);

    @Autowired
    private ConfigurableApplicationContext ctx;

    public List<Marketplace> getMarketplaces(ZonkyDateWrapper fromDatePublished) {
        logger.info("Call getMarketplaces, fromDatePublished=" + fromDatePublished);
        GETMarketplace getMarketplace = ctx.getBean(GETMarketplace.class);

        getMarketplace.addXOrder("datePublished", true);
        getMarketplace.addFilter("datePublished", fromDatePublished.getRequestDate(), "gt");

        return getMarketplace.sendAndReceive();
    }

}
