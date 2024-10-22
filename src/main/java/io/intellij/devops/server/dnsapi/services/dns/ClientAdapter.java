package io.intellij.devops.server.dnsapi.services.dns;

import io.intellij.devops.server.dnsapi.config.properties.AliyunProperties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * ClientAdapter
 *
 * @author tech@intellij.io
 */
@Getter
@Slf4j
public class ClientAdapter {
    private com.aliyun.alidns20150109.Client client;

    public ClientAdapter(AliyunProperties aliyunProperties) {
        try {
            com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                    .setAccessKeyId(aliyunProperties.getAccessKeyId())
                    .setAccessKeySecret(aliyunProperties.getAccessKeySecret());

            config.endpoint = aliyunProperties.getEndpoint();
            this.client = new com.aliyun.alidns20150109.Client(config);
        } catch (Exception e) {
            log.error("create client occurred error", e);
            throw new RuntimeException("create client occurred error");
        }
    }

    public ClientAdapter() {
    }
}
