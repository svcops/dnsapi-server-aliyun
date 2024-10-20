package io.intellij.devops.server.dnsapi.config.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.ToString;
import org.eclipse.microprofile.config.inject.ConfigProperties;

import java.util.List;

/**
 * AccessTokenProperties
 *
 * @author tech@intellij.io
 */
@ToString
@Data
@ConfigProperties(prefix = "service")
public class AccessTokenProperties {

    @NotBlank(message = "accessTokenHeaderKey must not be blank")
    private String accessTokenHeaderKey;

    @NotEmpty(message = "accessTokenList must not be empty collection")
    private List<String> accessTokenList;

}
