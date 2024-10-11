package io.intellij.devops.ddns.server.config;

import io.intellij.devops.ddns.server.config.properties.AccessTokenProperties;
import io.intellij.devops.ddns.server.config.properties.AliyunProperties;
import io.intellij.devops.ddns.server.config.properties.DdnsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * PropertiesConfig
 *
 * @author tech@intellij.io
 */
@EnableConfigurationProperties(value = {AliyunProperties.class, AccessTokenProperties.class, DdnsProperties.class})
@Configuration
public class PropertiesConfig {
}
