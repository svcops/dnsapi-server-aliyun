package io.intellij.devops.client.ddns.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * PropertiesConfig
 *
 * @author tech@intellij.io
 */
@EnableConfigurationProperties(value = {DDNSConfig.class})
@Configuration
public class PropertiesConfig {
}
