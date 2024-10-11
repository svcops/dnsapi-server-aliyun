package io.intellij.devops.ddns.server.sevice;

import io.intellij.devops.ddns.server.entites.DDnsResponse;

/**
 * DDnsService
 *
 * @author tech@intellij.io
 */
public interface DDnsService {
    DDnsResponse ddns(String domainName, String rr, String ipv4);
}
