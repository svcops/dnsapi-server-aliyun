package io.intellij.devops.server.dnsapi.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

/**
 * TokenInterceptor
 *
 * @author tech@intellij.io
 */
@RequiredArgsConstructor
public class TokenInterceptor implements HandlerInterceptor {
    private final String accessTokenKey;
    private final Set<String> accessTokens;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        String accessToken = request.getHeader(accessTokenKey);
        if (!accessTokens.contains(accessToken)) {
            throw new RuntimeException("access token is invalid");
        }
        return true;
    }

}
