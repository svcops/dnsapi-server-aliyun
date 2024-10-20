package io.intellij.devops.server.dnsapi.config;

import io.intellij.devops.server.dnsapi.config.properties.AccessTokenProperties;
import io.intellij.devops.server.dnsapi.config.properties.AccessTokenPropertiesHolder;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * RequestTokenValidatorFilter
 *
 * @author tech@intellij.io
 */
@Provider
public class RequestTokenValidatorFilter implements ContainerRequestFilter {

    @Inject
    AccessTokenPropertiesHolder holder;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        AccessTokenProperties accessTokenProperties = holder.getAccessTokenProperties();

        String accessToken = requestContext.getHeaderString(accessTokenProperties.getAccessTokenHeaderKey());

        Set<String> tokenSet = new HashSet<>(accessTokenProperties.getAccessTokenList());

        if (!tokenSet.contains(accessToken)) {
            throw new RuntimeException("invalid token");
        }

    }
}
