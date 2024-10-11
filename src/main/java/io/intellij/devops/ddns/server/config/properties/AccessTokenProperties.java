package io.intellij.devops.ddns.server.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * AccessTokenProperties
 *
 * @author tech@intellij.io
 */
@ConfigurationProperties(prefix = "service")
@Data
public class AccessTokenProperties {
    private String accessTokenHeaderKey;
    private List<String> accessTokenList;
}
