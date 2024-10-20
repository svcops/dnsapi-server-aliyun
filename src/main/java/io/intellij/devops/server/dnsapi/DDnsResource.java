package io.intellij.devops.server.dnsapi;

import io.intellij.devops.server.dnsapi.config.properties.DnsApiProperties;
import io.intellij.devops.server.dnsapi.entities.Result;
import io.intellij.devops.server.dnsapi.entities.ddns.DDnsRequest;
import io.intellij.devops.server.dnsapi.entities.ddns.DDnsResponse;
import io.intellij.devops.server.dnsapi.services.DDnsService;
import io.intellij.devops.server.dnsapi.utils.DomainNameUtils;
import io.vertx.ext.web.RoutingContext;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperties;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.intellij.devops.server.dnsapi.utils.RequestUtils.getRequestIp;
import static org.bouncycastle.util.IPAddress.isValidIPv4;

/**
 * DDnsResource
 *
 * @author tech@intellij.io
 */
@Path("/ddns")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class DDnsResource {

    @Inject
    DDnsService dDnsService;

    @Inject
    @ConfigProperties
    DnsApiProperties dnsApiProperties;

    @Context
    RoutingContext routingContext;

    @Path("/invoke")
    @POST
    public Result<DDnsResponse> invoke(DDnsRequest request) {
        validateDomain(request.getDomainName(), request.getRr());
        validateIpv4(request.getIpv4());

        log.info("ddns|domainName={}|rr={}|ipv4={}", request.getDomainName(), request.getRr(), request.getIpv4());
        return Result.ok(dDnsService.ddns(request.getDomainName(), request.getRr(), request.getIpv4()));
    }

    @Path("/invokeGetIpAutomatic")
    @POST
    public Result<DDnsResponse> invokeGetIpAutomatic(DDnsRequest request) {
        return this.invoke(DDnsRequest.builder()
                .domainName(request.getDomainName())
                .rr(request.getRr())
                .ipv4(getRequestIp(routingContext.request())).build()
        );
    }

    private void validateDomain(String domainName, String rr) {
        Set<String> domainAclSet = new HashSet<>(dnsApiProperties.getDomainAcl());
        if (!domainAclSet.contains(domainName)) {
            throw new RuntimeException("domain access control list does not contains domainName|domainName=" + domainName);
        }

        List<String> excludeRrList = dnsApiProperties.getExcludeRrList();
        Set<String> excludeRrSet = new HashSet<>(excludeRrList);

        if (excludeRrSet.contains(rr)) {
            throw new RuntimeException("request's rr has in excluded rr list");
        }

        if (!DomainNameUtils.validateRR(rr)) {
            throw new RuntimeException("The valid pattern should include alphanumeric characters and has a length between 1 and 32.");
        }
    }

    private void validateIpv4(String ipv4) {
        if (!isValidIPv4(ipv4)) {
            throw new RuntimeException("invalid ipv4|ip=" + ipv4);
        }
    }

}
