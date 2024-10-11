package io.intellij.devops.ddns.server.entites;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DDnsRequest
 *
 * @author tech@intellij.io
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Data
public class DDnsRequest {
    private String domainName;
    private String rr;
    private String ipv4;
}
