package io.intellij.devops.server.dnsapi;

import com.aliyun.alidns20150109.Client;
import com.aliyun.alidns20150109.models.DescribeSubDomainRecordsRequest;
import com.aliyun.alidns20150109.models.DescribeSubDomainRecordsResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.intellij.devops.server.dnsapi.config.properties.AccessTokenProperties;
import io.intellij.devops.server.dnsapi.config.properties.AliyunProperties;
import io.intellij.devops.server.dnsapi.config.properties.DnsApiProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * AliyunDnsApiTest
 * <p>
 * <a href="https://next.api.aliyun.com/api-tools/sdk/Alidns?version=2015-01-09&language=java-tea&tab=primer-doc">...</a>
 *
 * @author tech@intellij.io
 */
@SpringBootTest
@Slf4j
public class AliyunDnsApiTest {
    @Autowired
    private AliyunProperties aliyunProperties;

    @Autowired
    private AccessTokenProperties accessTokenProperties;

    @Autowired
    private DnsApiProperties ddnsApiProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetProperties() {
        log.info("aliyun properties = {}", aliyunProperties);
        log.info("access token properties = {}", accessTokenProperties);
        log.info("ddns properties = {}", ddnsApiProperties);
    }

    private Client createClient() throws Exception {
        // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考。
        // 建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html。
        Config config = new Config()
                // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID。
                .setAccessKeyId("")
                // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
                .setAccessKeySecret("");
        // Endpoint 请参考 https://api.aliyun.com/product/Alidns
        config.endpoint = aliyunProperties.getEndpoint();
        return new Client(config);
    }

    @Test
    public void testDescribeDomains() throws Exception {
        String subDomain = System.getProperty("DDNS_TEST_SUB_DOMAIN");
        Client client = createClient();
        DescribeSubDomainRecordsRequest describeSubDomainRecordsRequest = new DescribeSubDomainRecordsRequest()
                .setSubDomain(subDomain);
        try {
            DescribeSubDomainRecordsResponse describeSubDomainRecordsResponse = client.describeSubDomainRecords(describeSubDomainRecordsRequest);
            log.info(objectMapper.writeValueAsString(describeSubDomainRecordsResponse));
            log.info(objectMapper.writeValueAsString(describeSubDomainRecordsResponse.getBody()));

        } catch (TeaException error) {
            // 此处仅做打印展示，请谨慎对待异常处理，在工程项目中切勿直接忽略异常。
            // 错误 message
            System.out.println(error.getMessage());
            // 诊断地址
            System.out.println(error.getData().get("Recommend"));
            com.aliyun.teautil.Common.assertAsString(error.message);

        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 此处仅做打印展示，请谨慎对待异常处理，在工程项目中切勿直接忽略异常。
            // 错误 message
            System.out.println(error.getMessage());
            // 诊断地址
            System.out.println(error.getData().get("Recommend"));
            com.aliyun.teautil.Common.assertAsString(error.message);
        }

    }

}
