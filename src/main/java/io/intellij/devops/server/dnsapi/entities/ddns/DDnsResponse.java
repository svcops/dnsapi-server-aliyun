package io.intellij.devops.server.dnsapi.entities.ddns;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * DDnsResponse
 *
 * @author tech@intellij.io
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class DDnsResponse {
    private Date ts;
    private Object apiResponseBody;

    private String subDomain;
    private String ipv4;

    public static DDnsResponse of(Object apiResponseBody, String subDomain, String ipv4) {
        return new DDnsResponse(new Date(), apiResponseBody, subDomain, ipv4);
    }

}
