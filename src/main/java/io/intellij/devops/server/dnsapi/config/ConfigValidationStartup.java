package io.intellij.devops.server.dnsapi.config;

import io.intellij.devops.server.dnsapi.config.properties.AccessTokenProperties;
import io.intellij.devops.server.dnsapi.config.properties.AliyunProperties;
import io.intellij.devops.server.dnsapi.config.properties.DnsApiProperties;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperties;

import java.util.Set;

/**
 * ConfigValidationStartup
 *
 * @author tech@intellij.io
 */
@ApplicationScoped
@Slf4j
public class ConfigValidationStartup {

    @Inject
    @ConfigProperties
    AccessTokenProperties accessTokenProperties;

    @Inject
    @ConfigProperties
    AliyunProperties aliyunProperties;

    @Inject
    @ConfigProperties
    DnsApiProperties dnsApiProperties;

    @Inject
    Validator validator;

    void onStart(@Observes StartupEvent ev) {
        validateConfig(accessTokenProperties);
        validateConfig(aliyunProperties);
        validateConfig(dnsApiProperties);
    }

    private <T> void validateConfig(T config) {
        log.info("validate config|{}", config);
        Set<ConstraintViolation<T>> violations = validator.validate(config);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<T> violation : violations) {
                sb.append(violation.getPropertyPath()).append(" - ").append(violation.getMessage()).append("\n");
            }
            throw new RuntimeException("Configuration validation failed:\n" + sb.toString());
        }
    }

    void onStop(@Observes ShutdownEvent ev) {
        log.error("The application is stopping...");
    }

}
