package io.intellij.devops.server.dnsapi.services.dns;

import io.intellij.devops.server.dnsapi.config.properties.AliyunProperties;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperties;

/**
 * ClientProducer
 *
 * @author tech@intellij.io
 */
@ApplicationScoped
@Slf4j
public class ClientProducer {

    @Inject
    @ConfigProperties
    AliyunProperties aliyunProperties;

    @ApplicationScoped
    @Produces
    public ClientAdapter createClient() {
        try {
            return new ClientAdapter(aliyunProperties);
        } catch (Exception e) {
            log.error("create client occurred error", e);
            throw new RuntimeException("create client occurred error");
        }
    }

}
