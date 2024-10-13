package io.intellij.devops.ddns.server.config.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * PropertiesConfig
 *
 * @author tech@intellij.io
 */
@EnableConfigurationProperties(value = {AliyunProperties.class, AccessTokenProperties.class, DDnsProperties.class})
@Configuration
public class PropertiesConfig {
}
