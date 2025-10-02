package io.intellij.devops.client.ddns.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * DDNSConfig
 *
 * @author tech@intellij.io
 */
@ConfigurationProperties(prefix = "ddns")
@Validated
@Data
public class DDNSConfig {

    /**
     * 请求的api地址
     */
    @NotBlank(message = "rootUri must not be blank")
    private String rootUri;

    /**
     * token key
     */
    @NotBlank(message = "accessTokenKey must not be blank")
    private String accessTokenKey;

    /**
     * token value
     */
    @NotBlank(message = "accessTokenValue must not be blank")
    private String accessTokenValue;

    /**
     * domain name
     */
    @NotBlank(message = "domain name must not be blank")
    private String domainName;

    /**
     * rr
     */
    @NotBlank(message = "rr must not be blank")
    private String rr;

}
