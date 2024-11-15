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

import java.util.Map;

/**
 * DnsController
 *
 * @author tech@intellij.io
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/dns/subDomain")
@Slf4j
public class DnsController {
    private final DnsApiService dnsApiService;

    @PostMapping("/addRecord")
    public Result<AddDomainRecordResponseBody> addRecord(@RequestBody DnsRequest dnsRequest) {
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
                Map.of("delete", deleteSubDomainRecordsResponse.body,
                        "add", addDomainRecordResponse.body
                )
        );
    }

}
