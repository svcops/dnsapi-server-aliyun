package io.intellij.devops.server.dnsapi.config;

import io.intellij.devops.server.dnsapi.entities.Result;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

/**
 * GlobalExceptionHandler
 *
 * @author tech@intellij.io
 */
@Provider
@Slf4j
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        log.error("catch exception|{}", e.getMessage(), e);
        return Response.ok().entity(Result.fail(e.getMessage())).build();
    }

}
