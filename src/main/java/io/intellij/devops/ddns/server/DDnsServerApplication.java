package io.intellij.devops.ddns.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * DDnsServerApplication
 *
 * @author tech@intellij.io
 */
@SpringBootApplication
public class DDnsServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DDnsServerApplication.class, args);
    }
}
