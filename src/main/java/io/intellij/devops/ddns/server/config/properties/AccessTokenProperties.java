package io.intellij.devops.ddns.server.config.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * AccessTokenProperties
 *
 * @author tech@intellij.io
 */
@ConfigurationProperties(prefix = "service")
@Validated
@Data
public class AccessTokenProperties {
    @NotBlank(message = "accessTokenHeaderKey must not be blank")
    private String accessTokenHeaderKey;

    @NotEmpty(message = "accessTokenList must not be empty collection")
    private List<String> accessTokenList;
}
