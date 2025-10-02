package io.intellij.devops.server.dnsapi.config;

import io.intellij.devops.server.dnsapi.config.properties.AccessTokenProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashSet;

/**
 * WebConfig
 *
 * @author tech@intellij.io
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Configuration
@Profile("prod")
@Slf4j
public class WebConfig implements WebMvcConfigurer {
    private final AccessTokenProperties tokenProperties;

    @Override
    public void addInterceptors(@NotNull InterceptorRegistry registry) {
        registry.addInterceptor(
                new TokenInterceptor(tokenProperties.getAccessTokenHeaderKey(), new HashSet<>(tokenProperties.getAccessTokenList()))
        );
    }
}
