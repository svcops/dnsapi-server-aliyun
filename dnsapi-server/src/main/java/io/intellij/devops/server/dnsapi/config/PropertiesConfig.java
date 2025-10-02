package io.intellij.devops.server.dnsapi.config;

import io.intellij.devops.server.dnsapi.config.properties.AccessTokenProperties;
import io.intellij.devops.server.dnsapi.config.properties.AliyunProperties;
import io.intellij.devops.server.dnsapi.config.properties.DnsApiProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * PropertiesConfig
 *
 * @author tech@intellij.io
 */
@EnableConfigurationProperties({
        AccessTokenProperties.class,
        AliyunProperties.class,
        DnsApiProperties.class
})
@Configuration
public class PropertiesConfig {
}