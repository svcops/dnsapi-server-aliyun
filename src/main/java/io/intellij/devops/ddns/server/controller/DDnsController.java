package io.intellij.devops.ddns.server.controller;

import io.intellij.devops.ddns.server.config.properties.DDnsProperties;
import io.intellij.devops.ddns.server.entites.DDnsRequest;
import io.intellij.devops.ddns.server.entites.DDnsResponse;
import io.intellij.devops.ddns.server.entites.Result;
import io.intellij.devops.ddns.server.sevice.DDnsService;
import io.intellij.devops.ddns.server.utils.DomainNameUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.intellij.devops.ddns.server.utils.HttpServletRequestUtils.getRequestIp;
import static io.intellij.devops.ddns.server.utils.HttpServletRequestUtils.isValidIPv4;

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
    private final DDnsProperties ddnsProperties;

    @GetMapping("/")
    public String root() {
        return "Hello,DDNSServer!";
    }

    @PostMapping("/invoke")
    public Result<DDnsResponse> ddns(@RequestBody DDnsRequest request) {
        validateDomain(request.getDomainName(), request.getRr());
        validateIpv4(request.getIpv4());

        log.info("ddns|domainName={}|rr={}|ipv4={}", request.getDomainName(), request.getRr(), request.getIpv4());
        return Result.ok(dDnsService.ddns(request.getDomainName(), request.getRr(), request.getIpv4()));
    }

    @PostMapping("/invokeGetIpByServletRequest")
    public Result<DDnsResponse> ddnsGetIpByServletRequest(@RequestBody DDnsRequest request, HttpServletRequest httpServletRequest) {
        return ddns(DDnsRequest.builder().domainName(request.getDomainName()).rr(request.getRr()).ipv4(getRequestIp(httpServletRequest)).build());
    }

    private void validateDomain(String domainName, String rr) {
        Set<String> domainAclSet = new HashSet<>(ddnsProperties.getDomainAcl());
        if (!domainAclSet.contains(domainName)) {
            throw new RuntimeException("domain access control list does not contains domainName|domainName=" + domainName);
        }

        List<String> excludeRrList = ddnsProperties.getExcludeRrList();
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
