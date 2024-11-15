package io.intellij.devops.server.dnsapi.entities.dns;

import io.intellij.devops.server.dnsapi.entities.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * DnsRequest
 *
 * @author tech@intellij.io
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class DnsRequest extends PageRequest {
    private String domainName;
    private String rr;
    private String type;
    private String value;

    // 域名记录关键词搜索
    private String valueKeyWord;
    private String rrKeyWord;
}
