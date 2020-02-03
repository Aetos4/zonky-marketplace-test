package cz.aetos.zonky.zonkyapiconnector.request;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

abstract class ZonkyAPIRequest extends RestTemplate {

    protected abstract String getURL();

    protected ZonkyAPIRequest(ClientHttpRequestFactory requestFactory) {
        super(requestFactory);
    }

}
