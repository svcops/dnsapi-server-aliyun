package io.intellij.devops.server.dnsapi.controller;

import io.intellij.devops.server.dnsapi.config.properties.DnsApiProperties;
import io.intellij.devops.server.dnsapi.entities.Result;
import io.intellij.devops.server.dnsapi.entities.ddns.DDnsRequest;
import io.intellij.devops.server.dnsapi.entities.ddns.DDnsResponse;
import io.intellij.devops.server.dnsapi.services.DDnsService;
import io.intellij.devops.server.dnsapi.utils.DomainNameUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.intellij.devops.server.dnsapi.utils.HttpServletRequestUtils.getRequestIp;
import static io.intellij.devops.server.dnsapi.utils.HttpServletRequestUtils.isValidIPv4;

/**
 * DDnsController
 *
 * @author tech@intellij.io
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/ddns")
@Slf4j
public class DDnsController {
    private final DDnsService dDnsService;
    private final DnsApiProperties dnsApiProperties;

    /**
     * Invokes the dynamic DNS (DDNS) service with the provided request details.
     * Validates the domain and IPv4 format before processing the request.
     * Logs the request details and returns the response from the DDNS service.
     *
     * @param request the DDnsRequest object containing the domain name, resource record,
     *                and IPv4 address for the DDNS operation
     * @return a Result object containing a DDnsResponse with the outcome of the DDNS operation
     */
    @PostMapping("/invoke")
    public Result<DDnsResponse> invoke(@RequestBody DDnsRequest request) {
        validateDomain(request.getDomainName(), request.getRr());
        validateIpv4(request.getIpv4());

        log.info("ddns|domainName={}|rr={}|ipv4={}", request.getDomainName(), request.getRr(), request.getIpv4());
        return Result.ok(dDnsService.ddns(request.getDomainName(), request.getRr(), request.getIpv4()));
    }

    /**
     * Invokes the dynamic DNS (DDNS) service by automatically extracting the IPv4 address
     * from the provided HttpServletRequest. Combines the extracted IP with the domain name
     * and resource record from the provided DDnsRequest to perform the DDNS operation.
     *
     * @param request the DDnsRequest object containing the domain name and resource record for the operation
     * @param httpServletRequest the HttpServletRequest object from which the client IPv4 address is extracted
     * @return a Result object containing a DDnsResponse with the outcome of the DDNS operation
     */
    @PostMapping(value = {"/invokeGetIpAutomatic", "/invokeGetIpByHttpServletRequest"})
    public Result<DDnsResponse> invokeGetIpByHttpServletRequest(@RequestBody DDnsRequest request, HttpServletRequest httpServletRequest) {
        return invoke(DDnsRequest.builder().domainName(request.getDomainName()).rr(request.getRr()).ipv4(getRequestIp(httpServletRequest)).build());
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
