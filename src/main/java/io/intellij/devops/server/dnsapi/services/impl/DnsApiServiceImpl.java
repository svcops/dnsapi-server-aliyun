package io.intellij.devops.server.dnsapi.services.impl;

import com.aliyun.alidns20150109.Client;
import com.aliyun.alidns20150109.models.AddDomainRecordRequest;
import com.aliyun.alidns20150109.models.AddDomainRecordResponse;
import com.aliyun.alidns20150109.models.DeleteSubDomainRecordsRequest;
import com.aliyun.alidns20150109.models.DeleteSubDomainRecordsResponse;
import com.aliyun.alidns20150109.models.DescribeDomainRecordsRequest;
import com.aliyun.alidns20150109.models.DescribeDomainRecordsResponse;
import com.aliyun.alidns20150109.models.DescribeDomainsRequest;
import com.aliyun.alidns20150109.models.DescribeDomainsResponse;
import com.aliyun.alidns20150109.models.DescribeDomainsResponseBody;
import com.aliyun.alidns20150109.models.DescribeSubDomainRecordsRequest;
import com.aliyun.alidns20150109.models.DescribeSubDomainRecordsResponse;
import com.aliyun.alidns20150109.models.UpdateDomainRecordRequest;
import com.aliyun.alidns20150109.models.UpdateDomainRecordResponse;
import com.aliyun.teaopenapi.models.Config;
import io.intellij.devops.server.dnsapi.config.properties.AliyunProperties;
import io.intellij.devops.server.dnsapi.config.properties.DnsApiProperties;
import io.intellij.devops.server.dnsapi.services.DnsApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * DnsApiServiceImpl
 *
 * @author tech@intellij.io
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Slf4j
public class DnsApiServiceImpl implements DnsApiService, InitializingBean {
    private final AliyunProperties aliyunProperties;
    private final DnsApiProperties dnsApiProperties;

    private final List<Client> clients = new CopyOnWriteArrayList<>();
    private final Map<String, Client> clientMap = new ConcurrentHashMap<>();
    private final List<String> domains = new ArrayList<>();

    private Client getClient(String apiDomain) {
        Client client = clientMap.get(apiDomain);
        if (Objects.isNull(client)) {
            throw new RuntimeException("client is null");
        }
        return client;
    }

    @Override
    public List<String> domains() {
        return this.domains;
    }

    @Override
    public List<String> domainAccessControlList() {
        return this.dnsApiProperties.getDomainAcl().stream().sorted().toList();
    }

    @Override
    public List<DescribeDomainsResponse> describeDomains() {
        try {
            List<DescribeDomainsResponse> responses = new ArrayList<>();
            for (Client client : clients) {
                DescribeDomainsResponse response = client.describeDomains(new DescribeDomainsRequest());
                responses.add(response);
            }
            return responses;
        } catch (Exception e) {
            log.error("describeDomains|{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public DescribeDomainRecordsResponse describeDomainRecords(String apiDomain,
                                                               DescribeDomainRecordsRequest describeDomainRecordsRequest) {
        try {
            return getClient(apiDomain).describeDomainRecords(describeDomainRecordsRequest);
        } catch (Exception e) {
            log.error("describeDomainRecords|{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private List<DescribeDomainsResponseWithClient> describeDomainsWithClient() {
        List<DescribeDomainsResponseWithClient> responses = new ArrayList<>();
        try {
            for (Client client : clients) {
                DescribeDomainsResponse response = client.describeDomains(new DescribeDomainsRequest());
                responses.add(new DescribeDomainsResponseWithClient(response, client));
            }
            return responses;
        } catch (Exception e) {
            log.error("describeDomainsWithClient|{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private record DescribeDomainsResponseWithClient(DescribeDomainsResponse response, Client client) {
    }

    @Override
    public DescribeSubDomainRecordsResponse describeSubDomainRecords(String apiDomain,
                                                                     DescribeSubDomainRecordsRequest describeSubDomainRecordsRequest) {
        try {
            return getClient(apiDomain).describeSubDomainRecords(describeSubDomainRecordsRequest);
        } catch (Exception e) {
            log.error("describeSubDomainRecord|{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public AddDomainRecordResponse addDomainRecord(String apiDomain,
                                                   AddDomainRecordRequest addDomainRecordRequest) {
        try {
            return getClient(apiDomain).addDomainRecord(addDomainRecordRequest);
        } catch (Exception e) {
            log.error("addDomainRecord|{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public UpdateDomainRecordResponse updateDomainRecord(String apiDomain,
                                                         UpdateDomainRecordRequest updateDomainRecordRequest) {
        try {
            return getClient(apiDomain).updateDomainRecord(updateDomainRecordRequest);
        } catch (Exception e) {
            log.error("updateDomainRecord|{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public DeleteSubDomainRecordsResponse deleteSubDomainRecords(String apiDomain,
                                                                 DeleteSubDomainRecordsRequest deleteSubDomainRecordsRequest) {
        try {
            return getClient(apiDomain).deleteSubDomainRecords(deleteSubDomainRecordsRequest);
        } catch (Exception e) {
            log.error("deleteSubDomainRecords|{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public DescribeSubDomainRecordsResponse describeSubDomainRecords(String apiDomain,
                                                                     String subDomain) {
        /*
            --SubDomain
         */
        DescribeSubDomainRecordsRequest request = new DescribeSubDomainRecordsRequest().setSubDomain(subDomain);
        return describeSubDomainRecords(apiDomain, request);
    }

    @Override
    public AddDomainRecordResponse addDomainRecord(String apiDomain,
                                                   String domainName, String rr, String type, String ipv4) {
        /*
            --DomainName
            --RR
            --Type
            --Value
         */
        AddDomainRecordRequest request = new AddDomainRecordRequest()
                .setDomainName(domainName).setRR(rr).setType(type).setValue(ipv4);
        return addDomainRecord(apiDomain, request);
    }

    @Override
    public UpdateDomainRecordResponse updateDomainRecord(String apiDomain,
                                                         String rr, String recordId, String type, String ipv4) {
        /*
            --RR
            --RecordId
            --Type
            --Value
         */
        UpdateDomainRecordRequest request = new UpdateDomainRecordRequest()
                .setRR(rr).setRecordId(recordId).setType(type).setValue(ipv4);
        return updateDomainRecord(apiDomain, request);
    }

    @Override
    public DeleteSubDomainRecordsResponse deleteSubDomainRecords(String apiDomain,
                                                                 String domainName, String rr) {
        /*
            --DomainName
            --RR
         */
        DeleteSubDomainRecordsRequest request = new DeleteSubDomainRecordsRequest()
                .setDomainName(domainName).setRR(rr);
        return deleteSubDomainRecords(apiDomain, request);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<AliyunProperties.AccessKey> accessKeyList = aliyunProperties.getAccessKeyList();
        for (AliyunProperties.AccessKey accessKey : accessKeyList) {
            Config config = new Config()
                    .setAccessKeyId(accessKey.getId())
                    .setAccessKeySecret(accessKey.getSecret());

            config.setEndpoint(aliyunProperties.getEndpoint());
            Client client = new Client(config);
            clients.add(client);
        }

        List<DescribeDomainsResponseWithClient> responseWithClients = this.describeDomainsWithClient();
        for (DescribeDomainsResponseWithClient responseWithClient : responseWithClients) {
            List<DescribeDomainsResponseBody.DescribeDomainsResponseBodyDomainsDomain> domainList =
                    getDescribeDomainsResponseBodyDomainsDomains(responseWithClient);

            for (DescribeDomainsResponseBody.DescribeDomainsResponseBodyDomainsDomain domain : domainList) {
                String domainName = domain.getDomainName();
                clientMap.put(domainName, responseWithClient.client);
            }
        }

        log.info("clientMap={}", clientMap);

        this.domains.addAll(clientMap.keySet().stream().toList());
    }

    private static @NotNull List<DescribeDomainsResponseBody.DescribeDomainsResponseBodyDomainsDomain> getDescribeDomainsResponseBodyDomainsDomains(DescribeDomainsResponseWithClient responseWithClient) {
        DescribeDomainsResponse response = responseWithClient.response;
        if (response.statusCode != SUCCESS_STATUS_CODE) {
            throw new RuntimeException("DescribeDomains occurred error|statusCode={}" + response.statusCode);
        }

        List<DescribeDomainsResponseBody.DescribeDomainsResponseBodyDomainsDomain> domainList = response.getBody().getDomains().getDomain();
        if (CollectionUtils.isEmpty(domainList)) {
            throw new RuntimeException("当前阿里云DNS解析中没有域名");
        }
        return domainList;
    }

}
