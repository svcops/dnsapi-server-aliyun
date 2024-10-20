package io.intellij.devops.server.dnsapi.services.impl;

import com.aliyun.alidns20150109.Client;
import com.aliyun.alidns20150109.models.AddDomainRecordRequest;
import com.aliyun.alidns20150109.models.AddDomainRecordResponse;
import com.aliyun.alidns20150109.models.DeleteSubDomainRecordsRequest;
import com.aliyun.alidns20150109.models.DeleteSubDomainRecordsResponse;
import com.aliyun.alidns20150109.models.DescribeDomainsRequest;
import com.aliyun.alidns20150109.models.DescribeDomainsResponse;
import com.aliyun.alidns20150109.models.DescribeSubDomainRecordsRequest;
import com.aliyun.alidns20150109.models.DescribeSubDomainRecordsResponse;
import com.aliyun.alidns20150109.models.UpdateDomainRecordRequest;
import com.aliyun.alidns20150109.models.UpdateDomainRecordResponse;
import io.intellij.devops.server.dnsapi.config.properties.AliyunProperties;
import io.intellij.devops.server.dnsapi.services.DnsApiService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperties;

/**
 * DnsApiServiceImpl
 *
 * @author tech@intellij.io
 */
@ApplicationScoped
@Slf4j
public class DnsApiServiceImpl implements DnsApiService {

    @Inject
    @ConfigProperties
    AliyunProperties aliyunProperties;

    private Client getClient() {
        try {
            com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                    .setAccessKeyId(aliyunProperties.getAccessKeyId())
                    .setAccessKeySecret(aliyunProperties.getAccessKeySecret());

            config.endpoint = aliyunProperties.getEndpoint();
            return new Client(config);
        } catch (Exception e) {
            log.error("create client occurred error", e);
            throw new RuntimeException("create client occurred error");
        }
    }

    @Override
    public DescribeDomainsResponse describeDomains() {
        try {
            Client client = getClient();
            log.info("describeDomains|client={}", client);
            return client.describeDomains(new DescribeDomainsRequest());
        } catch (Exception e) {
            log.error("describeDomains|{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public DescribeSubDomainRecordsResponse describeSubDomainRecords(DescribeSubDomainRecordsRequest describeSubDomainRecordsRequest) {
        try {
            return getClient().describeSubDomainRecords(describeSubDomainRecordsRequest);
        } catch (Exception e) {
            log.error("describeSubDomainRecord|{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public AddDomainRecordResponse addDomainRecord(AddDomainRecordRequest addDomainRecordRequest) {
        try {
            return getClient().addDomainRecord(addDomainRecordRequest);
        } catch (Exception e) {
            log.error("addDomainRecord|{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public UpdateDomainRecordResponse updateDomainRecord(UpdateDomainRecordRequest updateDomainRecordRequest) {
        try {
            return getClient().updateDomainRecord(updateDomainRecordRequest);
        } catch (Exception e) {
            log.error("updateDomainRecord|{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public DeleteSubDomainRecordsResponse deleteSubDomainRecords(DeleteSubDomainRecordsRequest deleteSubDomainRecordsRequest) {
        try {
            return getClient().deleteSubDomainRecords(deleteSubDomainRecordsRequest);
        } catch (Exception e) {
            log.error("deleteSubDomainRecords|{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public DescribeSubDomainRecordsResponse describeSubDomainRecords(String subDomain) {
        /*
            --SubDomain
         */
        DescribeSubDomainRecordsRequest request = new DescribeSubDomainRecordsRequest().setSubDomain(subDomain);
        return describeSubDomainRecords(request);
    }

    @Override
    public AddDomainRecordResponse addDomainRecord(String domainName, String rr, String type, String ipv4) {
        /*
            --DomainName
            --RR
            --Type
            --Value
         */
        AddDomainRecordRequest request = new AddDomainRecordRequest()
                .setDomainName(domainName).setRR(rr).setType(type).setValue(ipv4);
        return addDomainRecord(request);
    }

    @Override
    public UpdateDomainRecordResponse updateDomainRecord(String rr, String recordId, String type, String ipv4) {
        /*
            --RR
            --RecordId
            --Type
            --Value
         */
        UpdateDomainRecordRequest request = new UpdateDomainRecordRequest()
                .setRR(rr).setRecordId(recordId).setType(type).setValue(ipv4);
        return updateDomainRecord(request);
    }

    @Override
    public DeleteSubDomainRecordsResponse deleteSubDomainRecords(String domainName, String rr) {
        /*
            --DomainName
            --RR
         */
        DeleteSubDomainRecordsRequest request = new DeleteSubDomainRecordsRequest()
                .setDomainName(domainName).setRR(rr);
        return deleteSubDomainRecords(request);
    }

}
