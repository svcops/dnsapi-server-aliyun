package io.intellij.devops.server.dnsapi.config.properties;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperties;

/**
 * AccessTokenPropertiesHolder
 * <p>
 * 改变作用域
 *
 * @author tech@intellij.io
 */
@RequestScoped
@Getter
public class AccessTokenPropertiesHolder {

    @Inject
    @ConfigProperties
    AccessTokenProperties accessTokenProperties;

}
