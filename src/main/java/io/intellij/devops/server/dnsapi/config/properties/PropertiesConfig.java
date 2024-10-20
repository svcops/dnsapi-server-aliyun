package io.intellij.devops.server.dnsapi.config.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * PropertiesConfig
 *
 * @author tech@intellij.io
 */
@EnableConfigurationProperties(value = {AliyunProperties.class, AccessTokenProperties.class, DnsApiProperties.class})
@Configuration
public class PropertiesConfig {
}
