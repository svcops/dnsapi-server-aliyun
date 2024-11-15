package io.intellij.devops.server.dnsapi.services;

import com.aliyun.alidns20150109.models.AddDomainRecordRequest;
import com.aliyun.alidns20150109.models.AddDomainRecordResponse;
import com.aliyun.alidns20150109.models.DeleteSubDomainRecordsRequest;
import com.aliyun.alidns20150109.models.DeleteSubDomainRecordsResponse;
import com.aliyun.alidns20150109.models.DescribeDomainsResponse;
import com.aliyun.alidns20150109.models.DescribeSubDomainRecordsRequest;
import com.aliyun.alidns20150109.models.DescribeSubDomainRecordsResponse;
import com.aliyun.alidns20150109.models.UpdateDomainRecordRequest;
import com.aliyun.alidns20150109.models.UpdateDomainRecordResponse;

import java.util.List;

/**
 * DnsApiService
 *
 * @author tech@intellij.io
 */
public interface DnsApiService {

    int SUCCESS_STATUS_CODE = 200;

    List<String> domains();

    List<DescribeDomainsResponse> describeDomains();

    DescribeSubDomainRecordsResponse describeSubDomainRecords(String apiDomain,
                                                              DescribeSubDomainRecordsRequest describeSubDomainRecordsRequest);

    AddDomainRecordResponse addDomainRecord(String apiDomain,
                                            AddDomainRecordRequest addDomainRecordRequest);

    UpdateDomainRecordResponse updateDomainRecord(String apiDomain,
                                                  UpdateDomainRecordRequest updateDomainRecordRequest);

    DeleteSubDomainRecordsResponse deleteSubDomainRecords(String apiDomain,
                                                          DeleteSubDomainRecordsRequest deleteSubDomainRecordsRequest);

    DescribeSubDomainRecordsResponse describeSubDomainRecords(String apiDomain,
                                                              String subDomain);

    AddDomainRecordResponse addDomainRecord(String apiDomain,
                                            String domainName, String rr, String type, String ipv4);

    UpdateDomainRecordResponse updateDomainRecord(String apiDomain,
                                                  String rr, String recordId, String type, String ipv4);

    DeleteSubDomainRecordsResponse deleteSubDomainRecords(String apiDomain,
                                                          String domainName, String rr);

}
