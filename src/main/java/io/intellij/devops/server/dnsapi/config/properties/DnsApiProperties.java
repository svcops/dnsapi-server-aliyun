package io.intellij.devops.server.dnsapi.config.properties;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.eclipse.microprofile.config.inject.ConfigProperties;

import java.util.List;

/**
 * DDnsProperties
 *
 * @author tech@intellij.io
 */
@ToString
@Data
@ConfigProperties(prefix = "dns-api")
public class DnsApiProperties {
    // domain access control list
    @NotEmpty(message = "domain acl must not be empty collection")
    private List<String> domainAcl;

    @NotNull(message = "excludeRrList must not empty collection")
    private List<String> excludeRrList;

}
