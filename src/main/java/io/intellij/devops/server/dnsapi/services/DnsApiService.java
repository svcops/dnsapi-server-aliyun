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

/**
 * DnsApiService
 *
 * @author tech@intellij.io
 */
public interface DnsApiService {

    int SUCCESS_STATUS_CODE = 200;

    DescribeDomainsResponse describeDomains();

    DescribeSubDomainRecordsResponse describeSubDomainRecords(DescribeSubDomainRecordsRequest describeSubDomainRecordsRequest);

    AddDomainRecordResponse addDomainRecord(AddDomainRecordRequest addDomainRecordRequest);

    UpdateDomainRecordResponse updateDomainRecord(UpdateDomainRecordRequest updateDomainRecordRequest);

    DeleteSubDomainRecordsResponse deleteSubDomainRecords(DeleteSubDomainRecordsRequest deleteSubDomainRecordsRequest);

    DescribeSubDomainRecordsResponse describeSubDomainRecords(String subDomain);

    AddDomainRecordResponse addDomainRecord(String domainName, String rr, String type, String ipv4);

    UpdateDomainRecordResponse updateDomainRecord(String rr, String recordId, String type, String ipv4);

    DeleteSubDomainRecordsResponse deleteSubDomainRecords(String domainName, String rr);

}
