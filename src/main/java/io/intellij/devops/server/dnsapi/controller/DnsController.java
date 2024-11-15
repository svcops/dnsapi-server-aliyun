package io.intellij.devops.server.dnsapi.controller;

import com.aliyun.alidns20150109.models.AddDomainRecordRequest;
import com.aliyun.alidns20150109.models.AddDomainRecordResponse;
import com.aliyun.alidns20150109.models.AddDomainRecordResponseBody;
import com.aliyun.alidns20150109.models.DeleteSubDomainRecordsRequest;
import com.aliyun.alidns20150109.models.DeleteSubDomainRecordsResponse;
import com.aliyun.alidns20150109.models.DeleteSubDomainRecordsResponseBody;
import com.aliyun.alidns20150109.models.DescribeSubDomainRecordsRequest;
import com.aliyun.alidns20150109.models.DescribeSubDomainRecordsResponse;
import com.aliyun.alidns20150109.models.DescribeSubDomainRecordsResponseBody;
import io.intellij.devops.server.dnsapi.entities.Result;
import io.intellij.devops.server.dnsapi.entities.dns.DnsRequest;
import io.intellij.devops.server.dnsapi.services.DnsApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * DnsController
 *
 * @author tech@intellij.io
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/dns/domain")
@Slf4j
public class DnsController {
    private final DnsApiService dnsApiService;

    @PostMapping("/acl")
    public Result<List<String>> domainAccessControlList() {
        return Result.ok(dnsApiService.domainAccessControlList());
    }

    @PostMapping("/addRecord")
    public Result<AddDomainRecordResponseBody> addRecord(@RequestBody DnsRequest dnsRequest) {
        log.info("addRecord|domainName={}|rr={}|type={}|value={}", dnsRequest.getDomainName(), dnsRequest.getRr(), dnsRequest.getType(), dnsRequest.getValue());
        aclCheck(dnsRequest.getDomainName());
        AddDomainRecordResponse addDomainRecordResponse = dnsApiService.addDomainRecord(dnsRequest.getDomainName(),
                new AddDomainRecordRequest()
                        .setDomainName(dnsRequest.getDomainName())
                        .setRR(dnsRequest.getRr())
                        .setType(dnsRequest.getType())
                        .setValue(dnsRequest.getValue())
        );

        if (addDomainRecordResponse.statusCode != DnsApiService.SUCCESS_STATUS_CODE) {
            throw new RuntimeException("AddDomainRecord occurred error|statusCode={}" + addDomainRecordResponse.statusCode);
        }
        return Result.ok(addDomainRecordResponse.getBody());
    }

    @PostMapping("/getRecords")
    public Result<DescribeSubDomainRecordsResponseBody> getRecords(@RequestBody DnsRequest dnsRequest) {
        log.info("getRecords|domainName={}|rr={}", dnsRequest.getDomainName(), dnsRequest.getRr());
        aclCheck(dnsRequest.getDomainName());
        DescribeSubDomainRecordsResponse describeSubDomainRecordsResponse = dnsApiService.describeSubDomainRecords(dnsRequest.getDomainName(),
                new DescribeSubDomainRecordsRequest()
                        .setSubDomain(dnsRequest.getRr() + "." + dnsRequest.getDomainName()));

        if (describeSubDomainRecordsResponse.statusCode != DnsApiService.SUCCESS_STATUS_CODE) {
            throw new RuntimeException("DescribeSubDomainRecords occurred error|statusCode={}" + describeSubDomainRecordsResponse.statusCode);
        }

        DescribeSubDomainRecordsResponseBody body = describeSubDomainRecordsResponse.getBody();
        return Result.ok(body);
    }

    @PostMapping("/deleteRecords")
    public Result<DeleteSubDomainRecordsResponseBody> deleteRecords(@RequestBody DnsRequest dnsRequest) {
        log.info("deleteRecords|domainName={}|rr={}", dnsRequest.getDomainName(), dnsRequest.getRr());
        aclCheck(dnsRequest.getDomainName());
        DeleteSubDomainRecordsResponse response = dnsApiService.deleteSubDomainRecords(dnsRequest.getDomainName(),
                new DeleteSubDomainRecordsRequest()
                        .setDomainName(dnsRequest.getDomainName())
                        .setRR(dnsRequest.getRr())
        );

        if (response.statusCode != DnsApiService.SUCCESS_STATUS_CODE) {
            throw new RuntimeException("DeleteSubDomainRecords occurred error|statusCode={}" + response.statusCode);
        }

        DeleteSubDomainRecordsResponseBody body = response.getBody();
        return Result.ok(body);
    }

    @PostMapping("/deleteThenAddRecord")
    public Result<Map<String, Object>> deleteThenAddRecord(@RequestBody DnsRequest dnsRequest) {
        log.info("deleteThenAddRecord|domainName={}|rr={}|type={}|value={}", dnsRequest.getDomainName(), dnsRequest.getRr(), dnsRequest.getType(), dnsRequest.getValue());
        aclCheck(dnsRequest.getDomainName());
        DeleteSubDomainRecordsResponse deleteSubDomainRecordsResponse = dnsApiService.deleteSubDomainRecords(dnsRequest.getDomainName(),
                new DeleteSubDomainRecordsRequest()
                        .setDomainName(dnsRequest.getDomainName())
                        .setRR(dnsRequest.getRr())
        );

        if (deleteSubDomainRecordsResponse.statusCode != DnsApiService.SUCCESS_STATUS_CODE) {
            throw new RuntimeException("DeleteSubDomainRecords occurred error|statusCode={}" + deleteSubDomainRecordsResponse.statusCode);
        }

        AddDomainRecordResponse addDomainRecordResponse = dnsApiService.addDomainRecord(dnsRequest.getDomainName(),
                new AddDomainRecordRequest()
                        .setDomainName(dnsRequest.getDomainName())
                        .setRR(dnsRequest.getRr())
                        .setType(dnsRequest.getType())
                        .setValue(dnsRequest.getValue())
        );

        if (addDomainRecordResponse.statusCode != DnsApiService.SUCCESS_STATUS_CODE) {
            throw new RuntimeException("AddDomainRecord occurred error|statusCode={}" + addDomainRecordResponse.statusCode);
        }

        return Result.ok(
                Map.of("deleteRecords", deleteSubDomainRecordsResponse.body,
                        "addRecord", addDomainRecordResponse.body
                )
        );
    }

    private void aclCheck(String domainName) {
        List<String> domainAclList = dnsApiService.domainAccessControlList();
        if (!domainAclList.contains(domainName)) {
            throw new RuntimeException("domain access control list does not contains domainName|domainName=" + domainName);
        }
    }

}
