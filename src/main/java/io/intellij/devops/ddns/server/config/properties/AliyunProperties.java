package io.intellij.devops.ddns.server.config.properties;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * AliyunProperties
 *
 * @author tech@intellij.io
 */
@ToString
@Data
@ConfigurationProperties(prefix = "aliyun")
public class AliyunProperties {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
}
