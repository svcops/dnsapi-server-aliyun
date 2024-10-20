package io.intellij.devops.server.dnsapi.entities.dns;

import lombok.Data;

/**
 * DnsRequest
 *
 * @author tech@intellij.io
 */
@Data
public class DnsRequest {
    private String domainName;
    private String rr;
    private String type;
    private String value;
}
