package io.intellij.devops.server.dnsapi.config.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.ToString;
import org.eclipse.microprofile.config.inject.ConfigProperties;

/**
 * AliyunProperties
 *
 * @author tech@intellij.io
 */
@ToString
@Data
@ConfigProperties(prefix = "aliyun")
public class AliyunProperties {
    @NotBlank(message = "endpoint must not be blank")
    private String endpoint;

    @NotEmpty(message = "accessKeyId must not be blank")
    private String accessKeyId;

    @NotEmpty(message = "accessKeySecret must not be blank")
    private String accessKeySecret;
}
