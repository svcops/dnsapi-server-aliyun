package io.intellij.devops.ddns.server.config.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * AliyunProperties
 *
 * @author tech@intellij.io
 */
@ToString
@Data
@ConfigurationProperties(prefix = "aliyun")
@Validated
public class AliyunProperties {
    @NotBlank(message = "endpoint must not be blank")
    private String endpoint;

    @NotEmpty(message = "accessKeyId must not be blank")
    private String accessKeyId;

    @NotEmpty(message = "accessKeySecret must not be blank")
    private String accessKeySecret;
}
