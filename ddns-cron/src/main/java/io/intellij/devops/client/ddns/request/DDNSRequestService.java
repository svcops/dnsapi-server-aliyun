package io.intellij.devops.client.ddns.request;

import io.intellij.devops.client.ddns.properties.DDNSConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * DDNSRequestService
 *
 * @author tech@intellij.io
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Slf4j
public class DDNSRequestService {
    private static final String DDNS_URI = "/ddns/invokeGetIpAutomatic";

    private final RestTemplate restTemplate;
    private final DDNSConfig ddnsConfig;

    public String request() {
        var url = ddnsConfig.getRootUri() + DDNS_URI;

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setConnection("close");
        headers.set(ddnsConfig.getAccessTokenKey(), ddnsConfig.getAccessTokenValue());

        var body = Map.of(
                "domainName", ddnsConfig.getDomainName(),
                "rr", ddnsConfig.getRr()
        );

        try {
            var requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);

            String responseBody = responseEntity.getBody();
            log.info("ddns request ok={}", responseBody);
            return responseBody;
        } catch (Exception e) {
            log.error("ddns request error|{}", e.getMessage(), e);
            return e.getMessage();
        }
    }

}
