package io.intellij.devops.ddns.server.config;

import io.intellij.devops.ddns.server.config.properties.AccessTokenProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
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
