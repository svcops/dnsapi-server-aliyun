package io.intellij.devops.server.dnsapi.controller;

import com.aliyun.alidns20150109.models.AddDomainRecordRequest;
import com.aliyun.alidns20150109.models.AddDomainRecordResponse;
import com.aliyun.alidns20150109.models.AddDomainRecordResponseBody;
import com.aliyun.alidns20150109.models.DeleteSubDomainRecordsRequest;
import com.aliyun.alidns20150109.models.DeleteSubDomainRecordsResponse;
import com.aliyun.alidns20150109.models.DeleteSubDomainRecordsResponseBody;
import com.aliyun.alidns20150109.models.DescribeDomainRecordsRequest;
import com.aliyun.alidns20150109.models.DescribeDomainRecordsResponse;
import com.aliyun.alidns20150109.models.DescribeDomainRecordsResponseBody;
import com.aliyun.alidns20150109.models.DescribeSubDomainRecordsRequest;
import com.aliyun.alidns20150109.models.DescribeSubDomainRecordsResponse;
import com.aliyun.alidns20150109.models.DescribeSubDomainRecordsResponseBody;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.intellij.devops.server.dnsapi.entities.PageResult;
import io.intellij.devops.server.dnsapi.entities.Result;
import io.intellij.devops.server.dnsapi.entities.dns.DnsRequest;
import io.intellij.devops.server.dnsapi.services.DnsApiService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    /**
     * Retrieves the access control list (ACL) of domains.
     *
     * @return a {@code Result} object containing a list of domain access control entries as strings.
     */
    @PostMapping("/acl")
    public Result<List<String>> domainAccessControlList() {
        return Result.ok(dnsApiService.domainAccessControlList());
    }

    /**
     * Retrieves a paginated list of simplified domain records based on the specified DNS request.
     *
     * @param dnsRequest the DNS request containing domainName, pagination details, and optional keyword filters
     *                   to search based on RR (Resource Record) or value.
     * @return a {@code PageResult} containing a list of simplified domain records along with pagination metadata
     *         such as page number, page size, and the total count of records.
     */
    @PostMapping("/getDomainRecords")
    public PageResult<List<SimplifyDomainRecord>> getDomainRecords(@RequestBody DnsRequest dnsRequest) {
        long pageNumber = Objects.isNull(dnsRequest.getPageNumber()) ? 1 : dnsRequest.getPageNumber();
        long pageSize = Objects.isNull(dnsRequest.getPageSize()) ? 10 : dnsRequest.getPageSize();
        log.info("getDomainRecords|domainName={}|pageNumber={}|pageSize={}|rrKeyWord={}|valueKeyWord={}", dnsRequest.getDomainName(), pageNumber, pageSize, dnsRequest.getRrKeyWord(), dnsRequest.getValueKeyWord());
        this.aclCheck(dnsRequest.getDomainName());
        DescribeDomainRecordsRequest describeDomainRecordsRequest = new DescribeDomainRecordsRequest()
                .setDomainName(dnsRequest.getDomainName())
                .setPageNumber(pageNumber)
                .setPageSize(pageSize);

        if (StringUtils.hasLength(dnsRequest.getRrKeyWord())) {
            describeDomainRecordsRequest.setRRKeyWord(dnsRequest.getRrKeyWord());
        }

        if (StringUtils.hasLength(dnsRequest.getValueKeyWord())) {
            describeDomainRecordsRequest.setValueKeyWord(dnsRequest.getValueKeyWord());
        }

        DescribeDomainRecordsResponse describeDomainRecordsResponse = dnsApiService.describeDomainRecords(dnsRequest.getDomainName(), describeDomainRecordsRequest);

        if (describeDomainRecordsResponse.statusCode != DnsApiService.SUCCESS_STATUS_CODE) {
            throw new RuntimeException("DescribeDomainRecords occurred error|statusCode={}" + describeDomainRecordsResponse.statusCode);
        }

        DescribeDomainRecordsResponseBody body = describeDomainRecordsResponse.getBody();

        Long rtPageNumber = body.getPageNumber();
        Long rtPageSize = body.getPageSize();
        Long rtTotalCount = body.getTotalCount();

        return PageResult.ok(body.getDomainRecords().getRecord().stream().map(record -> SimplifyDomainRecord.builder()
                        .type(record.getType())
                        .rr(record.getRR())
                        .domainName(record.getDomainName())
                        .value(record.getValue())
                        .ttl(record.getTTL())
                        .status(record.getStatus())
                        .build()
                ).toList(),
                rtPageNumber,
                rtPageSize,
                rtTotalCount
        );
    }

    @JsonPropertyOrder({"type", "rr", "domainName", "value", "ttl"})
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Builder
    @Data
    public static class SimplifyDomainRecord {
        private String type;
        private String rr;
        private String domainName;
        private String value;
        private Long ttl;
        private String status;
    }

    // 下面的都是subDomainRecords的操作

    /**
     * Adds a new DNS record based on the provided request data.
     *
     * @param dnsRequest The DNS request object containing the domain name, resource record (RR), type, and value for the DNS record to be added.
     * @return A result wrapping the response body of the added domain record.
     * @throws RuntimeException If the DNS record addition operation fails due to an error in the API call or response.
     */
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

    /**
     * Retrieves DNS records based on the provided DNS request.
     *
     * @param dnsRequest the DNS request object containing the domain name and resource record (rr).
     * @return a result object containing the response body with the DNS subdomain records information.
     * @throws RuntimeException if an error occurs while retrieving the DNS records.
     */
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

    /**
     * Deletes DNS subdomain records for the specified domain.
     *
     * @param dnsRequest The request object containing the domain name and record identifier (RR) to be deleted.
     * @return A Result object containing the response body of the delete operation, including details of the deleted records.
     * @throws RuntimeException if the delete operation fails with an unexpected status code.
     */
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

    /**
     * Deletes existing DNS subdomain records and then adds a new DNS record for the specified domain.
     *
     * @param dnsRequest The request containing the domain name, record type, record value,
     *                   and other details necessary to perform the operations.
     * @return A Result object containing a map with details of the deleted records and the newly added record.
     *         The map includes:
     *         - "deleteRecords": Response body of the delete operation.
     *         - "addRecord": Response body of the add operation.
     * @throws RuntimeException If either the delete operation or the add operation fails,
     *                          a RuntimeException is thrown containing the corresponding error message.
     */
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
