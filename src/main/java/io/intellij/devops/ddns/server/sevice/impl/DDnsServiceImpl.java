package io.intellij.devops.ddns.server.sevice.impl;

import com.aliyun.alidns20150109.models.AddDomainRecordResponse;
import com.aliyun.alidns20150109.models.DeleteSubDomainRecordsResponse;
import com.aliyun.alidns20150109.models.DescribeDomainsResponse;
import com.aliyun.alidns20150109.models.DescribeDomainsResponseBody;
import com.aliyun.alidns20150109.models.DescribeSubDomainRecordsResponse;
import com.aliyun.alidns20150109.models.DescribeSubDomainRecordsResponseBody;
import com.aliyun.alidns20150109.models.UpdateDomainRecordResponse;
import io.intellij.devops.ddns.server.config.properties.DnsApiProperties;
import io.intellij.devops.ddns.server.entites.ddns.DDnsResponse;
import io.intellij.devops.ddns.server.sevice.DDnsService;
import io.intellij.devops.ddns.server.sevice.DnsApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.intellij.devops.ddns.server.sevice.DnsApiService.SUCCESS_STATUS_CODE;

/**
 * DDnsServiceImpl
 *
 * @author tech@intellij.io
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Slf4j
public class DDnsServiceImpl implements DDnsService, InitializingBean {
    private final DnsApiService dnsApiService;

    private final DnsApiProperties ddnsApiProperties;

    @Override
    public DDnsResponse ddns(String domainName, String rr, String ipv4) {
        DescribeSubDomainRecordsResponse describeSubDomainRecordsResponse = dnsApiService.describeSubDomainRecords(rr + "." + domainName);
        if (describeSubDomainRecordsResponse.statusCode != SUCCESS_STATUS_CODE) {
            throw new RuntimeException("DescribeSubDomainRecords occurred error|statusCode={}" + describeSubDomainRecordsResponse.statusCode);
        }
        DescribeSubDomainRecordsResponseBody describeSubDomainRecordsResponseBody = describeSubDomainRecordsResponse.getBody();

        long totalCount = describeSubDomainRecordsResponseBody.getTotalCount();
        if (totalCount == 0) {
            // subDomain 没有解析记录，直接添加
            return add(domainName, rr, ipv4);
        } else if (totalCount == 1) {
            DescribeSubDomainRecordsResponseBody.DescribeSubDomainRecordsResponseBodyDomainRecords domainRecords = describeSubDomainRecordsResponseBody.getDomainRecords();
            DescribeSubDomainRecordsResponseBody.DescribeSubDomainRecordsResponseBodyDomainRecordsRecord record = domainRecords.record.getFirst();
            if (!"A".equals(record.getType())) {
                log.error("唯一的一条记录不是A记录，作为DDNS的子域名，保持一条记录方便操作，因此直接删除所有记录并重新添加");
                delete(domainName, rr);
                return this.add(domainName, rr, ipv4);
            } else {
                String recordId = record.getRecordId();
                String recordIpv4 = record.getValue();
                if (ipv4.equals(recordIpv4)) {
                    return DDnsResponse.of("request.ipv4 == record.value",
                            rr + "." + domainName, ipv4
                    );
                }
                return this.update(rr, recordId, ipv4, domainName);
            }
        } else {
            delete(domainName, rr);
            return add(domainName, rr, ipv4);
        }
    }


    private DDnsResponse add(String domainName, String rr, String ipv4) {
        AddDomainRecordResponse addDomainRecordResponse = dnsApiService.addDomainRecord(domainName, rr, IPV4_TYPE, ipv4);
        if (addDomainRecordResponse.statusCode == SUCCESS_STATUS_CODE) {
            return DDnsResponse.of(addDomainRecordResponse.getBody(), rr + "." + domainName, ipv4);
        } else {
            throw new RuntimeException("AddDomainRecord occurred error|statusCode={}" + addDomainRecordResponse.statusCode);
        }
    }

    private DDnsResponse update(String rr, String recordId, String ipv4, String domainName) {
        UpdateDomainRecordResponse updateDomainRecordResponse = dnsApiService.updateDomainRecord(rr, recordId, IPV4_TYPE, ipv4);
        if (SUCCESS_STATUS_CODE != updateDomainRecordResponse.statusCode) {
            throw new RuntimeException("UpdateDomainRecord occurred error|statusCode={}" + updateDomainRecordResponse.statusCode);
        }
        return DDnsResponse.of(updateDomainRecordResponse.getBody(), rr + "." + domainName, ipv4);
    }

    private void delete(String domainName, String rr) {
        DeleteSubDomainRecordsResponse deleteSubDomainRecordsResponse = dnsApiService.deleteSubDomainRecords(domainName, rr);
        if (SUCCESS_STATUS_CODE != deleteSubDomainRecordsResponse.statusCode) {
            throw new RuntimeException("DeleteSubDomainRecords occurred error|statusCode={}" + deleteSubDomainRecordsResponse.statusCode);
        }
        log.warn("删除DDNS子域名的所有解析记录|domainName={} rr={}", domainName, rr);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        DescribeDomainsResponse describeDomainsResponse = dnsApiService.describeDomains();
        if (SUCCESS_STATUS_CODE != describeDomainsResponse.statusCode) {
            throw new RuntimeException("DescribeDomains occurred error|statusCode={}" + describeDomainsResponse.statusCode);
        }

        List<DescribeDomainsResponseBody.DescribeDomainsResponseBodyDomainsDomain> domainList = describeDomainsResponse.getBody().getDomains().getDomain();
        if (CollectionUtils.isEmpty(domainList)) {
            throw new RuntimeException("当前阿里云DNS解析中没有域名");
        }

        // 验证 domain-acl 的域名列表
        List<String> domainAcl = ddnsApiProperties.getDomainAcl();
        if (CollectionUtils.isEmpty(domainAcl)) {
            throw new RuntimeException("domain acl is empty");
        }

        Set<String> accountDomains = domainList.stream().map(DescribeDomainsResponseBody.DescribeDomainsResponseBodyDomainsDomain::getDomainName).collect(Collectors.toSet());
        Set<String> aclDomains = new HashSet<>(domainAcl);

        if (!accountDomains.containsAll(aclDomains)) {
            throw new RuntimeException("域名控制列表的域名包含阿里云账号中不存在的域名");
        }
    }
}
