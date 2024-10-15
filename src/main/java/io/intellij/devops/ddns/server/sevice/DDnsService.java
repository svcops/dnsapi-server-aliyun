package io.intellij.devops.ddns.server.sevice;

import io.intellij.devops.ddns.server.entites.ddns.DDnsResponse;

/**
 * DDnsService
 *
 * @author tech@intellij.io
 */
public interface DDnsService {
    String IPV4_TYPE = "A";

    DDnsResponse ddns(String domainName, String rr, String ipv4);
}
