package io.intellij.devops.ddns.server.config.properties;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * DdnsProperties
 *
 * @author tech@intellij.io
 */
@ToString
@Data
@ConfigurationProperties(prefix = "ddns")
public class DdnsProperties {
    // domain access control list
    private List<String> domainAcl;
    private List<String> excludeRrList;
    private String testSubDomain;
}
