package io.intellij.devops.server.dnsapi.config.properties;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.util.List;

/**
 * AliyunProperties
 *
 * @author tech@intellij.io
 */
@ToString
@Data
@ConfigurationProperties(prefix = "aliyun")
@Validated
@Slf4j
public class AliyunProperties {
    @NotBlank(message = "endpoint must not be blank")
    private String endpoint;

    @NotEmpty(message = "accessKeyList must not empty collection")
    private List<AccessKey> accessKeyList;

    public void setAccessKeyList(String accessKeyListJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.accessKeyList = objectMapper.readValue(accessKeyListJson, new TypeReference<>() {
        });
    }

    @ToString
    @Data
    public static class AccessKey {
        private String id;
        private String secret;
    }
}
