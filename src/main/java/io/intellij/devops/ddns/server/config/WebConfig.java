package io.intellij.devops.ddns.server.config;

import io.intellij.devops.ddns.server.config.properties.AccessTokenProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashSet;
import java.util.List;

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
        String accessTokenHeaderKey = tokenProperties.getAccessTokenHeaderKey();
        List<String> accessTokenList = tokenProperties.getAccessTokenList();

        if (!StringUtils.hasLength(accessTokenHeaderKey)) {
            throw new RuntimeException("accessTokenHeaderKey is blank");
        }

        if (CollectionUtils.isEmpty(accessTokenList)) {
            throw new RuntimeException("accessTokenList is empty");
        }

        log.info("accessTokenHeaderKey = {}", accessTokenHeaderKey);
        log.info("accessTokenList      = {}", accessTokenList);

        registry.addInterceptor(new TokenInterceptor(accessTokenHeaderKey, new HashSet<>(accessTokenList)));
    }
}
