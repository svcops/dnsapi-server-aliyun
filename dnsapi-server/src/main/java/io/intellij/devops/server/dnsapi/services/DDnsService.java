package io.intellij.devops.server.dnsapi.services;

import io.intellij.devops.server.dnsapi.entities.ddns.DDnsResponse;

/**
 * DDnsService
 *
 * @author tech@intellij.io
 */
public interface DDnsService {
    String IPV4_TYPE = "A";

    DDnsResponse ddns(String domainName, String rr, String ipv4);
}
