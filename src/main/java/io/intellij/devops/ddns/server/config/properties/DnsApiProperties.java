package io.intellij.devops.ddns.server.config.properties;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * DDnsProperties
 *
 * @author tech@intellij.io
 */
@ToString
@Data
@ConfigurationProperties(prefix = "dns-api")
@Validated
public class DnsApiProperties {
    // domain access control list
    @NotEmpty(message = "domain acl must not be empty collection")
    private List<String> domainAcl;

    @NotNull(message = "excludeRrList must not empty collection")
    private List<String> excludeRrList;

}
