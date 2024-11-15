package io.intellij.devops.server.dnsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * DDnsServerApplication
 *
 * @author tech@intellij.io
 */
@SpringBootApplication
public class DnsApiServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DnsApiServerApplication.class, args);
    }
}
