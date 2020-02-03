package cz.aetos.zonky.zonkyapiconnector.request;

import cz.aetos.zonky.api.model.Marketplace;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GETMarketplace extends ZonkyAPIRequest {

    private static final String MARKETPLACE_URL = "https://api.zonky.cz/loans/marketplace";

    private HttpHeaders headers;

    private Map<String, String> filters;

    private ResponseEntity<Marketplace[]> responseEntity;

    public GETMarketplace(ClientHttpRequestFactory requestFactory) {
        super(requestFactory);
    }

    {
        filters = new HashMap<>(20);
        headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }


    public void addXOrder(String orderValue, boolean desc) {
        String fullValue = desc ? "-" + orderValue : "" + orderValue;
        String xOrderKey = "X-Order";
        if (headers.containsKey(xOrderKey)) {
            headers.get(xOrderKey).add(fullValue);
        } else {
            headers.add(xOrderKey, fullValue);
        }
    }

    public void addFilter(String fieldName, String value, String operation) {
        String key = fieldName + "__" + operation;
        filters.put(key, value);
    }

    public List<Marketplace> sendAndReceive() {
        HttpEntity<String> httpEntity = new HttpEntity<>("body", headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getURL());
        filters.entrySet().stream().forEach((item) ->
                    builder.queryParam(item.getKey(), item.getValue())
                );

        responseEntity = this.exchange(builder.toUriString(), HttpMethod.GET, httpEntity, Marketplace[].class);

        return List.of(Objects.requireNonNull(responseEntity.getBody()));
    }

    protected String getURL() {
        return MARKETPLACE_URL;
    }

}
